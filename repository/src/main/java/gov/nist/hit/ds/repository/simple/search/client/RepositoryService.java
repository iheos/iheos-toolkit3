/**
 * 
 */
package gov.nist.hit.ds.repository.simple.search.client;

import gov.nist.hit.ds.repository.simple.search.client.exception.NoServletSessionException;
import gov.nist.hit.ds.repository.simple.search.client.exception.RepositoryConfigException;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

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
	public List<AssetNode> getAssetTree(String[][] repos) throws RepositoryConfigException;
	public AssetNode getAssetTxtContent(AssetNode an) throws RepositoryConfigException;
	public List<AssetNode> getImmediateChildren(AssetNode an) throws RepositoryConfigException;
	public AssetNode getParentChain(AssetNode an) throws RepositoryConfigException;
	public Boolean isRepositoryConfigured() throws RepositoryConfigException;
	public QueryParameters getSearchCriteria(String queryLoc) throws RepositoryConfigException;	
	public QueryParameters getSearchCriteria(String id, String acs, String queryLoc) throws RepositoryConfigException;
	public AssetNode saveSearchCriteria(QueryParameters qp) throws RepositoryConfigException;
	public List<AssetNode> getSavedQueries(String id, String acs) throws RepositoryConfigException;
    public List<AssetNode> getTxUpdates(String queue) throws RepositoryConfigException;
}