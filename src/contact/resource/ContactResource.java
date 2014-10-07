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
import contact.entity.ContactList;
import contact.service.ContactDao;
import contact.service.DaoFactory;
import contact.service.mem.MemContactDao;
import contact.service.mem.MemDaoFactory;

/**
 * A class that manages getting, creating, updating, and deleting contacts.
 * It replies using HTTP status codes responses.
 * 
 * @author Supavit 5510546671
 * @version 2014.10.04
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
		
	}
	
	/**
	 * Get all contacts from the database.
	 * @param match data from parameter If-Match
	 * @param noneMatch data from parameter If-None-Match
	 * @param request the request from client
	 * @return HttpStatus codes or new Data
	 */
	public Response getContacts(@HeaderParam("If-Match") String match, @HeaderParam("If-None-Match") String noneMatch, @Context Request request){
		ContactList contactlist = new ContactList();
		contactlist.setContactList(dao.findAll());
		//GenericEntity<List<Contact>> entity = new GenericEntity<List<Contact>>(contactList){};
		
		EntityTag etag = new EntityTag(contactlist.createMD5());
		CacheControl cachecontrol = new CacheControl();
		//set max age to infinity
		cachecontrol.setMaxAge(-1);
		
		if(match == null && noneMatch == null){
			return replyInformationWithGet(etag, request, cachecontrol, contactlist);
		}
		if(match != null && noneMatch != null){
			return Response.status(Response.Status.BAD_REQUEST).build(); 
		}
		
		if(match != null){
			System.out.println("match is "+ match);
			System.out.println("contactlist.createMD5 is " + contactlist.createMD5());
			if(match.equals(contactlist.createMD5())){
				return Response.status(Response.Status.NOT_MODIFIED).build();
//				return replyInformationWithGet(etag, request, cachecontrol, contactlist);

			}
			else{
				System.out.println("---Print from precondition failed");
				return Response.status(Response.Status.PRECONDITION_FAILED).build();
			}
		}
		else{
			
		}
		if(noneMatch != null){
			if(!noneMatch.equals(contactlist.createMD5())){
				return replyInformationWithGet(etag, request, cachecontrol, contactlist);
			}
			else{
				return Response.status(Response.Status.NOT_MODIFIED).build(); 
			}
		}
		else{
			
		}
		
		return null;
		
	}
	
	/**
	 * Get contacts from database using id to find.
	 * @param match data from parameter If-Match
	 * @param noneMatch data from parameter If-None-Match
	 * @param id the id to be searched
	 * @param request the request from client
	 * @return HttpStatus codes or new data
	 */
	@GET
	@Path("{id: [0-9]\\d*}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@HeaderParam("If-Match") String match, @HeaderParam("If-None-Match") String noneMatch, @PathParam("id") long id, @Context Request request){
		
		Contact contact = dao.find(id);
		
		EntityTag etag = new EntityTag(contact.createMD5());
		CacheControl cachecontrol = new CacheControl();
		//set max age to infinity
		cachecontrol.setMaxAge(-1);
		
		if(match == null && noneMatch == null){
			return replyInformationWithGet(etag, request, cachecontrol, contact);
		}
		
		if(match != null && noneMatch != null){
			return Response.status(Response.Status.BAD_REQUEST).build(); 
		}
		if(match != null){
			if(match.equals(contact.createMD5())){
				return Response.status(Response.Status.NOT_MODIFIED).build();
			}
			else{
				return Response.status(Response.Status.PRECONDITION_FAILED).build();
			}
		}
		if(noneMatch != null){
			if( !noneMatch.equals(contact.createMD5())){
				return replyInformationWithGet(etag, request, cachecontrol, contact);
			}
			else{
				return Response.status(Response.Status.NOT_MODIFIED).build();
			}
		}
		
		return null;
	}
	
	

	/**
	 * Get contacts from database using query string.
	 * @param match data from parameter If-Match
	 * @param noneMatch data from parameter If-None-Match
	 * @param title the query string to be searched
	 * @param request the request from client
	 * @return HttpStatus codes or new data
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@HeaderParam("If-Match") String match, @HeaderParam("If-None-Match") String noneMatch, @QueryParam("title") String title, @Context Request request){
		if ( title == null ) return getContacts(match, noneMatch, request);
		
		ContactList contactlist = new ContactList();
		contactlist.setContactList(dao.search(title));
		
		
		//GenericEntity<List<Contact>> entity = new GenericEntity<List<Contact>>(contactlist){};
		
		EntityTag etag = new EntityTag(contactlist.createMD5());
		CacheControl cachecontrol = new CacheControl();
		//set max age to infinity
		cachecontrol.setMaxAge(-1);
		
		if(match == null && noneMatch == null){
			return replyInformationWithGet(etag, request, cachecontrol, contactlist);
		}
		if(match != null && noneMatch != null){
			return Response.status(Response.Status.BAD_REQUEST).build(); 
		}
		
		if(match != null){
			if(match.equals(contactlist.createMD5())){
				return Response.status(Response.Status.NOT_MODIFIED).build();
			}
			else{
				return Response.status(Response.Status.PRECONDITION_FAILED).build();
			}
		}
		else{
			
		}
		if(noneMatch != null){
			if(!noneMatch.equals(contactlist.createMD5())){
				return replyInformationWithGet(etag, request, cachecontrol, contactlist);
			}
			else{
				return Response.status(Response.Status.NOT_MODIFIED).build(); 
			}
		}
		else{
			
		}
		
		return null;
	}
	
	/**
	 * Create contact and save to the database.
	 * @param element the input element
	 * @param uriInfo
	 * @param request the request from client
	 * @return HttpStatus codes or new data
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response createContact(JAXBElement<Contact> element, @Context UriInfo uriInfo, @Context Request request){
		Contact contact = element.getValue();
		
		if(dao.find(contact.getId()) == null){
			
			EntityTag etag = new EntityTag(contact.createMD5());
			CacheControl cachecontrol = new CacheControl();
			//set max age to infinity
			cachecontrol.setMaxAge(-1);
			
			ResponseBuilder builder = request.evaluatePreconditions(etag);
			if(builder != null){
				builder.cacheControl(cachecontrol);
				return builder.build();
			}
			
			
			if( dao.save(contact) ){
				URI uri = uriInfo.getAbsolutePath();
				
				builder = Response.created(uri);
				builder.cacheControl(cachecontrol);
				builder.tag(etag);
				return builder.build();
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
	 * @param match data from parameter If-Match
	 * @param noneMatch data from parameter If-None-Match
	 * @param id the contact id of the person to update
	 * @param element the input element
	 * @param request the request from client
	 * @return HttpStatus codes or new data
	 */
	@PUT
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response updateContact(@HeaderParam("If-None-Match") String noneMatch, @HeaderParam("If-Match") String match, @PathParam("id") long id, JAXBElement<Contact> element, @Context Request request){
		
		Contact contact = element.getValue();
		System.out.println("..."+ contact.getId() + "....");
		System.out.println("---" + id + "----");
		if(contact.getId() == id){
			
			EntityTag etag = new EntityTag(contact.createMD5());
			
						
			if(match != null && noneMatch != null){
				return Response.status(Response.Status.BAD_REQUEST).build(); 
			}
			if(match == null && noneMatch == null){
				return replyInformationWithUpdate(etag, request, contact);
			}
			
			if(match != null){
				System.out.println("--PUT match is " + match);
				System.out.println("--PUT contact.createMD5() is " + contact.createMD5());
				if(match.equals(contact.createMD5())){
					return replyInformationWithUpdate(etag, request, contact);
				}
				else{
					return Response.status(Response.Status.PRECONDITION_FAILED).build();
				}
			}
			
			if(noneMatch != null){
				if(!noneMatch.equals(contact.createMD5())){
					return replyInformationWithUpdate(etag, request, contact);
				}
				else{
					return Response.status(Response.Status.PRECONDITION_FAILED).build(); 
				}
			}
		}
		else{
			System.out.println("---Content Not Found----");
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return null;
	}
	
	/**
	 * Delete contact from the database.
	 * @param match data from parameter If-Match
	 * @param noneMatch data from parameter If-None-Match
	 * @param id the contact id of the person to be deleted
	 * @param request the request from client
	 * @return HttpStatus codes or new data
	 */
	@DELETE
	@Path("{id}")
	public Response deleteContact(@HeaderParam("If-Match") String match, @HeaderParam("If-None-Match") String noneMatch, @PathParam("id") long id, @Context Request request){
		
		Contact contact = dao.find(id);
		EntityTag etag = new EntityTag(contact.createMD5());
		
		if(match != null && noneMatch != null){
			return Response.status(Response.Status.BAD_REQUEST).build(); 
		}
		if(match == null && noneMatch == null){
			return replyInformationWithDelete(etag, request, contact);
		}
		
		if(match != null){
			if(match.equals(contact.createMD5())){
				return replyInformationWithDelete(etag, request, contact);
			}
			else{
				return Response.status(Response.Status.PRECONDITION_FAILED).build();
			}
		}
		
		if(noneMatch != null){
			if(!noneMatch.equals(contact.createMD5())){
				return replyInformationWithDelete(etag, request, contact);
			}
			else{
				return Response.status(Response.Status.PRECONDITION_FAILED).build(); 
			}
		}
		
		
		if(dao.delete(id)){
			return Response.ok("Deleted").build();
		}
		else{
			return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
		}
	}
	
	/**
	 * Data to be replied when get list of contacts is called.
	 * @param etag the etag of the contact
	 * @param request the request from client 
	 * @param cachecontrol
	 * @param contactlist the requested list of contacts
	 * @return data of the contact list
	 */
	public Response replyInformationWithGet(EntityTag etag, Request request, CacheControl cachecontrol, ContactList contactlist){
		ResponseBuilder builder = request.evaluatePreconditions(etag); 
		
		if(builder != null){
			builder.cacheControl(cachecontrol);
			System.out.println("1st"+builder.build()+"");
			return builder.build();
		}
		
		builder = Response.ok(contactlist, "application/xml");
		builder.cacheControl(cachecontrol);
		builder.tag(etag);
		System.out.println("2nd"+builder.build()+"");
		
		return builder.build();
	}
	
	/**
	 * Data to be replied when get each contact is called.
	 * @param etag the etag of the contact
	 * @param request the request from client 
	 * @param cachecontrol
	 * @param contact the requested contact
	 * @return data of the contact
	 */
	public Response replyInformationWithGet(EntityTag etag, Request request, CacheControl cachecontrol, Contact contact){
		ResponseBuilder builder = request.evaluatePreconditions(etag); 
		
		if(builder != null){
			builder.cacheControl(cachecontrol);
			
			return builder.build();
		}
		
		builder = Response.ok(contact, "application/xml");
		builder.cacheControl(cachecontrol);
		builder.tag(etag);
		return builder.build();
	}
	
	/**
	 * Data to be replied when update each contact is called.
	 * @param etag the etag of the contact
	 * @param request the request from client 
	 * @param contact the requested contact
	 * @return updated data of the contact or HttpStatus codes if updating is unsuccessful
	 */
	public Response replyInformationWithUpdate(EntityTag etag, Request request, Contact contact){
		ResponseBuilder builder = request.evaluatePreconditions(etag);
		if(builder != null){
			System.out.println("====1st "+builder.build()+"");
			return builder.build();
		}
				
		if(dao.update(contact)){
			builder = Response.ok();
			System.out.println("=====2nd "+builder.build()+"");
			return builder.build();
		}
		else{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	/**
	 * Data to be replied when delete each contact is called.
	 * @param etag the etag of the contact
	 * @param request the request from client 
	 * @param contact the requested contact
	 * @return HttpStatus codes OK if successfully deleted or Method Not Allowed if there's error
	 */
	public Response replyInformationWithDelete(EntityTag etag, Request request, Contact contact){
		if(dao.delete(contact.getId())){
			return Response.ok("Deleted").build();
		}
		else{
			return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
		}
	}
	
}
