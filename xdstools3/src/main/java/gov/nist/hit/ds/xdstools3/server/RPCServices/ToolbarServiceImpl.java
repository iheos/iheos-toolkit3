package gov.nist.hit.ds.xdstools3.server.RPCServices;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import gov.nist.hit.ds.xdstools3.server.Caller;
import gov.nist.hit.ds.xdstools3.client.customWidgets.toolbar.ToolbarService;

/**
 * Server side of the GWT RPC mechanism.
 *
 * @author dazais
 * @see gov.nist.hit.ds.xdstools3.client.customWidgets.toolbar.ToolbarService
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

    // HEADER Services
    public String getToolkitEvent(){
        return Caller.getInstance().getToolkitConnectathonEvent();
    }

    public String getToolkitVersion(){
        return Caller.getInstance().getToolkitVersion();
    }
}
