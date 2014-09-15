package contact.resource;

import javax.persistence.Entity;

/**
 * 
 * @author Supavit 5510546671
 *
 */
@Entity
public class Contact {
	private int id;
	private String title;
	private String name;
	private String email;
	private String phoneNumber;
	
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
