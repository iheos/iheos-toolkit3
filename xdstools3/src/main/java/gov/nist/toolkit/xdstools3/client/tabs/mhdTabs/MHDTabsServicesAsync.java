package gov.nist.toolkit.xdstools3.client.tabs.mhdTabs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import gov.nist.toolkit.xdstools3.client.exceptions.ToolkitServerError;

/**
 * Asynchronous interface to call the RPC Services for mhd validation.
 */
public interface MHDTabsServicesAsync {
    void validateMHDMessage(String messageType, AsyncCallback<String> async) throws ToolkitServerError;

    void convertMHDToXDS(AsyncCallback<String> convertedDocument);
}
