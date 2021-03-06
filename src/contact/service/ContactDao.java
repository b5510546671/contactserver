package contact.service;

import java.util.Date;
import java.util.List;

import contact.entity.Contact;

/**
 * Interface defines the operations required by 
 * a DAO for Contacts.
 * @author Supavit 5510546671
 *
 */
public interface ContactDao {

	/** Find a contact by ID in contacts.
	 * @param the id of contact to find
	 * @return the matching contact or null if the id is not found
	 */
	public abstract Contact find(long id);

	/**
	 * Search contacts in database using query string.
	 * @param querystr string used for searching
	 * @return list of all contacts
	 */
	public abstract List<Contact> search(String querystr);

	/**
	 * Find all contacts in the database.
	 * @return list of all contacts
	 */
	public abstract List<Contact> findAll();

	/**
	 * Delete a saved contact.
	 * @param id the id of contact to delete
	 * @return true if contact is deleted, false otherwise.
	 */
	public abstract boolean delete(long id);

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
	public abstract boolean save(Contact contact);

	/**
	 * Update a Contact.  Only the non-null fields of the
	 * update are applied to the contact.
	 * @param update update info for the contact.
	 * @return true if the update is applied successfully.
	 */
	public abstract boolean update(Contact update);
	
	/**
	 * Remove all elements in the memory.
	 */
	public abstract void removeAll();
	
	/**
	 * Getting the last modified date of a contact using id.
	 * @param id the id of a contact
	 */
	public abstract Date getLastModifiedById(long id);

}