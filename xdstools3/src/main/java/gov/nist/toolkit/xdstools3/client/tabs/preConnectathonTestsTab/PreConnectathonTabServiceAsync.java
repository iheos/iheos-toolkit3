package gov.nist.toolkit.xdstools3.client.tabs.preConnectathonTestsTab;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Map;

public interface PreConnectathonTabServiceAsync {
    void getCollectionNames(String collectionSetName, AsyncCallback<Map<String, String>> callback);
    void getCollection(String collectionSetName, String collectionName, AsyncCallback<Map<String, String>> callback);
    void getTestReadme(String test, AsyncCallback<String> callback);
}
