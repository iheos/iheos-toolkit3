package gov.nist.toolkit.xdstools3.client.customWidgets.loginDialog;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Client side of the GWT RPC mechanism. This is the service class.
 * 
 * @author dazais
 * @see LoginServiceAsync
 *
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {

	public void logMeIn(String username, String password);

}
