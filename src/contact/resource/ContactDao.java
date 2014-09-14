package contact.resource;

import java.util.List;

import javax.ws.rs.*;

/**
 * 
 * @author Supavit 5510546671
 *
 */
public class ContactDao {
	@GET
	public Contact find(@PathParam("id") long id){
		
	}
	@GET
	public List<Contact> findAll(){
		
	}
	@DELETE
	public void delete(@PathParam("id") long id){
		
	}
	@POST
	public void save(Contact contact){
		
	}
	@PUT
	public void update(Contact contact){
		
	}
}
