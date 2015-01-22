package gov.nist.hit.ds.repository.rpc.search.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.hit.ds.repository.AssetHelper;
import gov.nist.hit.ds.repository.ContentHelper;
import gov.nist.hit.ds.repository.rpc.presentation.PresentationData;
import gov.nist.hit.ds.repository.rpc.search.client.QueryParameters;
import gov.nist.hit.ds.repository.rpc.search.client.RepositoryService;
import gov.nist.hit.ds.repository.rpc.search.client.RepositoryTag;
import gov.nist.hit.ds.repository.rpc.search.client.exception.NoServletSessionException;
import gov.nist.hit.ds.repository.rpc.search.client.exception.RepositoryConfigException;
import gov.nist.hit.ds.repository.shared.SearchCriteria;
import gov.nist.hit.ds.repository.shared.ValidationLevel;
import gov.nist.hit.ds.repository.shared.aggregation.AssertionAggregation;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.shared.id.AssetId;
import gov.nist.hit.ds.repository.shared.id.RepositoryId;
import gov.nist.hit.ds.repository.shared.id.SimpleTypeId;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.toolkit.installation.Installation;

import java.util.List;
import java.util.Map;


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
		return AssetHelper.search(reposData, sc);
	}

    @Override
    public Boolean searchHit(String[][] repos, SearchCriteria sc, Boolean newIndexOnly) throws RepositoryConfigException {
        return PresentationData.searchHit(repos, sc, newIndexOnly);
    }


    @Override
    public AssetNode getAssetNode(String reposSrc, String reposId, String assetId) throws RepositoryConfigException {
        return PresentationData.getAssetNode(reposSrc, reposId, assetId);
    }

    @Override
	public List<AssetNode> getAssetTree(String[][] reposData, int offset)
			throws RepositoryConfigException {
		return PresentationData.getTree(reposData, offset);
	}

	
	@Override
	public AssetNode getAssetTxtContent(AssetNode an)
			throws RepositoryConfigException {
		try {
			return ContentHelper.getContent(an);
		} catch (Exception re) {
			throw new RepositoryConfigException(re.toString());
		}
	}

	@Override
	public List<AssetNode> getImmediateChildren(AssetNode an, int offset)
			throws RepositoryConfigException {
		try {
			return AssetHelper.getImmediateChildren(an,offset);
		} catch (Exception re) {
			throw new RepositoryConfigException(re.toString());
		}
	}

    @Override
    public List<AssetNode> getImmediateChildren(AssetNode an, int offset, boolean stopFlag)
            throws RepositoryConfigException {
        try {
            return AssetHelper.getImmediateChildren(an,offset,stopFlag);
        } catch (Exception re) {
            throw new RepositoryConfigException(re.toString());
        }
    }


    @Override
    public AssetNode getChildren(AssetNode an) throws RepositoryConfigException {
        try {
            return AssetHelper.getChildren(an);
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
    public List<AssetNode> getParentChainInTree(AssetNode an)
            throws RepositoryConfigException {
        try {
            return PresentationData.getParentChainInTree(an);
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
    public Map<String,AssetNode> getTxUpdates(String queue, String filterLocation) throws RepositoryConfigException {
        try {
            return PresentationData.getLiveUpdates(queue,filterLocation);
        } catch (Exception re) {
            throw new RepositoryConfigException(re.toString());
        }
    }

    @Override
    public String getJmsHostAddress() throws RepositoryConfigException {
        try {
            return PresentationData.getJmsHostAddress();
        } catch (Exception re) {
            throw new RepositoryConfigException(re.toString());
        }
    }

    @Override
    public List<String> getValidatorNames() {
        return PresentationData.getValidatorNames();
    }

    @Override
    public Map<String, AssetNode> validateMessage(String validatorName, ValidationLevel validationLevel, AssetNode transaction) throws RepositoryConfigException {
        return PresentationData.validateMessage(validatorName, validationLevel, transaction);
    }

    @Override
    public AssertionAggregation aggregateAssertions(RepositoryId repositoryId, AssetId eventId, SimpleTypeId parentAssetType, SimpleTypeId detailAssetType, SearchCriteria detailAssetFilterCriteria, String[] displayColumns) throws RepositoryConfigException {
        try {
            return AssetHelper.aggregateAssertions(repositoryId,eventId, parentAssetType, detailAssetType, detailAssetFilterCriteria, displayColumns);
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
