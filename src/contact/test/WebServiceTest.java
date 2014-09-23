package contact.test;



import static org.junit.Assert.*;

import java.net.URI;

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

import contactserver.JettyMain;
	
public class WebServiceTest {
	private static String serviceUrl;
	private static HttpClient httpClient;
	@BeforeClass
	public static void doFirst(){
		try {
			serviceUrl = JettyMain.startServer(8080);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@AfterClass
	public static void doLast() throws Exception{
		JettyMain.stopServer();
		httpClient.stop();
	}
	
	@Before
	public void doBeforeTest(){
		
	}
	
	@After
	public void doAfterTest(){
		
	}
	
	/**
	 * Test POST method with successful condition (Created).
	 */
	@Test
	public void testPostSuccess(){
		httpClient = new HttpClient();
		try {
			httpClient.start();
			StringContentProvider cProvider = new StringContentProvider(
					"<contact id=\"1\">" +
					"<title>Knot</title>" +
					"<name>Supavit</name>" +
					"<email>tester@abc.com</email>" +
					"<phoneNumber>012345678</phoneNumber>" +
					"</contact>");
			Request request = httpClient.newRequest(serviceUrl).content(cProvider, "application/xml").method(HttpMethod.POST);
			ContentResponse contentResponse = request.send();
			assertEquals("POST with Response CREATED", Status.CREATED.getStatusCode(), contentResponse.getStatus());
			contentResponse = httpClient.GET(serviceUrl+1);
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
		
	}
	
	@Test
	public void testPutFail(){
		
	}
	
	@Test
	public void testPutSuccess(){
		
	}
	
	@Test
	public void testGetSuccess(){
		
	}
	
	@Test
	public void testGetFail(){
		
	}
	
	@Test
	public void testDeleteSuccess(){
		
	}
	
	@Test
	public void testDeleteFail(){
		
	}
}
