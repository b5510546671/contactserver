package contact.service.jpa;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import jersey.repackaged.com.google.common.collect.Lists;
import contact.entity.Contact;
import contact.service.ContactDao;
import contact.service.mem.MemDaoFactory;
/**
 * Data access object for saving and retrieving contacts,
 * using JPA.
 * To get an instance of this class use:
 * <p>
 * <tt>
 * dao = DaoFactory.getInstance().getContactDao()
 * </tt>
 * 
 * @author jim, Supavit 5510546671
 */
public class JpaContactDao implements ContactDao{

	/** the EntityManager for accessing JPA persistence services. */
	private final EntityManager em;
	
	/**
	 * constructor with injected EntityManager to use.
	 * @param em an EntityManager for accessing JPA services.
	 */
	public JpaContactDao(EntityManager em) {
		this.em = em;
		//createTestContact( );
	}
	
	// createTestContact( ) belongs someplace else.
	
	/**
	 * @see contact.service.ContactDao#find(long)
	 */
	@Override
	public Contact find(long id) {
		return em.find(Contact.class, id);
	}

	/**
	 * Find contacts whose title contains string
	 * @see contact.service.ContactDao#search(java.lang.String)
	 */
	@Override
	public List<Contact> findByTitle(String querystr) {
		// LIKE does string match using patterns.
		Query query = em.createQuery("select c from Contact c where LOWER(c.title) LIKE :title");
		// % is wildcard that matches anything
		query.setParameter("title", "%"+querystr.toLowerCase()+"%");

		return Lists.newArrayList( query.getResultList() );
		
	}

	/**
	 * @see contact.service.ContactDao#findAll()
	 */
	@Override
	public List<Contact> findAll() {
		Query query = em.createQuery("select c from Contact c");
		List list = query.getResultList();
		return list;
	}

	/**
	 * @see contact.service.ContactDao#delete(long)
	 */
	@Override
	public boolean delete(long id) {
		Contact contactObj = find(id);
		if(contactObj == null){
			throw new IllegalArgumentException("Can't delete a null contact");
		}
		EntityTransaction tx = em.getTransaction();
		try{
			tx.begin();
			em.remove(contactObj);
			tx.commit();
			return true;
		} catch(EntityExistsException ex){
			Logger.getLogger(this.getClass().getName()).warning(ex.getMessage());
			if(tx.isActive()){
				try{
					tx.rollback();
				}catch(Exception e){
					Logger.getLogger(this.getClass().getName()).warning(e.getMessage());
				}
			}
		}
		
		return true;
	}

	/**
	 * @see contact.service.ContactDao#save(contact.entity.Contact)
	 */
	@Override
	public boolean save(Contact contact) {
		if (contact == null) throw new IllegalArgumentException("Can't save a null contact");
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(contact);
			tx.commit();
			return true;
		} catch (EntityExistsException ex) {
			Logger.getLogger(this.getClass().getName()).warning(ex.getMessage());
			if (tx.isActive()) try { tx.rollback(); } catch(Exception e) {}
			return false;
		}
	}

	/**
	 * @see contact.service.ContactDao#update(contact.entity.Contact)
	 */
	@Override
	public boolean update(Contact update) {
		if (update == null) throw new IllegalArgumentException("Can't update a null contact");
		EntityTransaction tx = em.getTransaction();
		try {
			Contact contact = find(update.getId());
			em.merge(contact);
			tx.commit();
			return true;
		} catch (EntityExistsException ex) {
			Logger.getLogger(this.getClass().getName()).warning(ex.getMessage());
			if (tx.isActive()) try { tx.rollback(); } catch(Exception e) {}
			return false;
		}
	}

	/**
	 * @see contact.service.ContactDao#removeAll()
	 */
	@Override
	public void removeAll() {
		for(Contact c : findAll()){
			delete(c.getId());
		}

	}

}