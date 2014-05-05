package gov.nist.toolkit.xdstools3.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Client side of the GWT RPC mechanism. This is the service class.
 * 
 * @author dazais
 * @see InterfaceClientServerAsync
 * @see InterfaceClientServerImpl
 *
 */
@RemoteServiceRelativePath("service")
public interface InterfaceClientServer extends RemoteService {
	public void logMeIn(String username, String password);
	

}
