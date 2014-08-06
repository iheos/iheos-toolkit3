package gov.nist.hit.ds.repository.simple.index.db;

/**
 * Created by skb1 on 4/16/14.
 */

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 *
 * The worker thread to index a repository on the file system
 *
 */
public class IndexerThread implements Runnable {
    //		private int totalAssetsIndexed = -1;
    private final Map<String, String> unIndexed; // read-only
    private final Map<String, Asset> unIndexedAsset; // read-only
    private Repository repos;
    private String reposId;
    private List<String> aList;
    private static final ConcurrentHashMap<String, String> columnMap = new ConcurrentHashMap<String, String>(); // read-write

    private static Logger logger = Logger.getLogger(IndexerThread.class.getName());
    /**
     *
     * An enumeration of status codes for a given asset in the index container
     *
     */
    static enum IndexStatus {
        NOT_INDEXED,
        INDEXED,
        STALE
    }

    public IndexerThread(Repository repos,List<String> aList, Map<String, String> unIndexed, Map<String, Asset> assetMap) throws RepositoryException {
        this.unIndexed = unIndexed;
        this.unIndexedAsset = assetMap;
        this.repos = repos;
        this.aList = aList;
        this.reposId = repos.getId().getIdString();
    }

    @Override
    public void run() {

        try {
            for (String key : aList) {

                Asset a = unIndexedAsset.get(key);
                File propFile = a.getPropFile();
                String relativePartStr = a.getPropFileRelativePart(); // Make paths relative to repository root
                IndexStatus idxStatus = IndexStatus.NOT_INDEXED;

                logger.fine("Found indexable asset property file: " + relativePartStr);

                String assetId = (a.getId()!=null)?a.getId().getIdString():null;
                if (assetId == null || "".equals(assetId)) {
                    logger.fine("Missing asset Id for " + propFile);
                }

                String typeKeyword = (a.getAssetType()==null?null:a.getAssetType().getKeyword());
                // Properties should already be loaded by getAsset call by the Iterator
                Properties assetProps = a.getProperties();

                String hash = unIndexed.get(key);
                int idxId = -1;

                String props[] = assetProps.stringPropertyNames().toArray(new String[assetProps.size()]);
                ArrayList<String> columnsToExpand = new ArrayList<String>();
                for (String s: props) {
                    String dbCol = DbIndexContainer.getColumnIdentifier(typeKeyword,s);
                    if (!columnMap.contains(dbCol)) {
                        logger.fine("adding to expand list:" +s);

//                        for (String mkey : columnMap.keySet()) {
//                            logger.info(">>>>" + mkey);
//                        }
                        columnsToExpand.add(s);
                    } else {
                        logger.fine("already exists----" + s + " sz:" + columnMap.size());
                    }
                }

                logger.finest("map sz:" + columnMap.size() + " exp sz:" + columnsToExpand.size());

                if (!columnsToExpand.isEmpty()) {
                    String expandedCols[] = DbIndexContainer.expandContainer(columnsToExpand.toArray(new String[columnsToExpand.size()]));
                    if (expandedCols!=null && expandedCols.length>0) {
                        for (String s : expandedCols) {
                            if (s!=null && !columnMap.contains(s))
                                columnMap.put(s,"expanded");
                        }
                    }
                }

                for (String propertyName : assetProps.stringPropertyNames() ) { /* use properties for partial index */
                    try {
                        String propertyValue = a.getProperty(propertyName);
                        logger.fine("prop-" + propertyName + " -- " + propertyValue);
                        if (propertyValue!=null && !"".equals(propertyValue)) {

                            if (IndexStatus.NOT_INDEXED.equals(idxStatus)) {
                                idxId = addIndex(reposId,assetId,typeKeyword,relativePartStr, DbIndexContainer.getColumnIdentifier(typeKeyword,propertyName),propertyValue);
                                idxStatus = IndexStatus.STALE;
                            } else if (idxId!=-1) {
                                DbIndexContainer.updateIndex(idxId, DbIndexContainer.getColumnIdentifier(typeKeyword,propertyName), propertyValue);
                            }

                            //									totalAssetsIndexed++;
                        }
                    } catch (Exception e)  {
                        logger.finest("Ignore if property doesn't exist:" + e.toString());
                    }
                }

                DbIndexContainer.updateIndexKeys(idxId, "hash=?,reposAcs=?,location=?", new String[]{hash, repos.getSource().getAccess().name(), relativePartStr}); // Note the use of unquoted identifier vs. quoted identifiers for asset property references

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.warning(ex.toString());
        }


    }

    private int addIndex(String repositoryId, String assetId, String assetType, String locationStr, String property, String value)
            throws RepositoryException {
        DbContext dbc = new DbContext();
        try {
            dbc.setConnection(DbConnection.getInstance().getConnection());
            return DbIndexContainer.addIndex(dbc, repositoryId, assetId, assetType, locationStr, property, value);
        } catch (RepositoryException ex) {
            logger.warning(ex.toString());
        } finally {
            dbc.close();
        }
        return -1;


    }
}