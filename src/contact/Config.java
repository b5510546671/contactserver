package contact;

import java.io.File;

public class Config {
	static String filename = "contacts.xml";
	/**
	 * Create and return the name of a writable file on the local system,
	 * preferrably in the user's home directory.
	 * @return String name of a writable contacts file, with extension .xml
	 */
	public static String getWritableContactsFile() {
		
		String home = System.getProperty("user.home");
		//TODO if home doesn't exist, then try something else.
		if (home == null) throw new RuntimeException("Oops. user.home not defined.");
		String path = home + "/" + filename;
		return path;
		
	}
}
