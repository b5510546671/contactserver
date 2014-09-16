package contact.resource;

import java.net.URI;
import java.util.*;

import javax.inject.Singleton;
import javax.persistence.Entity;
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
 * A class that manages getting, creating, updating, and deleting contacts.
 * It responses using HTTP status codes.
 * 
 * @author Supavit 5510546671
 * @version 2014.09.16
 */
@Path("/contact")
@Singleton
public class ContactResource {
	
	/** DAO that manages saving and getting contacts. */
	private ContactDao dao = new ContactDao();
	
	@Context
	UriInfo uriInfo;
	
	public ContactResource(){
		System.out.println("Initializing ContactResource");
	}
	
	/**
	 * Get all contacts.
	 * @return HTTP status code
	 */
	public Response getContacts(){
		
		List<Contact> contactList = dao.findAll();
		GenericEntity<List<Contact>> entity = new GenericEntity<List<Contact>>(contactList){};
		if(entity != null){
			return Response.ok(entity).build();
		}
		else{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	/**
	 * Get contacts using id.
	 * @param id the id to search
	 * @return HTTP status code
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@PathParam("id") long id){
		Contact contact = dao.find(id);
		if(contact == null){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		else{
			return Response.ok(contact).build();
		}
	}
	
	/**
	 * Get contacts using query string
	 * @param q the query string to search
	 * @return HTTP status code
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@QueryParam("q") String q){
		
		if ( q == null ) return getContacts();
				
		List<Contact> contactlist = dao.search(q);
		GenericEntity<List<Contact>> entity = new GenericEntity<List<Contact>>(contactlist){};
		if(entity != null){
			return Response.ok(entity).build();
		}
		else{
			return Response.status(Response.Status.NOT_FOUND).build();

		}
	}
	
	/**
	 * Create contact.
	 * @param element the input element
	 * @param uriInfo
	 * @return HTTP status code
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response createContact(JAXBElement<Contact> element, @Context UriInfo uriInfo){
		Contact contact = element.getValue();
		if(dao.find(contact.getId()) == null){
			if( dao.save(contact) ){
				URI uri = uriInfo.getAbsolutePath();
				return Response.created(uri).build();
			}
			else{
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}
		else{
			return Response.status(Response.Status.CONFLICT).build();
		}
	
	}
	
	/**
	 * Update contact.
	 * @param id the contact id
	 * @param element the input element
	 * @return HTTP status code
	 */
	@PUT
	@Path("{id}")
	public Response updateContact(@PathParam("id") long id, JAXBElement<Contact> element){

		Contact contact = element.getValue();
		if(contact.getId() == id){
			if(dao.update(contact)){
				return Response.ok().header("Location", uriInfo).build();
			}
			else{
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		}
		else{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		
	}
	
	/**
	 * Delete contact.
	 * @param id the contact id
	 * @return HTTP status code
	 */
	@DELETE
	@Path("{id}")
	public Response deleteContact(@PathParam("id") long id){
		if(dao.delete(id)) return Response.ok("Deleted").build();
		else{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
}
