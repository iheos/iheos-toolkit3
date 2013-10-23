/**
 * 
 */
package gov.nist.hit.ds.repository.simple.search.client;

import gov.nist.hit.ds.repository.simple.search.client.exception.NoServletSessionException;
import gov.nist.hit.ds.repository.simple.search.client.exception.RepositoryConfigException;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author Sunil.Bhaskarla
 *
 */

@RemoteServiceRelativePath("repository")
public interface RepositoryService extends RemoteService  {

	
	/* Artifact Repository Services */
	public Map<String, String[]> getRepositoryDisplayTags() throws NoServletSessionException;
	public List<String> getIndexablePropertyNames() throws NoServletSessionException;
	public Boolean setRepositoryConfig() throws RepositoryConfigException;
	public List<AssetNode> search(String[][] repos, SearchCriteria sc) throws RepositoryConfigException;
	public List<AssetNode> getAssetTree(String[][] repos) throws RepositoryConfigException;
	public AssetNode getAssetTxtContent(AssetNode an) throws RepositoryConfigException;
}
