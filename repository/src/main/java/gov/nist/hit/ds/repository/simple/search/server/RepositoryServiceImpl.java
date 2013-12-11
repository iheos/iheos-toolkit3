package gov.nist.hit.ds.repository.simple.search.server;

import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.presentation.PresentationData;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
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



}
