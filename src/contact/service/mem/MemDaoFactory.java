package contact.service.mem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import contact.Config;
import contact.entity.Contact;
import contact.entity.ContactList;
import contact.service.ContactDao;
import contact.service.DaoFactory;
import contact.service.jpa.JpaDaoFactory;

/**
 * Manage instances of Data Access Objects (DAO) used in the app.
 * This enables you to change the implementation of the actual ContactDao
 * without changing the rest of your application.
 * 
 * @author jim, Supavit 5510546671
 * @version 2014.09.23
 */
public class MemDaoFactory extends DaoFactory {
	
	private static final String CONTACTS_FILE = Config.getWritableContactsFile();
	
	/** instance of the entity DAO */
	private ContactDao daoInstance;
	
	public MemDaoFactory() throws FileNotFoundException {
		System.out.println("Create daoInstance.");
		daoInstance = new MemContactDao();
		loadFromFile(CONTACTS_FILE);
	}
	
	/**
	 * @see contact.service.DaoFactory#getContactDao()
	 */
	@Override
	public ContactDao getContactDao(){
		return daoInstance;
	}
	
	/**
	 * Load saved file.
	 * @param fileLocator the location of the file
	 */
	public void loadFromFile(String fileLocator) throws FileNotFoundException{
		System.out.println("Load from file is called");
		ContactList ctlist = null;
		File inputFile = new File(fileLocator);
		if ( ! inputFile.exists() ) return;
		
		try {
			Unmarshaller unmarshaller = JAXBContext.newInstance(ContactList.class).createUnmarshaller();
			ctlist = (ContactList) unmarshaller.unmarshal(inputFile);
		}catch(JAXBException jax) {
			System.err.println(jax);
		}
		if(ctlist != null) {
			List<Contact> contactList = ctlist.getContactList();
			
			for(Contact contact : contactList){
				daoInstance.save(contact);
			}
		}
		
		
	}
	
	
	/**
	 * @see contact.service.DaoFactory#shutdown()
	 */
	@Override
	public void shutdown(){
		writeToFile();
	}
	
	/**
	 * Create xml file of contact(s) based on user's input.
	 */
	public void writeToFile(){
		System.out.println("Write to File is called");
		try{
			for(Contact ctc : daoInstance.findAll()){
				System.out.println(ctc.getName());
			}
			ContactList contact = new ContactList();
			contact.setContactList(daoInstance.findAll());
			File file = new File(CONTACTS_FILE);
			FileOutputStream fileoutput = new FileOutputStream(file);
			Marshaller jaxbMarshaller = JAXBContext.newInstance(ContactList.class).createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(contact, fileoutput);
		}catch(JAXBException ex){
			System.out.println("Error");
			Logger.getLogger(MemDaoFactory.class.getName()).warning(ex.getMessage());
		}catch(FileNotFoundException ex){
			Logger.getLogger(MemDaoFactory.class.getName()).warning(ex.getMessage());
		}
	}
	
}