package contact.resource;

import java.net.URI;
import java.util.*;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import contact.entity.Contact;
import contact.service.ContactDao;

/**
 * 
 * @author Supavit 5510546671
 *
 */
@Path("/contacts")
@Singleton
public class ContactResource {
	
	private ContactDao dao = new ContactDao();
	
	@Context
	UriInfo uriInfo;
	
	public ContactResource(){
		System.out.println("Initializing ContactResource");
	}
	
	public Response getContacts(){
		
		List<Contact> contactList = dao.findAll();
		GenericEntity<List<Contact>> entity = new GenericEntity<List<Contact>>(contactList){};
		return Response.ok(entity).build();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@PathParam("id") long id){
		Contact contact = dao.find(id);
		return Response.ok(contact).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@QueryParam("q") String q){
		
		if ( q == null ) return getContacts();
		
		List<Contact> contactlist = dao.search(q);
		GenericEntity<List<Contact>> entity = new GenericEntity<List<Contact>>(contactlist){};
		return Response.ok(entity).build();
	}
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response createContact(JAXBElement<Contact> element, @Context UriInfo uriInfo){
		Contact contact = element.getValue();
		dao.save(contact);
		URI uri = uriInfo.getAbsolutePath();
		return Response.ok().header("Location", uriInfo).build();
		
	}
	@PUT
	@Path("{id}")
	public Response updateContact(@PathParam("id") long id, JAXBElement<Contact> element){

		Contact contact = element.getValue();
		dao.update(contact);
		return Response.ok().header("Location", uriInfo).build();
	}
	@DELETE
	@Path("{id}")
	public Response deleteContact(@PathParam("id") long id){
//		ContactDao dao = new ContactDao();
		dao.delete(id);
		return Response.ok("Deleted").build();
	}
	
}
