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
	@Path("/contacts/{id}")
	public Contact find(@PathParam("id") long id){
		
	}
	@GET
	@Path("/contacts")
	public List<Contact> findAll(){
		
	}
	@DELETE
	@Path("/contacts/{id}")
	public void delete(@PathParam("id") long id){
		
	}
	@POST
	@Path("/contacts")
	public void save(Contact contact){
		
	}
	@PUT
	@Path("/contacts/{id}")
	public void update(Contact contact){
		
	}
}
