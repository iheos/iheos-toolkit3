package gov.nist.toolkit.xdstools3.client.customWidgets.toolbar;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;

/**
 * Client side of the GWT RPC mechanism. This is the service class.
 * 
 * @author dazais
 * @see ToolbarServiceAsync
 *
 */
@RemoteServiceRelativePath("toolbar")
public interface ToolbarService extends RemoteService {

    public ArrayList<String> retrieveEnvironments();
    public ArrayList<String> retrieveTestSessions();


	

}
