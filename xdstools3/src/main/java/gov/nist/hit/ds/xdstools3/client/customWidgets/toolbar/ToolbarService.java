package gov.nist.hit.ds.xdstools3.client.customWidgets.toolbar;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Client side of the GWT RPC mechanism. This is the service class.
 *
 * @author dazais
 * @see ToolbarServiceAsync
 *
 */
@RemoteServiceRelativePath("toolbar")
public interface ToolbarService extends RemoteService {

    public String[] retrieveEnvironments();
    public void setEnvironment(String environmentName);
    public String[] retrieveTestSessions();
    public String[] addTestSession(String sessionName);
    public String[] deleteTestSession(String sessionName);

    // header app properties services
    public String getToolkitEvent();
    public String getToolkitVersion();

}