package gov.nist.toolkit.xdstools3.server.RPCServices;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.toolkit.xdstools3.client.customWidgets.dialogs.loginDialog.LoginService;
import gov.nist.toolkit.xdstools3.server.Caller;

/**
 * Server side of the GWT RPC mechanism. 
 * 
 * @author dazais
 * @see gov.nist.toolkit.xdstools3.client.customWidgets.dialogs.loginDialog.LoginService
 *
 */
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     * Admin login. Passes the password to the backend to be checked.
     * @param password the admin password.
     * @return a boolean that indicates whether the login was successful / unsuccessful.
     */
	public boolean logMeIn(String password){
        return Caller.getInstance().logMeIn(password);}

}

