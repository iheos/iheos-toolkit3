package gov.nist.toolkit.xdstools3.client.customWidgets.toolbar;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * Client side of the GWT RPC mechanism. This is the asynchronous interface.
 * 
 * @author dazais
 * @see ToolbarService
 *
 */

public interface ToolbarServiceAsync {

    void retrieveEnvironments(AsyncCallback<ArrayList<String>> async);

    void retrieveTestSessions(AsyncCallback<ArrayList<String>> async);
}
