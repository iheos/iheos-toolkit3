package gov.nist.toolkit.xdstools3.client.customWidgets.toolbar;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Client side of the GWT RPC mechanism. This is the asynchronous interface.
 * 
 * @author dazais
 * @see ToolbarService
 *
 */

public interface ToolbarServiceAsync {

    void retrieveEnvironments(AsyncCallback<String[]> async);

    void retrieveTestSessions(AsyncCallback<String[]> async);

    void addTestSession(String sessionName, AsyncCallback<String[]> async);
}
