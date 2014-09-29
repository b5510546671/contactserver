package contact.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A person is a contact with a name, title, and email.
 * title is text to display for this contact in a list of contacts,
 * such as a nickname or company name.
 * 
 *@author Supavit 5510546671
 *@version 2014.09.28
 * 
 */
@Entity
@XmlRootElement(name="contact")
@XmlAccessorType(XmlAccessType.FIELD)
public class Contact implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@XmlAttribute
	private long id;
	
	//TODO how to specify a required element or attribute of an entity?
	@XmlElement(required=true,nillable=false)
	private String name;
	private String title;
	private String email;
	/** URL of photo */
	private String photoUrl;
	
	private String phoneNumber;
	
	@XmlElement(name="Last-Modified")
	private Date lastModified;
	
	public Contact() { }
	
	public Contact(String title, String name, String email, String phoneNumber ) {
		this.title = title;
		this.name = name;
		this.email = email;
		this.photoUrl = "";
		this.setPhoneNumber(phoneNumber);
	}

	public Contact(long id) {
		this.id = id;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photo) {
		this.photoUrl = photo;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
  
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return String.format("[%ld] %s (%s)", id, name, title);
	}
	
	/** Two contacts are equal if they have the same id,
	 * even if other attributes differ.
	 * @param other another contact to compare to this one.
	 */
	public boolean equals(Object other) {
		if (other == null || other.getClass() != this.getClass()) return false;
		Contact contact = (Contact) other;
		return contact.getId() == this.getId();
	}
	
	/**
	 * Update this contact's data from another Contact.
	 * The id field of the update must either be 0 or the same value as this contact!
	 * @param update the source of update values
	 */
	public void applyUpdate(Contact update) {
		if (update == null) return;
		if (update.getId() != 0 && update.getId() != this.getId() )
			throw new IllegalArgumentException("Update contact must have same id as contact to update");
		// Since title is used to display contacts, don't allow empty title
//		if (! isEmpty( update.getTitle()) ) this.setTitle(update.getTitle()); // empty nickname is ok
		// other attributes: allow an empty string as a way of deleting an attribute in update (this is hacky)
//		if (update.getName() != null ) this.setName(update.getName()); 
//		if (update.getEmail() != null) this.setEmail(update.getEmail());
//		if (update.getPhotoUrl() != null) this.setPhotoUrl(update.getPhotoUrl());
		this.setName(update.getName());
		this.setEmail(update.getEmail());
		this.setPhotoUrl(update.getPhotoUrl());
		this.setTitle(update.getTitle());
	}
	
	/**
	 * Test if a string is null or only whitespace.
	 * @param arg the string to test
	 * @return true if string variable is null or contains only whitespace
	 */
	private static boolean isEmpty(String arg) {
		return arg == null || arg.matches("\\s*") ;
	}

	public Date getLastModified() {
		return this.lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	/**
	 * Create the MD5 of the contact.
	 * @return String representation of MD5
	 */
	public String createMD5(){
		try {
	        MessageDigest m = MessageDigest.getInstance("MD5");
	        String s = this.id + "" + this.name + this.title + this.email + this.phoneNumber + this.photoUrl;
	        m.update(s.getBytes(), 0, s.length());
	        BigInteger i = new BigInteger(1,m.digest());
	        System.out.println(  String.format("%1$032x", i) );
	        return String.format("%1$032x", i);         
	    } catch (NoSuchAlgorithmException e) {
	    	System.out.println("Error from getMd5 method");
	        e.printStackTrace();
	    }
	    return null;
	}

	
}