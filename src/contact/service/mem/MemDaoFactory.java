package contact.service.mem;

import contact.service.ContactDao;
import contact.service.DaoFactory;

/**
 * Manage instances of Data Access Objects (DAO) used in the app.
 * This enables you to change the implementation of the actual ContactDao
 * without changing the rest of your application.
 * 
 * @author jim
 */
public class MemDaoFactory extends DaoFactory {
	
	/** instance of the entity DAO */
	private ContactDao daoInstance;
	
	public MemDaoFactory() {
		daoInstance = new MemContactDao();
	}
	
	@Override
	public ContactDao getContactDao(){
		return daoInstance;
	}
	
	@Override
	public void shutdown(){
		
	}
	
}