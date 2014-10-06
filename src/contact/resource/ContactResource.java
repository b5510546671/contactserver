package contact.resource;

import java.io.FileNotFoundException;
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
import contact.service.DaoFactory;
import contact.service.mem.MemContactDao;
import contact.service.mem.MemDaoFactory;

/**
 * A class that manages getting, creating, updating, and deleting contacts.
 * It replies using HTTP status codes responses.
 * 
 * @author Supavit 5510546671
 * @version 2014.09.23
 */
@Path("/contacts")
@Singleton
public class ContactResource {
	
	/** DAO that manages saving and getting contacts. */
	private ContactDao dao;

// this is not safe in singleton.
// could have 2 simultaneous requests using the same ContactResource object.
	/**
	 * URI information.
	 */
	@Context
	UriInfo uriInfo;
	
	/**
	 * Constructor of this class.
	 * @throws FileNotFoundException 
	 */
	public ContactResource() throws FileNotFoundException{
		
		dao = DaoFactory.getInstance().getContactDao();
// remove println() before submitting code.
		System.out.println("Initializing ContactResource");
	}
	
	/**
	 * Get all contacts from database.
	 * @return HTTP status code whether OK or not found
	 */
	public Response getContacts(){
// more println
		System.out.println("Get Contacts was called");
		List<Contact> contactList = dao.findAll();
		GenericEntity<List<Contact>> entity = new GenericEntity<List<Contact>>(contactList){};
		if(entity != null){
			return Response.ok(entity).build();
		}
		else{
// this will never be executed. The entity is never null.
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	/**
	 * Get contacts from database using id to find.
	 * @param id the id to search
	 * @return HTTP status code whether OK or not found
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@PathParam("id") long id){
		System.out.println("Get contact using id was called");
		Contact contact = dao.find(id);
		if(contact == null){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		else{
			return Response.ok(contact).build();
		}
	}
	
	/**
	 * Get contacts from database using query string.
	 * @param title the query string to search
	 * @return HTTP status code  OK or not found
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@QueryParam("title") String title){
		System.out.println("Get contact using query param was called");
		if ( title == null ) return getContacts();
// method should be dao.findByTitle()
		List<Contact> contactlist = dao.search(title);
		GenericEntity<List<Contact>> entity = new GenericEntity<List<Contact>>(contactlist){};
// its never null
		if(entity != null){
			return Response.ok(entity).build();
		}
		else{
			return Response.status(Response.Status.NOT_FOUND).build();

		}
	}
	
	/**
	 * Create contact and save to the database.
	 * @param element the input element
	 * @param uriInfo
	 * @return HTTP status code created, bad request, or conflict
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response createContact(JAXBElement<Contact> element, @Context UriInfo uriInfo){
		System.out.println("create contact was called");
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
	 * Update contact in the database.
	 * @param id the contact id of the person to update
	 * @param element the input element
	 * @return HTTP status code OK or not found
	 */
	@PUT
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response updateContact(@PathParam("id") long id, JAXBElement<Contact> element){
		System.out.println("update contact was called");
		Contact contact = element.getValue();
// what if client doesn't set the id attribute?  Since he is specifying id in
// in the URL it shouldn't be necessary.
		if(contact.getId() == id){
			if(dao.update(contact)){
				return Response.ok().header("Location", uriInfo).build();
			}
			else{
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		}
		else{
// should be BAD REQUEST
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		
	}
	
	/**
	 * Delete contact from the database.
	 * @param id the contact id of the person to be deleted
	 * @return HTTP status code OK if deleted successful
	 * 			NOT FOUND if that contact is not found
	 */
	@DELETE
	@Path("{id}")
	public Response deleteContact(@PathParam("id") long id){
		System.out.println("delete contact was called");
// check if id exists in persisted contacts
		if(dao.delete(id)) return Response.ok("Deleted").build();
		else{
			return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
		}
	}
	
}
