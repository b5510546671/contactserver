package contact.test;



import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response.Status;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.client.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import contact.entity.Contact;
import contact.service.ContactDao;
import contact.service.DaoFactory;
import contact.service.mem.MemDaoFactory;
import contactserver.JettyMain;

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
	
//	/**
//	 * Test POST method with successful condition (Created).
//	 */
//	@Test
//	public void testPostSuccess(){
//		httpClient = new HttpClient();
//		try {
//			httpClient.start();
//			StringContentProvider cProvider = new StringContentProvider(
//					"<contact id=\"5555\">" +
//					"<title>Knot</title>" +
//					"<name>Supavit</name>" +
//					"<email>tester@abc.com</email>" +
//					"<phoneNumber>012345678</phoneNumber>" +
//					"</contact>");
//			Request request = httpClient.newRequest(serviceUrl).content(cProvider, "application/xml").method(HttpMethod.POST);
//			ContentResponse contentResponse = request.send();
//			assertEquals("POST with Response CREATED", Status.CREATED.getStatusCode(), contentResponse.getStatus());
//			contentResponse = httpClient.GET(serviceUrl+5555);
//			assertTrue("GET posted contact using ID",!contentResponse.getContentAsString().isEmpty());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * Test POST method with fail condition (Bad request).
//	 */
//	@Test
//	public void testPostFail(){
//		httpClient = new HttpClient();
//		try {
//			httpClient.start();
//			StringContentProvider cProvider = new StringContentProvider(
//					"<contact id=\"5555\">" +
//					"<title>Knot</title>" +
//					"<name>Supavit</name>" +
//					"<email>tester@abc.com</email>" +
//					"<phoneNumber>012345678</phoneNumber>" +
//					"</contact>");
//			Request request = httpClient.newRequest(serviceUrl).content(cProvider, "application/xml").method(HttpMethod.POST);
//			ContentResponse contentResponse = request.send();
//			assertEquals("POST with Response CONFLICT", Status.CONFLICT.getStatusCode(), contentResponse.getStatus());
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * Test PUT method with success condition (OK).
//	 */
//	@Test
//	public void testPutSuccess(){
//		httpClient = new HttpClient();
//		try{
//			httpClient.start();
//			StringContentProvider cProvider = new StringContentProvider(
//					"<contact id=\"5000\">" +
//					"<title>Knot</title>" +
//					"<name>Supavit</name>" +
//					"<email>tester@abc.com</email>" +
//					"<phoneNumber>012345678</phoneNumber>" +
//					"</contact>");
//			Request request = httpClient.newRequest(serviceUrl).content(cProvider, "application/xml").method(HttpMethod.PUT);
//			ContentResponse contentResponse = request.send();
//			assertEquals("PUT with Response OK", Status.OK.getStatusCode(), contentResponse.getStatus());
//			contentResponse = httpClient.GET(serviceUrl);
//			String content = contentResponse.getContentAsString();
//			
//			Matcher matcher;
//			matcher = Pattern.compile(".*<name>Kongwudhi</name>.*").matcher(content);
//			assertTrue("Update name.", matcher.matches());
//			
//			matcher = Pattern.compile(".*<title>Knotsupavit</title>.*").matcher(content);
//			assertTrue("Update title.", matcher.matches());
//			
//			matcher = Pattern.compile(".*<email>knotsupavit@knotsupavit.com</email>.*").matcher(content);
//			assertTrue("Update email.", matcher.matches());
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * Test PUT method with fail condition (BAD Request).
//	 */
//	@Test
//	public void testPutFail(){
//		httpClient = new HttpClient();
//		try{
//			httpClient.start();
//			StringContentProvider cProvider = new StringContentProvider(
//					"<contact id=\"1234566543121\">" +
//					"</contact>");
//			Request request = httpClient.newRequest(serviceUrl).content(cProvider, "application/xml").method(HttpMethod.PUT);
//			ContentResponse contentResponse = request.send();
//			assertEquals("PUT with Response BAD REQUEST", Status.BAD_REQUEST.getStatusCode(), contentResponse.getStatus());
//						
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * Test GET method with OK condition (OK).
//	 */
//	@Test
//	public void testGetSuccess(){
//		long id = 1;
//		httpClient = new HttpClient();
//		try{
//			httpClient.start();
//			ContentResponse contentResponse = httpClient.GET(serviceUrl+id);
//			assertEquals("GET with Response OK", Status.OK.getStatusCode(), contentResponse.getStatus());
//			assertTrue("Content is not null", !contentResponse.getContentAsString().isEmpty());
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * Test GET method with fail condition (NO CONTENT).
//	 */
//	@Test
//	public void testGetFail(){
//		httpClient = new HttpClient();
//		try{
//			httpClient.start();
//			ContentResponse contentResponse = httpClient.GET(serviceUrl);
//			assertEquals("GET with Response NO CONTENT", Status.NO_CONTENT.getStatusCode(), contentResponse.getStatus());
//			assertTrue("Content is null", contentResponse.getContentAsString().isEmpty());
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * Test DELETE method with successful condition (OK).
//	 */
//	@Test
//	public void testDeleteSuccess(){
//		httpClient = new HttpClient();
//		try {
//			httpClient.start();
//			StringContentProvider cProvider = new StringContentProvider(
//					"<contact id=\"5555\">" +
//					"<title>Knot</title>" +
//					"<name>Supavit</name>" +
//					"<email>tester@abc.com</email>" +
//					"<phoneNumber>012345678</phoneNumber>" +
//					"</contact>");
//			
//			ContentResponse contentResponse = httpClient.newRequest(serviceUrl).content(cProvider, "application/xml").method(HttpMethod.POST).send();
//			Request request = httpClient.newRequest(serviceUrl + 5555).method(HttpMethod.DELETE);
//			contentResponse = request.send();
//			assertEquals("DELETE with Response OK", Status.OK.getStatusCode(), contentResponse.getStatus());
//			contentResponse = httpClient.GET(serviceUrl+5555);
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
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
