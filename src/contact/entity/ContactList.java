package contact.entity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Stores all the contacts on this contact server.
 * @author Supavit 5510546671
 * @version 2014.09.29
 *
 */
@XmlRootElement(name="contacts")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactList {
	
	@XmlElement(name="contact")
	private List<Contact> contact = new ArrayList<Contact>();
	
	public ContactList(){
		
	}

	public List<Contact> getContactList() {
		return contact;
	}

	public void setContactList(List<Contact> contactList) {
		this.contact = contactList;
	}
	
	/**
	 * Create the MD5 of the contact list.
	 * @return MD5 String representation of the contact list
	 */
	public String createMD5(){
		try {
	        MessageDigest m = MessageDigest.getInstance("MD5");
	        String s = "";
	        for(Contact ct : contact){
	        	s += ct.createMD5();
	        }
	        m.update(s.getBytes(), 0, s.length());
	        BigInteger i = new BigInteger(1,m.digest());
	        return String.format("%1$032x", i);         
	    } catch (NoSuchAlgorithmException e) {
	    	System.out.println("Error from getMd5 method");
	        e.printStackTrace();
	    }
	    return null;
	}
}
