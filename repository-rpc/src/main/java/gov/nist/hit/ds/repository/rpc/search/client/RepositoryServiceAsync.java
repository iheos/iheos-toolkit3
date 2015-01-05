package gov.nist.hit.ds.repository.rpc.search.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import gov.nist.hit.ds.repository.rpc.search.client.exception.RepositoryConfigException;
import gov.nist.hit.ds.repository.shared.SearchCriteria;
import gov.nist.hit.ds.repository.shared.aggregation.AssertionAggregation;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.shared.id.AssetId;
import gov.nist.hit.ds.repository.shared.id.RepositoryId;
import gov.nist.hit.ds.repository.shared.id.SimpleTypeId;

import java.util.List;
import java.util.Map;

public interface RepositoryServiceAsync {
	/* Artifact Repository Services */
	void getRepositoryDisplayTags(AsyncCallback<List<RepositoryTag>> callback);
	void getIndexablePropertyNames(AsyncCallback<List<String>> callback);
	void setRepositoryConfig(AsyncCallback<Boolean> callback) throws Exception;
	void search(String[][] repos, SearchCriteria sc, AsyncCallback<List<AssetNode>> callback);
    void searchHit(String[][] repos, SearchCriteria sc, Boolean newIndexOnly, AsyncCallback<Boolean> callback);
    void getAssetNode(String reposSrc, String reposId, String assetId, AsyncCallback<AssetNode> callback) throws RepositoryConfigException;
    void getAssetTree(String[][] repos, AsyncCallback<List<AssetNode>> callback);
	void getAssetTxtContent(AssetNode an, AsyncCallback<AssetNode> callback);
	void getImmediateChildren(AssetNode an, AsyncCallback<List<AssetNode>> callback) throws RepositoryConfigException;
    void getChildren(AssetNode an, AsyncCallback<AssetNode> callback) throws RepositoryConfigException;
	void getParentChain(AssetNode an, AsyncCallback<AssetNode> callback) throws RepositoryConfigException;
    void getParentChainInTree(AssetNode an, AsyncCallback<List<AssetNode>> callback) throws RepositoryConfigException;
	void isRepositoryConfigured(AsyncCallback<Boolean> callback) throws RepositoryConfigException;
	void getSearchCriteria(String queryLoc, AsyncCallback<QueryParameters> callback) throws RepositoryConfigException;	
	void getSearchCriteria(String reposId, String acs, String queryLoc, AsyncCallback<QueryParameters> callback) throws RepositoryConfigException;
	void saveSearchCriteria(QueryParameters qp, AsyncCallback<AssetNode> callback) throws RepositoryConfigException;
	void getSavedQueries(String id, String acs, AsyncCallback<List<AssetNode>> callback) throws RepositoryConfigException;
    void getTxUpdates(String queue, String filterLocation, AsyncCallback<Map<String,AssetNode>> callback) throws RepositoryConfigException;
    void getJmsHostAddress(AsyncCallback<String> callback) throws RepositoryConfigException;
    void getValidatorNames(AsyncCallback<List<String>> callback);
    void validateMessage(String validatorName, AssetNode transaction, AsyncCallback<Map<String,AssetNode>> callback) throws RepositoryConfigException;
    void aggregateAssertions(RepositoryId repositoryId, AssetId eventId, SimpleTypeId parentAssetType, SimpleTypeId detailAssetType, SearchCriteria detailAssetFilterCriteria, String[] displayColumns , AsyncCallback<AssertionAggregation> callback) throws RepositoryConfigException;
}

