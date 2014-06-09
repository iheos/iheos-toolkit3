package gov.nist.toolkit.xdstools3.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import gov.nist.toolkit.xdstools3.client.clientServerUtils.TransientArrayList;

import java.util.ArrayList;

/**
 * Client side of the GWT RPC mechanism. This is the asynchronous interface.
 * 
 * @author dazais
 * @see InterfaceClientServer
 *
 */

public interface InterfaceClientServerAsync {

	public void logMeIn(String username, String password, AsyncCallback<Void> callback);

    void retrieveEnvironments(AsyncCallback<String[]> async);

    void retrieveTestSessions(AsyncCallback<String[]> async);
}
