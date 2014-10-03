package contact.service;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import contactserver.JettyMain;

/**
 * Test the use of ETag whether it works correctly.
 * This test both success and fail conditions.
 * @author Supavit 5510546671
 * @version 2014.09.29
 *
 */
public class ETagTest {
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
	
	@Test
	public void testPostEtag(){
		StringContentProvider cProvider = new StringContentProvider(
				"<contact id=\"5555\">" +
				"<title>Knot</title>" +
				"<name>Supavit</name>" +
				"<email>tester@abc.com</email>" +
				"<phoneNumber>012345678</phoneNumber>" +
				"</contact>");
		try {
			
			
			ContentResponse responseFromPost = httpClient.newRequest(serviceUrl).content(cProvider, "application/xml").method(HttpMethod.POST).send();
			assertEquals("Return 201 CREATED", Status.CREATED.getStatusCode(), responseFromPost.getStatus());
			
			assertTrue("Test Posting ", !responseFromPost.getHeaders().get(HttpHeader.ETAG).isEmpty());
		} catch (InterruptedException | TimeoutException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDeleteEtag(){
		StringContentProvider cProvider = new StringContentProvider(
				"<contact id=\"5555\">" +
				"<title>Knot</title>" +
				"<name>Supavit</name>" +
				"<email>tester@abc.com</email>" +
				"<phoneNumber>012345678</phoneNumber>" +
				"</contact>");
		try {
			
			
			ContentResponse responseFromDelete = httpClient.newRequest(serviceUrl).content(cProvider, "application/xml").method(HttpMethod.POST).send();
			assertEquals("Return 201 CREATED", Status.CREATED.getStatusCode(), responseFromDelete.getStatus());
			assertTrue("Test Deleting ", !responseFromDelete.getHeaders().get(HttpHeader.ETAG).isEmpty());
		} catch (InterruptedException | TimeoutException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPutEtag(){
		StringContentProvider cProvider = new StringContentProvider(
				"<contact id=\"5555\">" +
				"<title>Tonk</title>" +
				"<name>Pavitsu</name>" +
				"<email>testing@abc.com</email>" +
				"<phoneNumber>0873172537</phoneNumber>" +
				"</contact>");
		try {
			
			ContentResponse responseFromPut = httpClient.newRequest(serviceUrl).content(cProvider, "application/xml").method(HttpMethod.POST).send();
			assertEquals("Return 201 OK", Status.CREATED.getStatusCode(), responseFromPut.getStatus());
//			assertTrue("Test Putting ", !responseFromPut.getHeaders().get(HttpHeader.ETAG).isEmpty());
			assertTrue("Test Putting ", !httpClient.GET(serviceUrl).getContentAsString().isEmpty());
		} catch (InterruptedException | TimeoutException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testGetETag() throws InterruptedException {
	   try {
			ContentResponse responseFromGet = httpClient.newRequest(serviceUrl).method(HttpMethod.GET).send();
			responseFromGet.getHeaders().get(HttpHeader.ETAG);
			assertTrue("Test Getting ", !responseFromGet.getHeaders().get(HttpHeader.ETAG).isEmpty());
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
