package gov.nist.toolkit.xdstools3.client.tabs.mhdTabs;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous interface to call the RPC Services for mhd validation.
 */
public interface MHDTabsServicesAsync {
    void validateMHDMessage(String messageType, AsyncCallback<String> async);

    void convertMHDToXDS(AsyncCallback<String> convertedDocument);
}
