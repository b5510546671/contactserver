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
}
