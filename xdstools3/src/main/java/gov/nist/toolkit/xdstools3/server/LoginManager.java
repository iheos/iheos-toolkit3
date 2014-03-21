package gov.nist.toolkit.xdstools3.server;

/**
 * Tracks which users are logged in. For now, it only supports logging in as admin for the purpose of editing the configurations.
 * 
 * This class is a Singleton.
 * 
 * @author dazais
 *
 */
public class LoginManager {
	
	private static LoginManager instance = null;
	private static boolean LOGGED_AS_ADMIN = false; // we need one of those for every user / IP address - how was it implemented in v2?
	
	protected LoginManager(){}
	
	public static LoginManager getInstance(){
		if (instance == null) instance = new LoginManager();
		return instance;
	}
	
	public boolean isLoggedAsAdmin(){
		// if a given user is logged in then
		LOGGED_AS_ADMIN = true;
		return LOGGED_AS_ADMIN;
	}

}
