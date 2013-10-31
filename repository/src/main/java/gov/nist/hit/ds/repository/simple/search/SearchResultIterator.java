package gov.nist.hit.ds.repository.simple.search;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.index.db.DbContext;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;

import javax.sql.rowset.CachedRowSet;

public class SearchResultIterator implements AssetIterator  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -719485351032161998L;
	String[] assetFileNames;
	int assetFileNamesIndex = 0;
	Id repositoryId = null;
	boolean[] selections = null;
	Type type = null;
	CachedRowSet crs = null;
	int totalRecords = -1;
	int fetchedRecords = 0;
	

	/**
	 * Retrieves a comprehensive search result of all indexed and non-indexed assets. 
	 * Search results are provided in the same order as specified by the repository parameter, grouped by assetType.
	 * Search results are stored in a temporary table called Session.SearchResults, which is automatically cleaned-up when the db session is disconnected.
	 * To avoid page overloading with numerous results, we use a paging method to retrieve a reasonable amount of records each time rather than the entire set.
	 *  
	 * @param repositories
	 * @param searchCriteria
	 * @throws RepositoryException 
	 */
	public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria) throws RepositoryException {

		init(repositories,searchCriteria,"");
	}
	/**
	 * 
	 * @param repositories
	 * @param searchCriteria
	 * @param orderBy
	 * @throws RepositoryException
	 */
	public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria, String orderBy) throws RepositoryException {

		init(repositories,searchCriteria,orderBy);
	}
	
	public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria, PropertyKey key) throws RepositoryException {

		init(repositories,searchCriteria,key.toString());
	}
	
	private void init(Repository[] repositories, SearchCriteria searchCriteria,
			String orderBy) {
		DbIndexContainer dbc = new DbIndexContainer();
		
		crs = dbc.getAssetsBySearch(repositories, searchCriteria, orderBy);
		totalRecords = crs.size();
		// System.out.println("total records in buffer: " + totalRecords);
		
		
	}

		
	@Override
	public boolean hasNextAsset() throws RepositoryException {
		if (crs!=null) {
				return fetchedRecords < totalRecords;
			
		}
		return false;
	
	}

	@Override
	public Asset nextAsset() throws RepositoryException {
		if (!hasNextAsset())
			throw new RepositoryException(RepositoryException.NO_MORE_ITERATOR_ELEMENTS);
		SimpleId repId = null;
		SimpleId assetId = null;
		RepositorySource reposSrc = null;
		
		try {
			
			if (crs.next()) {
				fetchedRecords++;
				repId = new SimpleId(crs.getString(1));
				assetId = new SimpleId(crs.getString(2));
				String reposSrcAcs = crs.getString(3);
				if (reposSrcAcs!=null) {
					reposSrc = Configuration.getRepositorySrc(RepositorySource.Access.valueOf(reposSrcAcs));
				} else {
					DbContext.log("No [null] access indexed for " + repId);
				}				
			}			
			// System.out.println(assetId.getIdString());
			
			Repository repos = new RepositoryFactory(reposSrc).getRepository(repId);
			return repos.getAsset(assetId);
			
		} catch (Exception e) {
			DbContext.log(e.toString());
		}
		
		return null;
	
	}
	

}
