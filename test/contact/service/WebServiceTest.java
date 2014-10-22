package contact.service;



import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.client.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import contact.entity.Contact;
import contactserver.JettyMain;
import contact.service.ContactDao;
import contact.service.DaoFactory;
import contact.service.mem.MemDaoFactory;

/**
 * Tests for Contact Service.
 * It tests both success conditions and fail conditions.
 * It tries method PUT, POST, GET, DELETE.
 * @author Supavit 5510546671
 * @version 2014.09.23
 *
 */
public class WebServiceTest {
	
	private static String serviceUrl;
	private static HttpClient httpClient;
	private static ContactDao dao;
	
	StringContentProvider contactOne = new StringContentProvider(
			"<contact>" +
			"<title>One</title>" +
			"<name>Numero Uno</name>" +
			"<email>one@first.com</email>" +
			"<phoneNumber>012345678</phoneNumber>" +
			"</contact>");
	
	@BeforeClass
	public static void before(){
		
		try {
			dao = DaoFactory.getInstance().getContactDao();
			serviceUrl = JettyMain.startServer(8080);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@AfterClass
	public static void after(){
		try {
			JettyMain.stopServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Things to do before each test.
	 */
	@Before
	public void doBeforeTest(){
		try {
			httpClient = new HttpClient();
			httpClient.start();
			dao.removeAll();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Things to do after each test.
	 */
	@After
	public void doAfterTest(){
		try {
//			JettyMain.stopServer();
			httpClient.stop();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Test POST method with successful condition (Created).
	 */
	@Test
	public void testPostSuccess(){
		//httpClient = new HttpClient();
		try {
			//httpClient.start();
			StringContentProvider cProvider = new StringContentProvider(
					"<contact id=\"5555\">" +
					"<title>Knot</title>" +
					"<name>Supavit</name>" +
					"<email>tester@abc.com</email>" +
					"<phoneNumber>012345678</phoneNumber>" +
					"</contact>");
			Request request = httpClient.newRequest(serviceUrl).content(cProvider, "application/xml").method(HttpMethod.POST);
			ContentResponse contentResponse = request.send();
			assertEquals("POST with Response CREATED", Status.CREATED.getStatusCode(), contentResponse.getStatus());
			contentResponse = httpClient.GET(serviceUrl+5555);
			assertTrue("GET posted contact using ID",!contentResponse.getContentAsString().isEmpty());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test POST method with fail condition (Bad request).
	 */
	@Test
	public void testPostFail(){
//		httpClient = new HttpClient();
		try {
//			httpClient.start();
			StringContentProvider cProvider = new StringContentProvider("");
			Request request = httpClient.newRequest(serviceUrl).content(cProvider, "application/xml").method(HttpMethod.POST);
			ContentResponse contentResponse = request.send();
			assertEquals("POST with Response CONFLICT", Status.BAD_REQUEST.getStatusCode(), contentResponse.getStatus());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test PUT method with success condition (OK).
	 */
	@Test
	public void testPutValidUpdate(){
//		httpClient = new HttpClient();
		try{
//			httpClient.start();
			
			String url = createTestContact();
			StringContentProvider cProvider = new StringContentProvider(
					"<contact>" +
					"<title>Knotsupavit</title>" +
					"<name>Knot Reloaded</name>" +
					"<email>unlikelyemail@abc.com</email>" +
					"<phoneNumber>012345678</phoneNumber>" +
					"</contact>");
			Request request = httpClient.newRequest(url).content(cProvider, "application/xml").method(HttpMethod.PUT);
			ContentResponse contentResponse = request.send();
			assertEquals("PUT with Response OK", Status.OK.getStatusCode(), contentResponse.getStatus());
			contentResponse = httpClient.GET(serviceUrl);
			String content = contentResponse.getContentAsString();
			
			Matcher matcher;
			matcher = Pattern.compile(".*<name>Knot Reloaded</name>.*").matcher(content);
			assertTrue("Update name.", matcher.matches());
			
			matcher = Pattern.compile(".*<title>Knotsupavit</title>.*").matcher(content);
			assertTrue("Update title.", matcher.matches());
			
			matcher = Pattern.compile(".*<email>unlikelyemail@abc.com</email>.*").matcher(content);
			assertTrue("Update email.", matcher.matches());
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test PUT method with fail condition (Method Not Allowed).
	 */
	@Test
	public void testPutFail(){
		//httpClient = new HttpClient();
		try{
			//httpClient.start();
			StringContentProvider cProvider = new StringContentProvider(
					"<contact id=\"1234566543121\">" +
					"</contact>");
			Request request = httpClient.newRequest(serviceUrl).content(cProvider, "application/xml").method(HttpMethod.PUT);
			ContentResponse contentResponse = request.send();
			assertEquals("PUT with Response Method Not Allowed", Status.METHOD_NOT_ALLOWED.getStatusCode(), contentResponse.getStatus());
						
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test GET method with OK condition (OK).
	 * @throws Exception if cannot create test contact
	 */
	@Test
	public void testGetExistingContact() throws Exception {
		String url = createTestContact();
		
//		httpClient = new HttpClient();
		
//			httpClient.start();
			ContentResponse contentResponse = httpClient.GET(url);
			assertEquals("GET with Response OK", Status.OK.getStatusCode(), contentResponse.getStatus());
			assertTrue("Content is not null", !contentResponse.getContentAsString().isEmpty());
			
			// the response body must be the XML representation of a contact
			//TODO use JAXB to create a contact from the contentResponse
			JAXBContext ctx = JAXBContext.newInstance("Marshaller");
			//Marshaller m = ctx.createMarshaller();
			//XMLStreamWriter writer =  XMLOutputFactory.newInstance().createXMLStreamWriter();
			//m.marshal(contentResponse, writer);
	}
	
	private String createTestContact() throws InterruptedException,
			TimeoutException, ExecutionException {
		Request request = httpClient.newRequest(serviceUrl).content(contactOne, "application/xml").method(HttpMethod.POST);
		ContentResponse response = request.send();
		Assume.assumeTrue(response.getStatus() == Status.CREATED.getStatusCode());
		String url = response.getHeaders().get("Location");
		assertNotNull("Created a test contact but no Loation provided", url);
		// get the final component, which should be an id
		return url;
	}
	/**
	 * Test GET method with fail condition (NO CONTENT).
	 */
	@Test
	public void testGetNonexistentContact( ) {
//		httpClient = new HttpClient();
		try{
//			httpClient.start();
			ContentResponse contentResponse = httpClient.GET(serviceUrl);
			assertEquals("GET with Response NO CONTENT", Status.OK.getStatusCode(), contentResponse.getStatus());
			contentResponse = httpClient.GET(serviceUrl + "/999999999");
			assertEquals("GET with Response NOT FOUND", Status.NOT_FOUND.getStatusCode(), contentResponse.getStatus());
// This test is not relevant.
//			assertTrue("Content is null", contentResponse.getContentAsString().isEmpty());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Test DELETE method with successful condition (OK).
	 */
	@Test
	public void testDeleteSuccess(){
//		httpClient = new HttpClient();
		try {
//			httpClient.start();
			StringContentProvider cProvider = new StringContentProvider(
					"<contact id=\"5555\">" +
					"<title>Knot</title>" +
					"<name>Supavit</name>" +
					"<email>tester@abc.com</email>" +
					"<phoneNumber>012345678</phoneNumber>" +
					"</contact>");
			
			ContentResponse contentResponse = httpClient.newRequest(serviceUrl).content(cProvider, "application/xml").method(HttpMethod.POST).send();
			Request request = httpClient.newRequest(serviceUrl + 5555).method(HttpMethod.DELETE);
			contentResponse = request.send();
			assertEquals("DELETE with Response OK", Status.OK.getStatusCode(), contentResponse.getStatus());
			contentResponse = httpClient.GET(serviceUrl+5555);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test DELETE method with fail condition (BAD REQUEST).
	 */
	@Test
	public void testDeleteFail(){
		httpClient = new HttpClient();
		try{
			httpClient.start();
			Request request = httpClient.newRequest(serviceUrl).method(HttpMethod.DELETE);
			ContentResponse contentResponse = request.send();
			assertEquals("DELETE with Response Method Not Allowed", Status.METHOD_NOT_ALLOWED.getStatusCode(), contentResponse.getStatus());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
