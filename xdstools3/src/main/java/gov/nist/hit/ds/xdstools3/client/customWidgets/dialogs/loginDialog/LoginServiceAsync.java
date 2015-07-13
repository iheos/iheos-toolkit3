package gov.nist.hit.ds.xdstools3.client.customWidgets.dialogs.loginDialog;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Client side of the GWT RPC mechanism. This is the asynchronous interface.
 * 
 * @author dazais
 * @see LoginService
 *
 */

public interface LoginServiceAsync {

	public void logMeIn(String password, AsyncCallback<Boolean> callback);
}
