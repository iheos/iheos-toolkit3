package gov.nist.hit.ds.repository.simple.search.client;

import gov.nist.hit.ds.repository.simple.search.client.exception.RepositoryConfigException;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RepositoryServiceAsync {
	/* Artifact Repository Services */
	void getRepositoryDisplayTags(AsyncCallback<List<RepositoryTag>> callback);
	void getIndexablePropertyNames(AsyncCallback<List<String>> callback);
	void setRepositoryConfig(AsyncCallback<Boolean> callback) throws Exception;
	void search(String[][] repos, SearchCriteria sc, AsyncCallback<List<AssetNode>> callback);
	void getAssetTree(String[][] repos, AsyncCallback<List<AssetNode>> callback);
	void getAssetTxtContent(AssetNode an, AsyncCallback<AssetNode> callback);
	void getImmediateChildren(AssetNode an, AsyncCallback<List<AssetNode>> callback) throws RepositoryConfigException;

}
