package contact.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="contact")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactList {
	
	private List<Contact> ContactList = new ArrayList<Contact>();
	
	public ContactList(){
		
	}

	public List<Contact> getContactList() {
		return ContactList;
	}

	public void setContactList(List<Contact> contactList) {
		this.ContactList = contactList;
	}
}
