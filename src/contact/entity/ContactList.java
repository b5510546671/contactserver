package contact.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Stores all the contacts on this contact server.
 * @author Supavit 5510546671
 * @version 2014.09.23
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
}
