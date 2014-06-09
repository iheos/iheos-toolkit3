package gov.nist.toolkit.xdstools3.client.RPCUtils;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;

/**
 * Created by dazais on 5/16/2014.
 */
public class CustomCallback implements AsyncCallback {
    public void onFailure(Throwable caught) {
        SC.say("Failed to communicate with the server. Please contact the support team.");
    }

    @Override
    public void onSuccess(Object result) {
        // no error message when things work fine
    }


}
