package contact.service.mem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import contact.entity.Contact;
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
	
	/** instance of the entity DAO */
	private ContactDao daoInstance;
	
	public MemDaoFactory() {
		System.out.println("Create daoInstance.");
		daoInstance = new MemContactDao();
	}
	
	/**
	 * @see contact.service.DaoFactory#getContactDao()
	 */
	@Override
	public ContactDao getContactDao(){
		return daoInstance;
	}
	
	/**
	 * @see contact.service.DaoFactory#shutdown()
	 */
	@Override
	public void shutdown(){
		writeToFile();
		//TODO here's your chance to show your skill!
		// Use JAXB to write all your contacts to a file on disk.
		// Then recreate them the next time a MemFactoryDao and ContactDao are created.
	}
	
	public void writeToFile(){
		System.out.println("Write to File is called");
		try{
			for(Contact ctc : daoInstance.findAll()){
				System.out.println(ctc.getName());
			}
			Contact contact = daoInstance.findAll().get(0);
			File file = new File("C://Users/knotsupavit/Desktop/build.xml");
			FileOutputStream fileoutput = new FileOutputStream(file);
			Marshaller jaxbMarshaller = JAXBContext.newInstance(Contact.class).createMarshaller();
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