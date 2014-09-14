package contact.resource;

import javax.persistence.Entity;

@Entity
public class Contact {
	private int id;
	private String title;
	private String name;
	private String email;
	private String phoneNumber;
}
