package gov.nist.toolkit.xdstools3.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import gov.nist.toolkit.xdstools3.client.InterfaceClientServer;

/**
 * Server side of the GWT RPC mechanism. 
 * 
 * @author dazais
 * @see InterfaceClientServer
 *
 */
public class InterfaceClientServerImpl extends RemoteServiceServlet implements InterfaceClientServer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    // Login functions
	public void logMeIn(String username, String password){Caller.getInstance().logMeIn(username, password);}

    public String[] retrieveEnvironments(){  return Caller.getInstance().retrieveEnvironments();}
    public String[] retrieveTestSessions(){  return Caller.getInstance().retrieveTestSessions();}


}
