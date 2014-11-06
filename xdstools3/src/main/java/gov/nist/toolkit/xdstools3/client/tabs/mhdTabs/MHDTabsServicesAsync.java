package gov.nist.toolkit.xdstools3.client.tabs.mhdTabs;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MHDTabsServicesAsync {
    void validateMHDMessage(String messageType, AsyncCallback<String> async);
}
