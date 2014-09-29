package contact.resource;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.*;

import javax.inject.Singleton;
import javax.persistence.Entity;
import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.JAXBElement;

import org.osgi.framework.hooks.service.FindHook;

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
		
		System.out.println("Initializing ContactResource");
	}
	
	/**
	 * Get all contacts from database.
	 * @return HTTP status code whether OK or not found
	 */
	public Response getContacts(){
		System.out.println("Get Contacts was called");
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
	 * Get contacts from database using id to find.
	 * @param id the id to search
	 * @return HTTP status code whether OK or not found
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@PathParam("id") long id, @Context Request request){
		System.out.println("Get contact using id was called");
		
		Contact contact = dao.find(id);
		
		EntityTag etag = new EntityTag(Integer.toString(contact.hashCode()));
		CacheControl cachecontrol = new CacheControl();
		//set max age to 3 minutes
		cachecontrol.setMaxAge(180);
		
		ResponseBuilder builder = request.evaluatePreconditions(/*contact.getLastModified(),*/ etag); 
		
		if(builder != null){
			builder.cacheControl(cachecontrol);
			return builder.build();
		}
		
		builder = Response.ok(contact, "application/xml");
		builder.cacheControl(cachecontrol);
		builder.tag(etag);
		return builder.build();
		
//		if(contact == null){
//			return Response.status(Response.Status.NOT_FOUND).build();
//		}
//		else{
//			return Response.ok(contact).build();
//		}
	}
	
	/**
	 * Get contacts from database using query string.
	 * @param title the query string to search
	 * @return HTTP status code  OK or not found
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@QueryParam("title") String title, @Context Request request){
		System.out.println("Get contact using query param was called");
		if ( title == null ) return getContacts();
				
		List<Contact> contactlist = dao.search(title);
		GenericEntity<List<Contact>> entity = new GenericEntity<List<Contact>>(contactlist){};
		
		EntityTag etag = new EntityTag(Integer.toString(entity.hashCode()));
		CacheControl cachecontrol = new CacheControl();
		//set max age to 3 minutes
		cachecontrol.setMaxAge(180);
		
		ResponseBuilder builder = request.evaluatePreconditions(etag); 
		
		if(builder != null){
			builder.cacheControl(cachecontrol);
			return builder.build();
		}
		
		builder = Response.ok(entity, "application/xml");
		builder.cacheControl(cachecontrol);
		builder.tag(etag);
		return builder.build();
		
		
//		if(entity != null){
//			return Response.ok(entity).build();
//		}
//		else{
//			return Response.status(Response.Status.NOT_FOUND).build();
//		}
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
				dao.find(contact.getId()).setLastModified(Calendar.getInstance().getTime());
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
	public Response updateContact(@PathParam("id") long id, JAXBElement<Contact> element, @Context Request request){
		System.out.println("update contact was called");
		
		Contact contact = element.getValue();
			
		if(contact.getId() == id){
			
			EntityTag etag = new EntityTag(Integer.toString(contact.hashCode()));
			Date timestamp = contact.getLastModified();
			System.out.println("Contact is "+ contact.getId());
			System.out.println("Timestamp is "+timestamp);
			ResponseBuilder builder = request.evaluatePreconditions(timestamp, etag);
			
			if(builder != null){
				return builder.build();
			}
					
			if(dao.update(contact)){
				builder = Response.noContent();
				return builder.build();
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
	 * Delete contact from the database.
	 * @param id the contact id of the person to be deleted
	 * @return HTTP status code OK if deleted successful
	 * 			NOT FOUND if that contact is not found
	 */
	@DELETE
	@Path("{id}")
	public Response deleteContact(@PathParam("id") long id){
		System.out.println("delete contact was called");
		if(dao.delete(id)) return Response.ok("Deleted").build();
		else{
			return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
		}
	}
	
}
