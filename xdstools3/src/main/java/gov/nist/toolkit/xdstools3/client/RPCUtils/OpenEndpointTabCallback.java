package gov.nist.toolkit.xdstools3.client.RPCUtils;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import gov.nist.toolkit.xdstools3.client.util.eventBus.OpenTabEvent;
import gov.nist.toolkit.xdstools3.client.manager.Manager;

/**
 * Created by dazais on 5/16/2014.
 */
public class OpenEndpointTabCallback implements AsyncCallback {
    public void onFailure(Throwable caught) {
        SC.say("Failed to communicate with the server. Please contact the support team.");
    }

    @Override
    /**
     * Displays the Endpoint Tab if successful
     */
    public void onSuccess(Object result) {
        // Display the Admin Settings Tab if login was successful
        Manager.EVENT_BUS.fireEvent(new OpenTabEvent("ENDPOINTS"));
    }


}
