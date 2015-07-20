package gov.nist.hit.ds.repository.simple.search;

import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.shared.PropertyKey;
import gov.nist.hit.ds.repository.shared.SearchCriteria;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleAsset;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.index.db.DbIndexContainer;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class SearchResultIterator implements AssetIterator {

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
     * @throws gov.nist.hit.ds.repository.api.RepositoryException
     */
    public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria) throws RepositoryException {
        this(repositories, searchCriteria, false);
    }

    /**
     * This iterator retrieves a comprehensive set of search results including both the indexed and the non-indexed assets.
     * Search results are provided in the same order as specified by the repository parameter, grouped by assetType.
     * Search results are stored in a temporary table called Session.SearchResults, which is automatically cleaned-up when the db session is disconnected.
     * To avoid page overloading with numerous results, we use a paging method to retrieve a reasonable amount of records each time rather than the entire set.
     *
     * @param repositories
     * @param searchCriteria
     * @param reIndex
     * @throws RepositoryException
     */
    public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria, boolean reIndex) throws RepositoryException {

        init(repositories, searchCriteria, null, false,0 ,0, true, reIndex);
    }

    public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria, boolean searchCriteriaLocationOnly, boolean loadProperties) throws RepositoryException {

        setLoadProperties(loadProperties);
        init(repositories, searchCriteria, null, searchCriteriaLocationOnly,0,0, true, true);
    }

    /**
     * @param repositories
     * @param searchCriteria
     * @param orderBy        Optional - This can be null. For example {null} or {"propertyKey1 asc", "propertyKey2 desc"}
     * @throws RepositoryException
     */
    public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria, String[] orderBy) throws RepositoryException {

        init(repositories, searchCriteria, orderBy, false, 0, 0, true, true);
    }

    public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria, String[] orderBy, boolean reIndex) throws RepositoryException {

        init(repositories, searchCriteria, orderBy, false, 0, 0, true, reIndex);
    }

    public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria, String[] orderBy, int offset, int fetchNext) throws RepositoryException {

        init(repositories, searchCriteria, orderBy, false, offset, fetchNext, true, true);
    }

    public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria, PropertyKey[] orderByKeys) throws RepositoryException {

        String[] orderKeys = PropertyKey.getStrings(orderByKeys);

        init(repositories, searchCriteria, orderKeys, false, 0, 0, true, true);

    }

    public SearchResultIterator(Repository[] repositories, SearchCriteria searchCriteria, PropertyKey[] orderByKeys, int offset, int fetchNext, boolean addEllipses) throws RepositoryException {

        String[] orderKeys = PropertyKey.getStrings(orderByKeys);

        init(repositories, searchCriteria, orderKeys, false, offset, fetchNext,addEllipses, true);

    }

    private void init(Repository[] repositories, SearchCriteria searchCriteria,
                      String[] orderBy, boolean searchCriteriaLocationOnly, int offset, int fetchNext, boolean addEllipses, boolean reIndex) throws RepositoryException {
        DbIndexContainer dbc = new DbIndexContainer();

        crs = dbc.getAssetsBySearch(repositories, searchCriteria, orderBy, searchCriteriaLocationOnly, offset, fetchNext, addEllipses, reIndex);
        if (crs == null)
            totalRecords = 0;
        else
            totalRecords = crs.size();
        // System.out.println("total records in buffer: " + totalRecords);


    }


    @Override
    public boolean hasNextAsset() throws RepositoryException {
        if (crs != null) {
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

            if (crs != null) {
                AssetNode an = crs.get(fetchedRecords++);
                String reposSrcAcs = an.getReposSrc();

                if (reposSrcAcs != null) {
                    reposSrc = Configuration.getRepositorySrc(RepositorySource.Access.valueOf(reposSrcAcs));
                } else {
                    logger.fine("No [null] access indexed for " + repId);
                }

                String offsetIndicator = an.getExtendedProps().get("_offset");
                if (offsetIndicator==null) {
                    repId = new SimpleId(an.getRepId());

                    Repository repos = new RepositoryFactory(reposSrc).getRepository(repId);

                    Asset a = null;

                    if (isLoadProperties()) {
                        logger.fine("loading by prop pop method");
                        String propFileStr = an.getRelativePath();
                        if (propFileStr != null && !"".equals(propFileStr)) {
                            String fullPath = repos.getRoot() + File.separator + propFileStr;
                            logger.fine("Retrieving by path: " + fullPath);
                            File propFile = new File(fullPath);
                            a = repos.getAssetByPath(propFile);
                            a.setPath(propFile);

                            logger.fine("mimeType:" + a.getMimeType());

                            if (a == null) {
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
                        a.setAutoFlush(false);

                        a.setProperty(PropertyKey.REPOSITORY_ID, an.getRepId());
                        a.setProperty(PropertyKey.ASSET_ID, an.getAssetId());
                        a.setProperty(PropertyKey.DISPLAY_NAME, an.getDisplayName());
                        a.setProperty(PropertyKey.DESCRIPTION, an.getDescription());
                        a.setProperty(PropertyKey.PARENT_ID, an.getParentId());
                        a.setProperty(PropertyKey.MIME_TYPE, an.getMimeType());
                        a.setProperty(PropertyKey.CREATED_DATE, an.getCreatedDate());
                        a.setPath(new File(an.getRelativePath())); // Relative

                    }
                    return a;

                } else {
                    // Only a placeholder for something to like ellipses
                    Asset a =  new SimpleAsset(reposSrc);
                    a.setAutoFlush(false);

                    a.setProperty(PropertyKey.REPOSITORY_ID, an.getRepId());
                    a.setProperty(PropertyKey.DISPLAY_NAME, an.getDisplayName());
                    a.setProperty("_offset",offsetIndicator);

                    return a;
                }





            }
            // System.out.println(assetId.getIdString());


        } catch (Throwable t) {
            t.printStackTrace();
            logger.warning(t.toString());
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
