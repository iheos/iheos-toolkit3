package gov.nist.toolkit.xdstools3.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import gov.nist.toolkit.xdstools3.client.clientServerUtils.TransientArrayList;

import java.util.ArrayList;

/**
 * Client side of the GWT RPC mechanism. This is the service class.
 * 
 * @author dazais
 * @see InterfaceClientServerAsync
 *
 */
@RemoteServiceRelativePath("service")
public interface InterfaceClientServer extends RemoteService {

	public void logMeIn(String username, String password);
    public String[] retrieveEnvironments();
    public String[] retrieveTestSessions();


	

}
