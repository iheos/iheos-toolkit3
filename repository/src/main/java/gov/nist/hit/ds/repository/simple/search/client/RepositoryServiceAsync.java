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
	void getParentChain(AssetNode an, AsyncCallback<AssetNode> callback) throws RepositoryConfigException;
	void isRepositoryConfigured(AsyncCallback<Boolean> callback) throws RepositoryConfigException;
	void getSearchCriteria(String queryLoc, AsyncCallback<QueryParameters> callback) throws RepositoryConfigException;	
	void getSearchCriteria(String reposId, String acs, String queryLoc, AsyncCallback<QueryParameters> callback) throws RepositoryConfigException;
	void saveSearchCriteria(QueryParameters qp, AsyncCallback<AssetNode> callback) throws RepositoryConfigException;
	void getSavedQueries(String id, String acs, AsyncCallback<List<AssetNode>> callback) throws RepositoryConfigException;
    void getTxUpdates(String queue, AsyncCallback<List<AssetNode>> callback) throws RepositoryConfigException;
}