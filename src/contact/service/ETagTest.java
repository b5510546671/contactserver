package contact.service;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.client.HttpClient;
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
	
//	@Test
//	public void testETag() throws InterruptedException {
//	    WebTarget webTarget = 
//	    		("rest").path("e_tag").queryParam("userId", "eric");
//
//	    Response head = webTarget.request().get();
//	    EntityTag eTag = head.getEntityTag();
//	    System.out.println(head.getStatus() + "\t" + eTag);
//	    Assert.assertEquals(200, head.getStatus());
//	    Thread.sleep(1000);
//
//	    Response head1 = webTarget.request().header("If-None-Match", eTag).get();
//	    System.out.println(head1.getStatus() + "\t" + head1.getEntityTag());
//	    Assert.assertEquals(304, head1.getStatus());
//	}
}
