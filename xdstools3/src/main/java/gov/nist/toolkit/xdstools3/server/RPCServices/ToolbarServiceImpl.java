package gov.nist.toolkit.xdstools3.server.RPCServices;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import gov.nist.toolkit.xdstools3.client.customWidgets.toolbar.ToolbarService;
import gov.nist.toolkit.xdstools3.server.Caller;

/**
 * Server side of the GWT RPC mechanism. 
 * 
 * @author dazais
 * @see gov.nist.toolkit.xdstools3.client.customWidgets.toolbar.ToolbarService
 *
 */
public class ToolbarServiceImpl extends RemoteServiceServlet implements ToolbarService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public String[] retrieveEnvironments(){  return Caller.getInstance().retrieveEnvironments();}
    public void setEnvironment(String envName) { Caller.getInstance().setEnvironment(envName); }
    public String[] retrieveTestSessions(){  return Caller.getInstance().retrieveTestSessions();}
    public String[] addTestSession(String sessionName) {
        Caller.getInstance().addTestSession(sessionName); return Caller.getInstance().retrieveTestSessions();}
    public String[] deleteTestSession(String sessionName) {
        Caller.getInstance().addTestSession(sessionName); return Caller.getInstance().retrieveTestSessions();}
}
