package gov.nist.hit.ds.repository.simple.search;

import java.io.File;
import java.util.logging.Logger;

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
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;

import javax.sql.rowset.CachedRowSet;

public class SearchResultIterator implements AssetIterator  {

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SearchResultIterator.class.getName());
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
	 * This iterator retrieves a comprehensive set of search results including both the indexed and the non-indexed assets. 
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
			String orderBy) throws RepositoryException {
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
		// SimpleId assetId = null;
		RepositorySource reposSrc = null;
		
		try {
			
			if (crs.next()) {
				fetchedRecords++;
				repId = new SimpleId(crs.getString(1));				
				String reposSrcAcs = crs.getString(3);
				
				if (reposSrcAcs!=null) {
					reposSrc = Configuration.getRepositorySrc(RepositorySource.Access.valueOf(reposSrcAcs));
				} else {
					logger.fine("No [null] access indexed for " + repId);
				}
				
				Repository repos = new RepositoryFactory(reposSrc).getRepository(repId);
				
				String propFileStr = crs.getString(4);
				Asset a = null;
				if (propFileStr!=null && !"".equals(propFileStr)) {
					String fullPath = repos.getRoot() + File.separator + propFileStr;
					logger.fine("Retrieving by path: " + fullPath);
					File propFile = new File(fullPath);
					a = repos.getAssetByPath(propFile);
					a.setPath(propFile);
				} 
				
				if (a==null) {
					logger.warning("Asset prop load by path failed" + propFileStr);
//					assetId = new SimpleId(crs.getString(2));
//					if (assetId!=null) {
//						logger.fine("Retrieving by path: " + assetId.getIdString());
//						a = repos.getAsset(assetId);						
//					}
				} 
				
				return a;

			}			
			// System.out.println(assetId.getIdString());
			
			
		} catch (Exception e) {
			logger.warning(e.toString());
		}
		
		return null;
	
	}
	

}
