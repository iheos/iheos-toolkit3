package gov.nist.toolkit.xdstools3.client.customWidgets.dialogs.loginDialog;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Client side of the GWT RPC mechanism. This is the asynchronous interface.
 * 
 * @author dazais
 * @see gov.nist.toolkit.xdstools3.client.customWidgets.dialogs.loginDialog.LoginService
 *
 */

public interface LoginServiceAsync {

	public void logMeIn(String password, AsyncCallback<Boolean> callback);
}
