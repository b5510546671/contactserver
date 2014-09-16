package contact.resource;

import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import contact.entity.Contact;

/**
 * 
 * @author Supavit 5510546671
 *
 */
@Path("/contact")
@Singleton
public class ContactResource {
	@Context
	UriInfo uriInfo;
	public ContactResource(){
		System.out.println("Initializing ContactResource");
	}
	@GET
	public List<Contact> getContacts(){
		return null;
	}
	
	@GET
	@Path("{id}")
	@Produces("text/plain")
	public Contact getContact(@PathParam("id") long id){
		return null;
	}
	@GET
	@Path("{name}")
	@Produces("text/plain")
	public Contact getContact(@PathParam("name") String querystr){
		return null;
	}
	@POST
	public Response createContact(long id){
		System.out.println("Invoking createContact method");
		return null;
		
	}
	@PUT
	@Path("{id}")
	public Response updateContact(@PathParam("id") long id){
		return null;
		
	}
	@DELETE
	@Path("{id}")
	public void deleteContact(long id){
		
	}
	
}
