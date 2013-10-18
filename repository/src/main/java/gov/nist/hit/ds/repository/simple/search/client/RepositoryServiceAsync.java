package gov.nist.hit.ds.repository.simple.search.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RepositoryServiceAsync {
	/* Artifact Repository Services */
	void getRepositoryDisplayTags(AsyncCallback<Map<String, String[]>> callback);
	void getIndexablePropertyNames(AsyncCallback<List<String>> callback);
	void setRepositoryConfig(AsyncCallback<Boolean> callback) throws Exception;
	void search(String[][] repos, SearchCriteria sc, AsyncCallback<List<AssetNode>> callback);
	void getAssetTree(String[][] repos, AsyncCallback<List<AssetNode>> callback);
	void getAssetTxtContent(AssetNode an, AsyncCallback<String> callback);

}
