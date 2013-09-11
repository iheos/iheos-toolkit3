package gov.nist.hit.ds.repository.presentation;

import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositoryIterator;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.SimpleRepository;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;
import gov.nist.hit.ds.repository.simple.search.SearchResultIterator;
import gov.nist.hit.ds.repository.simple.search.client.Asset;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PresentationData implements IsSerializable, Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4939311135239253727L;

	
	public Map<String, String> getRepositoryDisplayTags() {
		
		
		Map<String, String> m = new HashMap<String,String>();
		
		RepositoryIterator it;
		try {
			
			
			for (Access acs : RepositorySource.Access.values()) {
				it = new RepositoryFactory(Configuration.getRepositorySrc(acs)).getRepositories();

				Repository r =  null;
				while (it.hasNextRepository()) {
					r = it.nextRepository();
					m.put(r.getId().getIdString(), r.getDisplayName());
					
				}
				
			}
			
		} catch (RepositoryException e) {
			return null;
		}
		
		// Apply sorting on display tags here if necessary
		
		return m;
		
	}
	
	public static List<String> getIndexablePropertyNames() {
		List<String> indexProps = new ArrayList<String>(); 
		for (Access acs : RepositorySource.Access.values()) {
			try {
				indexProps.addAll(DbIndexContainer.getIndexableProperties(Configuration.getRepositorySrc(acs)));
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
		return indexProps;
	}
	
	public static List<Asset> search(String[][] repos, SearchCriteria sc) {
		
		ArrayList<Asset> result = new ArrayList<Asset>();
		
		try {
		
		int reposCt = repos.length;
		Repository[] reposList = new Repository[reposCt];
		
		for (int cx=0; cx<reposCt; cx++) {
			try {
				reposList[cx] = new SimpleRepository(new SimpleId(repos[cx][0]));
				reposList[cx].setSource(Configuration.getRepositorySrc(Access.valueOf(repos[cx][1])));
				
			} catch (RepositoryException e) {
				e.printStackTrace();
				; // incorrect or missing data repository config file 
			}
		}

		AssetIterator iter = null;
		
			iter = new SearchResultIterator(reposList, sc );
		
			int recordCt = 0;
			if (iter!=null && recordCt++ < 50) {// hard limit for now
				while (iter.hasNextAsset()) {
					gov.nist.hit.ds.repository.api.Asset aSrc = iter.nextAsset();
					
					Asset aDst = new Asset();
			
					aDst.setRepId(aSrc.getRepository().getIdString());
					aDst.setAssetId(aSrc.getId().getIdString());
					aDst.setDescription(aSrc.getDescription());
					aDst.setDisplayName(aSrc.getDisplayName());
					
					result.add(aDst);
				}
			}

		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
}


