package contact.service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import contact.entity.Contact;

/**
 * Data access object for saving and retrieving contacts.
 * This DAO uses an in-memory list of person.
 * Use DaoFactory to get an instance of this class, such as:
 * dao = DaoFactory.getInstance().getContactDao()
 * 
 * @author jim, Supavit 5510546671
 * @version 2014.09.22
 */
public class ContactDao {
	private List<Contact> contacts;
	private AtomicLong nextId;
	
	public ContactDao() {
		contacts = new ArrayList<Contact>();
		nextId = new AtomicLong(1000L);
		//createTestContact(1);
	}
	
	/** add a single contact with given id for testing. */
	private void createTestContact(long id) {
		Contact test = new Contact("Test contact", "Joe Experimental", "none@testing.com");
		test.setId(id);
		contacts.add(test);
	}

	/** Find a contact by ID in contacts.
	 * @param the id of contact to find
	 * @return the matching contact or null if the id is not found
	 */
	public Contact find(long id) {
		for(Contact c : contacts) 
			if (c.getId() == id) return c;
		return null;
	}

	/**
	 * Search contacts in database using query string.
	 * @param querystr string used for searching
	 * @return list of all contacts
	 */
	public List<Contact> search(String querystr){
		List<Contact> contactlist = new ArrayList<Contact>();
		for ( Contact contact : contacts ) {
			 if ( contact.getTitle().contains(querystr) ){
				 contactlist.add(contact);
			 }
		}
		return contactlist;
	}
	
	/**
	 * Find all contacts in the database.
	 * @return list of all contacts
	 */
	public List<Contact> findAll() {
		return java.util.Collections.unmodifiableList(contacts);
	}

	/**
	 * Delete a saved contact.
	 * @param id the id of contact to delete
	 * @return true if contact is deleted, false otherwise.
	 */
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
	 * Save or replace a contact.
	 * If the contact.id is 0 then it is assumed to be a
	 * new (not saved) contact.  In this case a unique id
	 * is assigned to the contact.  
	 * If the contact.id is not zero and the contact already
	 * exists in saved contacts, the old contact is replaced.
	 * @param contact the contact to save or replace.
	 * @return true if saved successfully
	 */
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
	 * Update a Contact.  Only the non-null fields of the
	 * update are applied to the contact.
	 * @param update update info for the contact.
	 * @return true if the update is applied successfully.
	 */
	public boolean update(Contact update) {
		Contact contact = find(update.getId());
		if (contact == null) return false;
//		if (update.getEmail() == null || update.getName() == null || /*contact.getPhotoUrl() == null ||*/ update.getTitle() == null){
//			return false;
//		}
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
}