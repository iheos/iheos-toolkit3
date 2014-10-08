package gov.nist.hit.ds.repository.simple.search;

import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.shared.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleAsset;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.shared.SearchCriteria;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SearchResultIterator implements AssetIterator  {

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SearchResultIterator.class.getName());
	private static final long serialVersionUID = -719485351032161998L;
	String[] assetFileNames;
	int assetFileNamesIndex = 0;
	ArtifactId repositoryId = null;
	boolean[] selections = null;
	Type type = null;
	List<AssetNode> crs = null;
	int totalRecords = -1;
	int fetchedRecords = 0;

    private boolean loadProperties = true;
	

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

		init(repositories,searchCriteria,null, false);
	}

    public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria, boolean searchCriteriaLocationOnly, boolean loadProperties) throws RepositoryException {

        setLoadProperties(loadProperties);
        init(repositories,searchCriteria,null, searchCriteriaLocationOnly);
    }
	/**
	 * 
	 * @param repositories
	 * @param searchCriteria
	 * @param orderBy Optional - This can be null
	 * @throws RepositoryException
	 */
	public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria, String[] orderBy) throws RepositoryException {

		init(repositories,searchCriteria,orderBy, false);
	}
	
	public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria, PropertyKey[] orderByKeys) throws RepositoryException {

        String[] orderKeys = null;
		if (orderByKeys!=null) {
            List<String> orderBy = new ArrayList<String>(orderByKeys.length);

            for (PropertyKey pk : orderByKeys) {
                orderBy.add(pk.toString());
            }

            orderKeys = orderBy.toArray(new String[orderBy.size()]);

        }
        init(repositories,searchCriteria,orderKeys, false);

	}
		
	private void init(Repository[] repositories, SearchCriteria searchCriteria,
			String[] orderBy, boolean searchCriteriaLocationOnly) throws RepositoryException {
		DbIndexContainer dbc = new DbIndexContainer();
		
		crs = dbc.getAssetsBySearch(repositories, searchCriteria, orderBy, searchCriteriaLocationOnly);
		if (crs==null)
			totalRecords = 0;
		else
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
			
			if (crs!=null) {
				AssetNode an = crs.get(fetchedRecords++);
				repId = new SimpleId(an.getRepId());				
				String reposSrcAcs = an.getReposSrc();
				
				if (reposSrcAcs!=null) {
					reposSrc = Configuration.getRepositorySrc(RepositorySource.Access.valueOf(reposSrcAcs));
				} else {
					logger.fine("No [null] access indexed for " + repId);
				}
				
				Repository repos = new RepositoryFactory(reposSrc).getRepository(repId);

                Asset a = null;

                if (isLoadProperties()) {
                    logger.info("loading by prop pop method");
                    String propFileStr = an.getLocation();
                    if (propFileStr!=null && !"".equals(propFileStr)) {
                        String fullPath = repos.getRoot() + File.separator + propFileStr;
                        logger.fine("Retrieving by path: " + fullPath);
                        File propFile = new File(fullPath);
                        a = repos.getAssetByPath(propFile);
                        a.setPath(propFile);
                        logger.fine("mimeType:" + a.getMimeType());

                        if (a==null) {
                            logger.warning("Asset prop load by path failed" + propFileStr);
    //					assetId = new SimpleId(crs.getString(2));
    //					if (assetId!=null) {
    //						logger.fine("Retrieving by path: " + assetId.getIdString());
    //						a = repos.getAsset(assetId);
    //					}
                        }

                    }

                } else {
                    logger.info("partial prop direct pop");
                    a = new SimpleAsset(repos.getSource());

                    a.setProperty(PropertyKey.REPOSITORY_ID,an.getRepId());
                    a.setProperty(PropertyKey.ASSET_ID,an.getAssetId());
                    a.setProperty(PropertyKey.DISPLAY_NAME,an.getDisplayName());
                    a.setProperty(PropertyKey.DESCRIPTION,an.getDescription());
                    a.setProperty(PropertyKey.PARENT_ID,an.getParentId());
                    a.setProperty(PropertyKey.MIME_TYPE,an.getMimeType());
                    a.setProperty(PropertyKey.CREATED_DATE,an.getCreatedDate());
                    a.setPath(new File(an.getLocation())); // Relative

                }
				

				return a;

			}			
			// System.out.println(assetId.getIdString());
			
			
		} catch (Exception e) {
			logger.warning(e.toString());
		}
		
		return null;
	
	}

    public boolean isLoadProperties() {
        return loadProperties;
    }

    public void setLoadProperties(boolean loadProperties) {
        this.loadProperties = loadProperties;
    }

}
