package gov.nist.toolkit.xdstools3.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Client side of the GWT RPC mechanism. This is the asynchronous interface.
 * 
 * @author dazais
 * @see InterfaceClientServer
 * @see InterfaceClientServerImpl
 *
 */

public interface InterfaceClientServerAsync {
	
	// change "void" to "Request" if a return type is needed
	public void logMeIn(String username, String password, AsyncCallback<Void> callback);
}
