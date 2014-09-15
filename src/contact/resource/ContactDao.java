package contact.resource;

import java.util.*;

import javax.ws.rs.*;

/**
 * A Data Access Object class.
 * The separated class to handle CRUD operatons.
 * @author Supavit 5510546671
 *
 */

public class ContactDao {
	
	private Contact contact;
	
	public ContactDao(){
		
	}
	
	public Contact find(long id){
		return null;
	}
	
	public List<Contact> findAll(){
		return null;
	}
	
	public void delete(long id){
		System.out.println("invoking delete");
	}
	
	public void save(Contact contact){
		
	}
	
	public void update(Contact contact){
		
	}
}
