package gov.nist.toolkit.xdstools3.client.customWidgets.loginDialog;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Client side of the GWT RPC mechanism. This is the asynchronous interface.
 * 
 * @author dazais
 * @see gov.nist.toolkit.xdstools3.client.customWidgets.loginDialog.LoginService
 *
 */

public interface LoginServiceAsync {

	public void logMeIn(String username, String password, AsyncCallback<Boolean> callback);
}
