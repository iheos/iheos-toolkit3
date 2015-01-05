/**
 * 
 */
package gov.nist.hit.ds.repository.rpc.search.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import gov.nist.hit.ds.repository.rpc.search.client.exception.NoServletSessionException;
import gov.nist.hit.ds.repository.rpc.search.client.exception.RepositoryConfigException;
import gov.nist.hit.ds.repository.shared.SearchCriteria;
import gov.nist.hit.ds.repository.shared.aggregation.AssertionAggregation;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.shared.id.AssetId;
import gov.nist.hit.ds.repository.shared.id.RepositoryId;
import gov.nist.hit.ds.repository.shared.id.SimpleTypeId;

import java.util.List;
import java.util.Map;

/**
 * @author Sunil.Bhaskarla
 *
 */

@RemoteServiceRelativePath("repository")
public interface RepositoryService extends RemoteService  {

	
	/* Toolkit Artifact Repository Services */
	public List<RepositoryTag> getRepositoryDisplayTags() throws RepositoryConfigException;
	public List<String> getIndexablePropertyNames() throws NoServletSessionException;
	public Boolean setRepositoryConfig() throws RepositoryConfigException;
	public List<AssetNode> search(String[][] repos, SearchCriteria sc) throws RepositoryConfigException;
    public Boolean searchHit(String[][] repos, SearchCriteria sc, Boolean newIndexOnly) throws RepositoryConfigException;
	public List<AssetNode> getAssetTree(String[][] repos) throws RepositoryConfigException;
    public AssetNode getAssetNode(String reposSrc, String reposId, String assetId) throws RepositoryConfigException;
	public AssetNode getAssetTxtContent(AssetNode an) throws RepositoryConfigException;
	public List<AssetNode> getImmediateChildren(AssetNode an) throws RepositoryConfigException;
    public AssetNode getChildren(AssetNode an) throws RepositoryConfigException;
	public AssetNode getParentChain(AssetNode an) throws RepositoryConfigException;
    public List<AssetNode> getParentChainInTree(AssetNode an) throws RepositoryConfigException;
	public Boolean isRepositoryConfigured() throws RepositoryConfigException;
	public QueryParameters getSearchCriteria(String queryLoc) throws RepositoryConfigException;	
	public QueryParameters getSearchCriteria(String id, String acs, String queryLoc) throws RepositoryConfigException;
	public AssetNode saveSearchCriteria(QueryParameters qp) throws RepositoryConfigException;
	public List<AssetNode> getSavedQueries(String id, String acs) throws RepositoryConfigException;
    public Map<String,AssetNode> getTxUpdates(String queue, String filterLocation) throws RepositoryConfigException;
    public String getJmsHostAddress() throws RepositoryConfigException;
    public List<String> getValidatorNames();
    public Map<String,AssetNode> validateMessage(String validatorName, AssetNode transaction) throws RepositoryConfigException;
    public AssertionAggregation aggregateAssertions(RepositoryId repositoryId, AssetId eventId, SimpleTypeId parentAssetType, SimpleTypeId detailAssetType, SearchCriteria detailAssetFilterCriteria, String[] displayColumns) throws RepositoryConfigException;

}
