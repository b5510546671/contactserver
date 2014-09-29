package contact.service.mem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import contact.entity.Contact;
import contact.entity.ContactList;
import contact.service.ContactDao;

/**
 * Data access object for saving and retrieving contacts.
 * This DAO uses an in-memory list of person.
 * Use DaoFactory to get an instance of this class, such as:
 * dao = DaoFactory.getInstance().getContactDao()
 * 
 * @author jim, Supavit 5510546671
 * @version 2014.09.23
 */
public class MemContactDao implements ContactDao {
	private List<Contact> contacts;
	private AtomicLong nextId;
	
	public MemContactDao() {
		contacts = new ArrayList<Contact>();
		nextId = new AtomicLong(1000L);
		//createTestContact(1);
	}
	
	/** add a single contact with given id for testing. */
	private void createTestContact(long id) {
		Contact test = new Contact("Test contact", "Joe Experimental", "none@testing.com", "022245123");
		test.setId(id);
		contacts.add(test);
	}

	/** 
	 * @see contact.service.ContactDao#find(long)
	 */
	@Override
	public Contact find(long id) {
		for(Contact c : contacts) 
			if (c.getId() == id) return c;
		return null;
	}

	/** 
	 * @see contact.service.ContactDao#search(java.lang.String)
	 */
	@Override
	public List<Contact> search(String querystr){
		List<Contact> contactlist = new ArrayList<Contact>();
		for ( Contact contact : contacts ) {
			 if ( contact.getTitle().toLowerCase().contains(querystr.toLowerCase()) ){
				 contactlist.add(contact);
			 }
		}
		return contactlist;
	}
	
	/**
	 * @see contact.service.ContactDao#findAll()
	 */
	@Override
	public List<Contact> findAll() {
		return java.util.Collections.unmodifiableList(contacts);
	}

	/**
	 * @see contact.service.ContactDao#delete(long)
	 */
	@Override
	public boolean delete(long id) {
		for(int k=0; k<contacts.size(); k++) {
			if (contacts.get(k).getId() == id) {
				contacts.remove(k);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @see contact.service.ContactDao#save(contact.entity.Contact)
	 */
	@Override
	public boolean save(Contact contact) {
		if (contact.getId() == 0) {
			contact.setId( getUniqueId() );
			return contacts.add(contact);
		}
		// check if this contact is already in persistent storage
		Contact other  = find(contact.getId());
		if (other == contact) return true;
		if ( other != null ) contacts.remove(other);
		return contacts.add(contact);
	}

	/**
	 * @see contact.service.ContactDao#update(contact.entity.Contact)
	 */
	@Override
	public boolean update(Contact update) {
		Contact contact = find(update.getId());
		if (contact == null) return false;
		contact.applyUpdate(update);
		save(contact);
		return true;
	}
	
	/**
	 * Get a unique contact ID.
	 * @return unique id not in persistent storage
	 */
	private synchronized long getUniqueId() {
		long id = nextId.getAndAdd(1L);
		while( id < Long.MAX_VALUE ) {	
			if (find(id) == null) return id;
			id = nextId.getAndAdd(1L);
		}
		return id; // this should never happen
	}

	/**
	 * @see contact.service.ContactDao#removeAll()
	 */
	@Override
	public void removeAll() {
		for(Contact c : findAll()){
			delete(c.getId());
		}
	}

	/**
	 * @see contact.service.ContactDao#getLastModifiedById(long)
	 */
	@Override
	public Date getLastModifiedById(long id) {
		return find(id).getLastModified();
	}
}