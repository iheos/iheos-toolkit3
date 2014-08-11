package gov.nist.toolkit.xdstools3.client.tabs.preConnectathonTestsTab;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ToolkitServiceAsync {


//	void getActorNames(AsyncCallback<List<String>> callback);

//	void  getActorTypeNames(AsyncCallback<List<String>> callback);

	void getCollectionNames(String collectionSetName, AsyncCallback<Map<String, String>> callback);
//	void getCollection(String collectionSetName, String collectionName, AsyncCallback<Map<String, String>> callback);
//	void getTestReadme(String test, AsyncCallback<String> callback);
}
