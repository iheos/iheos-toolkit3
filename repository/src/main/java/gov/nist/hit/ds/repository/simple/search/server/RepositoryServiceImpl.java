package gov.nist.hit.ds.repository.simple.search.server;

import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.presentation.PresentationData;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import gov.nist.hit.ds.repository.simple.search.client.QueryParameters;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryService;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryTag;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;
import gov.nist.hit.ds.repository.simple.search.client.exception.NoServletSessionException;
import gov.nist.hit.ds.repository.simple.search.client.exception.RepositoryConfigException;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


@SuppressWarnings("serial")
public class RepositoryServiceImpl extends RemoteServiceServlet implements
RepositoryService {


	@Override
	public List<RepositoryTag> getRepositoryDisplayTags()
			throws RepositoryConfigException {
		return new PresentationData().getRepositoryDisplayTags();
	}

	@Override
	public List<String> getIndexablePropertyNames()
			throws NoServletSessionException {
		return PresentationData.getIndexablePropertyNames();
	}

	@Override
	public Boolean setRepositoryConfig() throws RepositoryConfigException {
		
		try {
			
			Installation.installation().initialize();
 				
			Configuration.configuration();
		
			return true;
			
		} catch (Exception e) {			
			throw new RepositoryConfigException(e.toString());
		}
		
	}

	@Override
	public List<AssetNode> search(String[][] reposData, SearchCriteria sc) {
		return PresentationData.search(reposData, sc);
	}

    @Override
    public Boolean searchHit(String[][] repos, SearchCriteria sc, Boolean newIndexOnly) throws RepositoryConfigException {
        return PresentationData.searchHit(repos, sc, newIndexOnly);
    }

    @Override
	public List<AssetNode> getAssetTree(String[][] reposData)
			throws RepositoryConfigException {
		return PresentationData.getTree(reposData);
	}

	
	@Override
	public AssetNode getAssetTxtContent(AssetNode an)
			throws RepositoryConfigException {
		try {
			return PresentationData.getTextContent(an);	
		} catch (Exception re) {
			throw new RepositoryConfigException(re.toString());
		}
		
	}

	@Override
	public List<AssetNode> getImmediateChildren(AssetNode an)
			throws RepositoryConfigException {
		try {
			return PresentationData.getImmediateChildren(an);	
		} catch (Exception re) {
			throw new RepositoryConfigException(re.toString());
		}
	}

	@Override
	public AssetNode getParentChain(AssetNode an)
			throws RepositoryConfigException {
		try {
			return PresentationData.getParentChain(an);	
		} catch (Exception re) {
			throw new RepositoryConfigException(re.toString());
		}
	}

	@Override
	public Boolean isRepositoryConfigured() throws RepositoryConfigException {
		try {
			return PresentationData.isRepositoryConfigured();	
		} catch (Exception re) {
			throw new RepositoryConfigException(re.toString());
		}
	}

	@Override
	public AssetNode saveSearchCriteria(QueryParameters qp)
			throws RepositoryConfigException {
		try {
			return PresentationData.saveSearchCriteria(qp);	
		} catch (Exception re) {
			throw new RepositoryConfigException(re.toString()); 
		}
	}

	@Override
	public List<AssetNode> getSavedQueries(String id, String acs)
			throws RepositoryConfigException {
		try {
			return PresentationData.getSavedQueries(id, acs);	
		} catch (Exception re) {
			throw new RepositoryConfigException(re.toString()); 
		}
	}

    @Override
    public Map<String,AssetNode> getTxUpdates(String queue) throws RepositoryConfigException {
        try {
            return PresentationData.getLiveUpdates(queue);
        } catch (Exception re) {
            throw new RepositoryConfigException(re.toString());
        }
    }

    @Override
	public QueryParameters getSearchCriteria(String id, String acs,
			String queryLoc) throws RepositoryConfigException {
		try {
			return PresentationData.getSearchCriteria(id, acs, queryLoc);	
		} catch (Exception re) {
			throw new RepositoryConfigException(re.toString()); 
		}

	}

	@Override
	public QueryParameters getSearchCriteria(String queryLoc)
			throws RepositoryConfigException {
		try {
			return PresentationData.getSearchCriteria(queryLoc);	
		} catch (Exception re) {
			throw new RepositoryConfigException(re.toString()); 
		}

	}


	



}
