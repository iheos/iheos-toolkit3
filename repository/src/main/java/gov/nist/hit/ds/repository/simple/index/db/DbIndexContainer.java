package gov.nist.hit.ds.repository.simple.index.db;


import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.api.TypeIterator;
import gov.nist.hit.ds.repository.simple.IdFactory;
import gov.nist.hit.ds.repository.simple.SimpleAssetIterator;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.repository.simple.SimpleTypeIterator;
import gov.nist.hit.ds.repository.simple.index.Index;
import gov.nist.hit.ds.repository.simple.index.IndexContainer;
import gov.nist.hit.ds.repository.simple.search.client.PnIdentifier;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;
import gov.nist.hit.ds.utilities.io.Hash;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

/**
 * 
 * @author Sunil.Bhaskarla
 *
 */
public class DbIndexContainer implements IndexContainer, Index {

	private static enum IndexStatus {
		NOT_INDEXED,
		INDEXED,
		STALE
	};	
	private static Logger logger = Logger.getLogger(DbIndexContainer.class.getName());
	private static final String repContainerLabel = "repositoryIndex";		
	private static final String assetId = PnIdentifier.getQuotedIdentifer(PropertyKey.ASSET_ID);
	private static final String locationId = "location";
	private static final String assetType = PnIdentifier.getQuotedIdentifer(PropertyKey.ASSET_TYPE);
	private static final String repId = PnIdentifier.getQuotedIdentifer(PropertyKey.REPOSITORY_ID);
	private static final String displayOrder = PnIdentifier.getQuotedIdentifer(PropertyKey.DISPLAY_ORDER); 
	private static final String parentId = PnIdentifier.getQuotedIdentifer(PropertyKey.PARENT_ID);
	private static final String createdDate =  PnIdentifier.getQuotedIdentifer(PropertyKey.CREATED_DATE);
	

	private static final String CACHED_SESSION = "CACHED";
	private static final String CONTAINER_VERSION = "2013-12-18";
	private static final String CONTAINER_VERSION_ID = "VERSION";	
	private static final String BYPASS_VERSION = "BYPASS"; // an indicator to bypass container version and resuse existing container
	
	/* Use an upgrade script to update existing tables in case a newer version of TTT (new ArtRep API) runs against an older copy of the repositoryIndex table in the database */
	private static final String repContainerDefinition = 
	"(repositoryIndexId integer not null  generated always as identity," 	/* (Internal use) This is the primary key */
	+ repId + " varchar(64) not null,"								/* This is the repository Id as it appears on the filesystem */
	+ assetId + " varchar(64)," /* */								/* This is the asset Id of the asset under the repository folder */
	+ locationId + " varchar(512),"									/* This is the file path */
	+ parentId +  " varchar(64),"									/* The parent asset Id. A null-value indicates top-level asset and no children */
	+ assetType + " varchar(32)," 									/* Asset type - usually same as the keyword property */ 
	+"hash varchar(64),"											/* (Internal use) The hash of the property file */
	+"reposAcs varchar(40),"										/* (Internal use) src enum string */	
	+ displayOrder + " int,"         						    	/* This is a reserved keyword for sorting purpose */
	+"indexSession varchar(64))";									/* (Internal use) Stores the indexer repository session id -- later used for removal of stale assets */				
			
	/**
	 * This method uses two main logical data sets to identify assets that are to be indexed, refreshed, or marked as stale.  
	 * Set A is the master reference of a repository as provided by its repository source. The SimpleAssetIterator provides an iterator for the set on the filesystem. 
	 * Set B is the indexed version of set A as it appears in the database. The repositoryIndex database table contains records by the repositoryId, source, and the location.
	 * The following sets are the outcome of operations between the two main sets:
	 * Set C is the union of both sets that identifies set of assets that exist both on the filesystem and the database. The hash value of both asset instances are compared to ensure byte-level consistency. 
	 * Set D is the difference between set A - B. These are the new assets that belong to the unindexed asset category.
	 * Set E is the difference between set B - A. These are the stale assets, a byproduct of assets that were once indexed but later removed from the filesystem. These assets will be removed from the database.
	 *     
	 * @param repos
	 * @param iter
	 * @return
	 * @throws RepositoryException
	 */
	private int indexRepository(Repository repos, SimpleAssetIterator iter) throws RepositoryException {
		int totalAssetsIndexed = 0;
		String reposId = repos.getId().getIdString();
		boolean repositorySynced = false;
		Map<String, String> unIndexed = new HashMap<String,String>();
		Map<String, Asset> unIndexedAsset = new HashMap<String,Asset>();

		List<String> aList = new ArrayList<String>();
		
		if (iter!=null && iter.hasNextAsset()) {
			
			Map<String, Asset> assetMap = new HashMap<String,Asset>();
			
			// Db Index
			try {

				Map<String, String> reposFsIndex = new HashMap<String,String>();
				String fullIndex = getFsHash(repos, iter, assetMap, reposFsIndex);								
				
				String[] reposData = getReposHash(repos);
				if (fullIndex!=null && !"".equals(fullIndex)) {
					if (reposData != null) {
						if (fullIndex.equals(reposData[1])) {
							repositorySynced = true; 	
						} else {
							repositorySynced = false; 
							setReposHash(repos, fullIndex, true);							
						}
					} else {
						repositorySynced = false; 
						setReposHash(repos, fullIndex, false);
					}					
				}
								
				if (!repositorySynced) {
					
					Map<String, String> reposDbIndex = getIndexedHash(repos);
					
					if (reposDbIndex.size()==0) {
						repositorySynced = false;
						unIndexed = reposFsIndex;
						unIndexedAsset.putAll(assetMap);
						aList.addAll(unIndexed.keySet());
						totalAssetsIndexed = reposFsIndex.size();
					} else {
						for (String key : reposFsIndex.keySet()) {
							if ((reposDbIndex.get(key)==null) || !reposDbIndex.get(key).equals(reposFsIndex.get(key))) {							
								unIndexed.put(key, reposFsIndex.get(key));
								unIndexedAsset.put(key, assetMap.get(key));
								aList.add(key);					
								totalAssetsIndexed++;
							} 
						}					
						
						cleanupStaleItems(repos, reposFsIndex, reposDbIndex);
						
						if (unIndexed.size()>0) {
							repositorySynced = false;
						} else {
							repositorySynced = true;
						}

					} 
			
				}
				
			} catch (Exception ex) {
				logger.warning(ex.toString());
			} 			
		

		}		

		if (repositorySynced) {
			logger.fine("Repos Id <" + reposId + "> is already synced.");
		} else {
			ExecutorService svc = Executors.newCachedThreadPool();
			
			final int batchSz = 27;
			int sz = aList.size();
			if (sz >batchSz) {
				int start = 0;
				int end = batchSz;

				int cx = 0;
				while (end <= sz) {
					logger.fine("running indexer thread " + cx++);
					svc.execute(new Thread(new IndexerThread(repos, aList.subList(start, end), unIndexed, unIndexedAsset)));					
					start += batchSz;
					end +=batchSz;
				}
				
				if (end>sz) {
					svc.execute(new Thread(new IndexerThread(repos, aList.subList(start, sz), unIndexed, unIndexedAsset)));
				}
					
					
			} else {
				svc.execute(new Thread(new IndexerThread(repos, aList, unIndexed, unIndexedAsset)));
			}
			
			svc.shutdown();
			boolean svcStatus = false;
			try {
				svcStatus = svc.awaitTermination(15,TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				logger.warning("svc termination failed!");
			} 
			
			logger.info("indexer svc exit status: " + svcStatus);
		}
		
		return totalAssetsIndexed;

	}
	
	@Override
	public String getIndexContainerDefinition() {
		String repContainerHead = 
		"create table " + repContainerLabel;				/* This is the master container for all indexable asset properties */
	
		logger.fine("using label " + repContainerLabel);
		
		return repContainerHead + repContainerDefinition;
		
	}
	
	public int getIndexCount() throws RepositoryException {
		
		try {
			String sqlStr = "select count(*)ct from "+repContainerLabel;
		
			return getQuickCount(sqlStr);
		} catch (RepositoryException e) {
			throw new RepositoryException("count error" , e);
		}
		
	}
	
	/**
	 * Provide a SQL String with ONE count column labeled as "ct"
	 * Returns an integer with the actual count
	 * @param sqlStr
	 * @return
	 * @throws RepositoryException
	 */
	private int getQuickCount(String sqlStr) throws RepositoryException {	
		DbContext dbc = new DbContext();
		dbc.setConnection(DbConnection.getInstance().getConnection());
		
		int ct = dbc.getInt(sqlStr);
		dbc.close();
		
		return ct;
		
	}

	@Override
	public boolean doesIndexContainerExist() throws RepositoryException {
		DbContext dbc = new DbContext();
		dbc.setConnection(DbConnection.getInstance().getConnection());

		String sqlStr = "select hash from " + repContainerLabel 
				+" where " + repId + "=? ";
		
		ResultSet rs = null;
		try {
			
			rs = dbc.executeQuery(sqlStr, new String[]{CONTAINER_VERSION_ID});

			if (rs.next()) { // exists
				String versionStr = rs.getString(1);
				rs.close();
				
				if (BYPASS_VERSION.equals(versionStr)) {
					return true;
				}
				
				if (!CONTAINER_VERSION.equals(versionStr)) { // is it up to date with the software being used?
					logger.fine("Container definition is obsolete <"+ versionStr +">. Removing old container...");
					removeIndexContainer();
					return false;
				} else
					return true;
				
			} else {
				int recordCt = getIndexCount();
				logger.fine("Number of indexed items in obsolete container: " + recordCt);
				logger.fine("Removing old container...");

				removeIndexContainer();
				return false;
			}
				
		} catch (java.sql.SQLSyntaxErrorException nfex) {

			if ("42X05".equals(nfex.getSQLState()) && 30000 == nfex.getErrorCode()) {
				// NFEx expected under some circumstances
				logger.fine("Table does not yet exist ");
			} else {
				logger.warning("NFEx?" + nfex.getSQLState() + " cd: " +  nfex.getErrorCode() + " " + nfex.getMessage());
			}
				 
		} catch (Exception ex) {
			logger.warning(ex.toString());
		} finally {
			dbc.close();
		}
		return false;
		
	}
	


	@Override
	public void createIndexContainer() throws RepositoryException {
		DbContext dbc = new DbContext();
			try {

				dbc.setConnection(DbConnection.getInstance().getConnection());
				dbc.internalCmd(getIndexContainerDefinition());
				dbc.internalCmd("insert into " + repContainerLabel + "("+ repId +",hash) values('" + CONTAINER_VERSION_ID +"','"+ CONTAINER_VERSION + "')");
				
				try {
					
					String index = "create unique index \"repAssetUniqueIdx" + repContainerDefinition.hashCode() + "\" on " + repContainerLabel + " (repositoryIndexId,indexSession)";  // ,"+ repId +"," +  assetId +" These may not be unique anymore with multiple rep sources 
					dbc.internalCmd(index);					
					index = "create index \"repAssetIdx" + repContainerDefinition.hashCode() + "\" on " + repContainerLabel + " ("+ repId +"," +  assetId +"," + assetType +",reposAcs,hash,"+ locationId + "," + parentId + ")";
					dbc.internalCmd(index);
					
				} catch (SQLException e) {
					logger.fine("index probably exists.");
				}

				logger.info("New index container created.");
								
			} catch (SQLException e) {
				logger.warning(e.toString());
				throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
			} finally {
				dbc.close();
			}
		
	}
	
	

	@Override
	public void removeIndexContainer() throws RepositoryException {

		DbContext dbc = new DbContext();
		try {
			dbc.setConnection(DbConnection.getInstance().getConnection());
			dbc.internalCmd("drop table "+repContainerLabel);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
		} finally {
			dbc.close();
		}
	}
	

	@Override
	public int addIndex(String repositoryId, String assetId, String assetType, String locationStr, String property, String value)
			throws RepositoryException {
		DbContext dbc = new DbContext();
		try {				
			dbc.setConnection(DbConnection.getInstance().getConnection());
			return addIndex(dbc, repositoryId, assetId, assetType, locationStr, property, value);
		} catch (RepositoryException ex) {
			logger.warning(ex.toString());			
		} finally {
			dbc.close();
		}
		return -1;
		
		
	}

	public int addIndex(DbContext dbc, String repositoryId, String assetId, String assetType, String locationStr, String property, String value)
			throws RepositoryException {
		int[] rsData = null;
		
		try {
		
			String sqlStr = "insert into "+ repContainerLabel + "("+ repId +"," + DbIndexContainer.assetId + ","+ DbIndexContainer.assetType + "," + locationId;
			if (DbIndexContainer.assetId.equals(property)
				|| DbIndexContainer.repId.equals(property)
				|| DbIndexContainer.assetType.equals(property)) {
				sqlStr += ") values(?,?,?,?)";
				 rsData = dbc.executePreparedId(sqlStr, new String[]{repositoryId,assetId,assetType, locationStr});
			} else {
				sqlStr += "," +  property + ") values(?,?,?,?,?)";
				rsData = dbc.executePreparedId(sqlStr, new String[]{repositoryId,assetId, assetType, locationStr, value});
			}

			logger.fine("rows affected:" + rsData[0]);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
		} 
				
		return rsData[1];
		
	}
	
	
	/**
	 * 
	 * @param repositoryId
	 * @param assetId
	 * @param assetType
	 * @param property
	 * @param value
	 * @throws RepositoryException
	 */
	public void updateIndexOld(Repository repos, String assetId, String assetType, String locationStr, String propCol, String value)
			throws RepositoryException {
		DbContext dbc = new DbContext();
		try {				
			String reposId = repos.getId().getIdString();
			dbc.setConnection(DbConnection.getInstance().getConnection());
			
				
				// Value based
				/* sqlStr = "update "+ repContainerLabel + " set "+propCol+"=? where " + DbIndexContainer.assetId + "=? and " + repId   
						+"= ? and " + DbIndexContainer.assetType +  " = ?  and ("+propCol+" is null or "+propCol+" != ?)";
				*/
				
				// Path based
				String sqlStr = "update "+ repContainerLabel + " set "+propCol+"=? where "
						+ DbIndexContainer.repId + "=? and reposAcs=? and " + locationId   
						+"= ? and ("+propCol+" is null or "+propCol+" != ?)";
				
				int[] rsData = dbc.executePreparedId(sqlStr, new String[]{value, reposId, repos.getSource().getAccess().name(), locationStr, value });				
				logger.fine("rows affected: " + rsData[0]);

			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
		} finally {
			dbc.close();
		}
		
	}

	public void updateIndex(DbContext dbc, int idKey, String propCol, String value)
			throws RepositoryException {
				
		try {													
				// Path based
				String sqlStr = "update "+ repContainerLabel + " set "+propCol+"=? where repositoryIndexId="+idKey						   
						+" and ("+propCol+" is null or "+propCol+" != ?)";
				
				int[] rsData = dbc.executePreparedId(sqlStr, new String[]{value, value});				
				logger.fine("rows affected: " + rsData[0]);


		} catch (SQLException e) {
			e.printStackTrace();
			throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
		} 
		
	}

	public void updateIndexKeys(DbContext dbc, int idKey, String setFragment, String[] val)
			throws RepositoryException {
		
		try {				
						
				// Path based
				String sqlStr = "update "+ repContainerLabel + " set " + setFragment + " where repositoryIndexId=" + idKey;
				
				int[] rsData = dbc.executePreparedId(sqlStr, val);				
				logger.fine("rows affected: " + rsData[0]);


		} catch (SQLException e) {
			e.printStackTrace();
			throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
		} 
		
	}

	
	@Override
	public void removeIndex(String reposId, String sessionId) throws RepositoryException {
		if (reposId!=null && !"".equals(reposId)) {
			DbContext dbc = new DbContext();			
			try {

				dbc.setConnection(DbConnection.getInstance().getConnection());
	 
				String sqlStr = "delete from "+ repContainerLabel + " where " + repId + " = ? and indexSession != ?";
				int rsData = dbc.executePrepared(sqlStr, new String[]{reposId,sessionId});
				logger.fine("Number of stale items removed: " + rsData);

			} catch (SQLException e) {
				e.printStackTrace();
				throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
			} finally {
				dbc.close();				
			}
		}
		
	}
	
	/**
	 * This method deletes all indexes from the repository index container residing in the database.
	 * The repository on the filesystem is NOT modified.
	 * @throws RepositoryException
	 */
	public void purge() throws RepositoryException {
		logger.info("Purging index...");
		DbContext dbc = new DbContext();
		try {

			dbc.setConnection(DbConnection.getInstance().getConnection());
			
			// The TRUNCATE table feature is not implemented Derby 10.4 
			String sqlStr = "delete from "+ repContainerLabel;
			dbc.internalCmd(sqlStr);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
		} finally {
			dbc.close();
		}
		
	}
	
	public void expandContainer(String[] column, Map<String,String> columnMap) throws RepositoryException {
		DbContext dbc = new DbContext();
		try {
			dbc.setConnection(DbConnection.getInstance().getConnection());
			
			expandContainer(dbc, column, columnMap);				
		} catch (Exception ex) {
			logger.warning(ex.toString());
		} finally {
			dbc.close();
		}
	}
	
	
	/**
	 * This method will extend the container to allow for new indexable properties.
	 */
	// 
	public void expandContainer(DbContext dbc, String[] column, Map<String,String> columnMap) throws RepositoryException {

				
			if (column!=null) {
				String sqlStr = "";
				String index = "";

				try {
									
					int cx=0; 
					for (String c : column) {
						String dbCol = getDbIndexedColumn(assetType, c);
						
						if (!columnMap.containsKey(dbCol)) {
							
							if (!isIndexed(dbCol)) {
								columnMap.put(dbCol, null);
								if (cx++>0) index+=",";
								index += dbCol;
		
								// Can only add one at a time
								sqlStr = "alter table "+ repContainerLabel + " add column " + dbCol + " varchar(128)";					
								dbc.internalCmd(sqlStr);								
							} else {
								columnMap.put(dbCol, null);
							}
							
						} else {
							logger.fine("Column "+ c +" already exists " + ((assetType!=null)?"for assetType: "+assetType:""));
						}
						
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
				} 
			}
			
//			try {
//				if (sqlStr!="") {
//					index = "create index \"repAssetIdxp" + sqlStr.hashCode() + "\" on " + repContainerLabel + " ("+ index +")";
//					dbc.internalCmd(index);					
//				}
//			} catch (SQLException e) {
//				logger.fine("Index probably exists.");
//			}
			
		
		
	}
	

	/**
	 * Returns whether a given property for the assetType has a container column associated in the index database.
	 * @param assetType
	 * @param property
	 * @return
	 * @throws RepositoryException
	 */
	public boolean isIndexed(String dbCol) throws RepositoryException {
		int records=0;
		DbContext dbc = new DbContext();		
		try {
			
			if (!PnIdentifier.uniquePropertyColumn) { // Take care of quoted identifiers
				dbCol = dbCol.replace("\"", "");
			}
			
			if (repContainerDefinition.contains(dbCol)) { // Quick check in definition
				return true;
			}
			
			dbc.setConnection(DbConnection.getInstance().getConnection());
			String sqlStr = "select count(c.columnname)ct from sys.syscolumns c, sys.systables t "
							+"where c.referenceid=t.tableid and t.tabletype='T' and lower(t.tablename)=lower('" + repContainerLabel 
							+ "') and lower(c.columnname) = lower('" + dbCol + "')";
			ResultSet rs = dbc.executeQuery(sqlStr);
			
			if (rs!=null) {
				while (rs.next()) {
			          records = rs.getInt("ct");
			          logger.fine("records: " + records);

				}
				rs.close();
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			throw new RepositoryException(RepositoryException.INDEX_ERROR, e);			
		} finally {
			dbc.close();
		}
		
		if (records==0) 
			return false;
		else
			return true;
	}
	
	public String getColumn(String assetType, String property) throws RepositoryException {
		if (!PnIdentifier.uniquePropertyColumn) {
			return property.replace("\"", "");
		} else 
			return getDbIndexedColumn(assetType, property);
	}

	/**
	 * @param assetType
	 * @param property
	 * @return
	 * @throws RepositoryException
	 */
	
	public static String getDbIndexedColumn(String assetType, String property)
			throws RepositoryException {		
		if (!PnIdentifier.uniquePropertyColumn) {			
			return PnIdentifier.getQuotedIdentifer(property);
		} else {			
			String dbCol = null;		
			
			if (assetType!=null) {
				dbCol = assetType+"_"+property.trim();
			} else if (property.indexOf('_')>-1) {
				dbCol = property.trim();
			} else {
				throw new RepositoryException(RepositoryException.INDEX_ERROR, new Exception("Invalid property or assetType"));
			}
			return dbCol;
		}
	}
	

	/**
	 * 
	 * @param columnName
	 * @return
	
	private String getDbColumnSuffix(String columnName) {
		if (!PnIdentifier.uniquePropertyColumn) {
			return columnName; 
		} else {
			if (columnName!=null && columnName.indexOf('_')>-1) {
				String[] assetProp = columnName.split("_");
				if (assetProp.length>1) {
					return assetProp[1];
				}
			} 
			return null;
			
		}
	}
	*/ 
	
	/**
	 * 
	 * @return
	*/ 
	static public ArrayList<String> getIndexableAssets(RepositorySource rs) {
		return getIndexableProperties(rs,true,false);
	}
	
	
	/**
	 * Returns full unique column headers including prefix and suffix, ex. "assetTyp_property" 
	 * @return
	*/ 
	static public ArrayList<String> getIndexableDbProperties(RepositorySource rs) {
		return getIndexableProperties(rs,false,false);		
	}
	
	
	/**
	 * Returns quoted identifiers if uniquePropertyColumn is not specified, and suffixes only 
	 * @return
	 */
	static public ArrayList<String> getIndexableProperties(RepositorySource rs) {
		return getIndexableProperties(rs,false,true);		
	}
	
	/**
	 * ToDo: this needs to be cached
	 * Returns false to indicate indexing feature is not available
	 */
	static public boolean isRepositoryIndexable(RepositorySource rs, Type rep) {
		
		if (rep==null)
			return false;
		
		TypeIterator it;
		try {
			it = new SimpleTypeIterator(rs, rep);
			while (it.hasNextType()) {				
				Type t = it.nextType();
				
				if (t.getDomain()!=null && t.getDomain().equals(SimpleType.REPOSITORY)) {
					if (rep.getKeyword()!=null && rep.getKeyword().equals(t.getKeyword())) {
						return "on".equalsIgnoreCase(t.getIndex());					
					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		logger.fine("returning false");
		return false;
		
	}
	
	
	/**
	 * 
	 * Note: The repository Configuration must already be setup prior to this call.
	 * 
	 * 
	 * @return
	 */
	static private ArrayList<String> getIndexableProperties(RepositorySource rs, boolean assetOnly, boolean suffixOnly) {
		TypeIterator it;
		ArrayList<String> indexableAssetProperties = null;
		try {
			indexableAssetProperties = new ArrayList<String>();
			
			it = new SimpleTypeIterator(rs);
			while (it.hasNextType()) {
				Type t = it.nextType();
				if (t.getDomain()!=null && t.getDomain().equals(SimpleType.ASSET)) {
				 
				if (t.getIndex()!=null) {
					
					String[] indexableProperties = t.getIndex().split(",");
					if (indexableProperties!=null && indexableProperties.length>0) {
						for (String s : indexableProperties) {
							
							String iap = "";
							
							if (!assetOnly) {
								if (!suffixOnly) {
									iap = t.getKeyword() + "_";
								}
								String assetProperty = s.trim();
								int propertyDescriptor = s.indexOf('(');
								if (propertyDescriptor > -1) {
									iap +=assetProperty.substring(0, propertyDescriptor-1);
								} else
									iap += assetProperty.trim();
								
								if (!indexableAssetProperties.contains(iap)) {
									indexableAssetProperties.add(iap);			
								}
								 
							} else {
								if (!indexableAssetProperties.contains(t.getKeyword())) {
									iap = t.getKeyword();
									indexableAssetProperties.add(iap);
								}
							}


						}
					}
					
				}
				
				}
			}
		} catch (RepositoryException e) {
			logger.fine(e.toString());
		}
		return indexableAssetProperties;
	}
	
	/**
	 * This method reindexes all indexable asset properties within all repositories.
	 
	public void reIndex() throws Exception {
		int totalAssetsIndexed = 0;
		int totalRepositoriesInvolved = 0;
		
		try {
						
			ArrayList<String> assets = getIndexableAssets();
			ArrayList<String> properties = getIndexableProperties(); 
		
			for (String assetType : assets) {
				DbContext.log("found indexable asset type: " + assetType);
				SimpleRepositoryIterator it = new SimpleRepositoryIterator();

				while (it.hasNextRepository()) {
					Repository repos = it.nextRepository();					
					
					totalAssetsIndexed += indexRep(repos, properties, assetType );
				totalRepositoriesInvolved++;
			  }				
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		
		DbContext.log("Full Index Summary\n"
						+  "==================\n"
						+  "Total Assets Indexed: " + totalAssetsIndexed + "\nRepositories: " + totalRepositoriesInvolved + "\nIndexes up to date.");
	}
	*/


	/**
	 * @param totalAssetsIndexed
	 * @param properties
	 * @param assetType
	 * @param repos
	 * @return
	 * @throws RepositoryException
	 */
	public int indexRep(Repository repos) throws RepositoryException {
		SimpleAssetIterator iter = null;
		
		if (repos==null || repos.getId()==null) {
			logger.warning("Null repository or repository id");
			return -1; 
		}		
		
		if (!doesIndexContainerExist()) {
			createIndexContainer();
			logger.fine("New index container created.");
		}
		
		String reposId = repos.getId().getIdString();		
		
		// Make this repos is not being actively queued for indexing by another instance
		
		if (isReposQueuedForIndexing(repos)) {
			logger.warning("Repos <" + reposId + "> is already queued for indexing.");
			return -1;
		} else {
			queueReposForIndex(repos,true);
		}
		
		
		iter = new SimpleAssetIterator(repos);
		 		
		if (!iter.hasNextAsset()) {
			logger.fine("Nothing to index in " + reposId);
			return -1;
		}		
		
		int totalAssetsIndexed = -1;
//		if (iter.getSize()<=maxIndexFast) {
			totalAssetsIndexed = indexRepository(repos, iter);
//		} else {
//			totalAssetsIndexed = indexRepLong(repos,iter);
//		}
		
		queueReposForIndex(repos,false);
		return totalAssetsIndexed;
	}
	
	
	private String getFsHash(Repository repos, SimpleAssetIterator iter, Map<String, Asset> assetMap, Map<String, String> reposFsIndex) throws RepositoryException {
			 
			try {
				String reposHash = "";
				
				while (iter.hasNextAsset()) {
					Asset a = iter.nextAsset();
					Properties assetProps = a.getProperties();
					assetMap.put(a.getPropFileRelativePart(), a);
					 
					
					try {
						String hash = getHash(assetProps.toString().getBytes());
						reposFsIndex.put(a.getPropFileRelativePart()
								,hash);
						reposHash += hash;
					} catch (Exception ex) {
						logger.warning("Quick hash calc failed: " + ex.toString());					
					}			
				}
				
				return getHash(reposHash.getBytes());
				
			} catch (Exception ex) {
				logger.warning(ex.toString());
			}
			
			return null;
	}

	
	private class IndexerThread implements Runnable {
//		private int totalAssetsIndexed = -1;
		private Map<String, String> unIndexed; // read-only
		private Map<String, Asset> unIndexedAsset; // read-only
		private Repository repos;
		private String reposId;
		private List<String> aList;
		
		public IndexerThread(Repository repos,List<String> aList, Map<String, String> unIndexed, Map<String, Asset> assetMap) throws RepositoryException {
			this.unIndexed = unIndexed;
			this.unIndexedAsset = assetMap;
			this.repos = repos;
			this.aList = aList;
			this.reposId = repos.getId().getIdString();
			
		}
		
		@Override
		public void run() {
			Map<String,String> columnMap = new HashMap<String,String>();
			DbContext dbc = new DbContext();
			dbc.setConnection(DbConnection.getInstance().getConnection());

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
						
						expandContainer(dbc, assetProps.stringPropertyNames().toArray(new String[assetProps.size()]), columnMap);
						
						for (String propertyName : assetProps.stringPropertyNames() ) { /* use properties for partial index */ 						
							try {
								String propertyValue = a.getProperty(propertyName);
								logger.fine("prop-" + propertyName + " -- " + propertyValue);
								if (propertyValue!=null && !"".equals(propertyValue)) {
									
									if (IndexStatus.NOT_INDEXED.equals(idxStatus)) {
										idxId = addIndex(dbc,reposId,assetId,typeKeyword,relativePartStr,getDbIndexedColumn(typeKeyword,propertyName),propertyValue);
										idxStatus = IndexStatus.STALE;
									} else if (idxId!=-1) {
										updateIndex(dbc, idxId, getDbIndexedColumn(typeKeyword,propertyName), propertyValue);									
									}

//									totalAssetsIndexed++;
								}
							} catch (Exception e)  {
								; // Ignore if property doesn't exist
							}					
						}

						updateIndexKeys(dbc, idxId, "hash=?,reposAcs=?,location=?", new String[]{hash, repos.getSource().getAccess().name(), relativePartStr}); // Note the use of unquoted identifier vs. quoted identifiers for asset property references 				
			}
				
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.warning(ex.toString());
			} finally {
				dbc.close();
			}
	}
	}


	/**
	 * @param reposId
	 * @param reposFsIndex
	 * @param reposDbIndex
	 * @throws SQLException
	 */
	private void cleanupStaleItems(Repository repos,
			Map<String, String> reposFsIndex, Map<String, String> reposDbIndex)
			throws SQLException {
		// build a stale list
		boolean staleItems = false;
		DbContext dbc = new DbContext();
		PreparedStatement ps = null;
		
		try {
			String reposId = repos.getId().getIdString();
			dbc.setConnection(DbConnection.getInstance().getConnection());
			ps = dbc.prepareBulkUpdate("delete from " + repContainerLabel + " where " + repId + "=? and reposAcs=? and location=?");
			
			for (String key : reposDbIndex.keySet()) {
				if (!reposFsIndex.containsKey(key) || !reposDbIndex.get(key).equals(reposFsIndex.get(key))) {
					staleItems = true;
					dbc.setBulkParameters(ps, new String[]{reposId, repos.getSource().getAccess().name(), key});
				}
			}
			
			if (staleItems) {
				dbc.updateBulk(ps);
			}
			
		} catch (Exception ex) {
			logger.warning(ex.toString());
		} finally {
			if (ps!=null) ps.close();
			dbc.close();
		}
	}


	/**
	 * @param sbParam
	 * @param paramValues
		private static enum RefreshParamIndex {
		UNDEFINED,
		REPOSID,
		REPOSACS				
	};
	private void refreshIndex(StringBuffer sbParam, String[] paramValues) {
		DbContext dbc = new DbContext();
		try {
			dbc.setConnection(DbConnection.getInstance().getConnection());
			
	
			String sqlStr = "delete from " + repContainerLabel 
					+ " where " + repId + "=? and reposAcs=? and " + locationId + " not in(" + sbParam.toString() + ")";
			int[] rsData = dbc.executePreparedId(sqlStr, paramValues);
			
			logger.fine("refresh rows affected: " + rsData[0]);
			
		} catch (Exception ex) {
			logger.warning("error in stale index cleanup. " + ex.toString());
		} finally {
			dbc.close();
		}
	}
	*/
	
	
	private boolean setReposHash(Repository repos, String hash, boolean update) throws RepositoryException {
		if (repos!=null) {
			DbContext dbc = new DbContext();			
			try {
				String reposId = repos.getId().getIdString();
				String compositeId = reposId + "_" +  repos.getSource().getAccess().name();
				dbc.setConnection(DbConnection.getInstance().getConnection());
				
				if (update) {
					String sqlStr = "update "+ repContainerLabel + " set hash =? where " + DbIndexContainer.repId + "=? and indexSession=?";
					int rsData = dbc.executePrepared(sqlStr, new String[]{hash, compositeId, CACHED_SESSION});
					logger.fine("rows affected" + rsData);
				} else {
					String sqlStr = "insert into "+ repContainerLabel  +"(hash," + DbIndexContainer.repId + ",indexSession) values(?,?,?)";
					int[] id = dbc.executePreparedId(sqlStr, new String[]{hash, compositeId, CACHED_SESSION});
					logger.fine("Inserted hash "  + hash + " key "  + id[1] +  " for " + compositeId);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
			} finally {
				dbc.close();				
			}
		}
		return true;
	}


	
	private String[] getReposHash(Repository repos) {
		String[] rsData = null;
		DbContext dbc = new DbContext();

		try {
			String reposId = repos.getId().getIdString();
			String compositeId = reposId + "_" +  repos.getSource().getAccess().name();			
			dbc.setConnection(DbConnection.getInstance().getConnection());

			String sqlStr = "select repositoryIndexId,hash from " + repContainerLabel 
					+" where " + DbIndexContainer.repId + "=? and indexSession=?";
							
			ResultSet rs = dbc.executeQuery(sqlStr, new String[]{compositeId, CACHED_SESSION});

			if (rs!=null) {
				if (rs.next()) {
					 rsData = new String[]{new Integer(rs.getInt(1)).toString(), rs.getString(2)};
				}
				rs.close();
			}
			
		} catch (Exception ex) {
			logger.warning(ex.toString());
		} finally {
			dbc.close();
		}

		return rsData;		
	}
	
	private Map<String, String> getIndexedHash(Repository repos) {
		Map<String, String> reposIndex = new HashMap<String,String>();
		DbContext dbc = new DbContext();

		try {
			dbc.setConnection(DbConnection.getInstance().getConnection());

			String sqlStr = "select repositoryIndexId,location,hash from " + repContainerLabel 
					+" where " + DbIndexContainer.repId + "=? and reposAcs=?";
							
			ResultSet rs = dbc.executeQuery(sqlStr, new String[]{repos.getId().getIdString(),repos.getSource().getAccess().name()});

			if (rs!=null) {
				while (rs.next()) {
					 reposIndex.put(rs.getString(2), rs.getString(3));
				}
				rs.close();
			}

			
		} catch (Exception ex) {
			logger.warning(ex.toString());
		} finally {
			dbc.close();
		}
		
		return reposIndex;
		
	}
	
	private boolean isReposQueuedForIndexing(Repository repos) {
		DbContext dbc = new DbContext();

		int queuedItems = 0;
		try {
			String reposId = repos.getId().getIdString();
			dbc.setConnection(DbConnection.getInstance().getConnection());

			String sqlStr = "select count(*)ct from " + repContainerLabel 
					+" where " + DbIndexContainer.repId + "=? and reposAcs=? and indexSession='QUEUED'";
							
			ResultSet rs = dbc.executeQuery(sqlStr, new String[]{reposId,repos.getSource().getAccess().name()});

			if (rs!=null) {
				while (rs.next()) {
					 queuedItems = rs.getInt(1);
					 if (queuedItems>0) {
						 logger.info("Repos <" + reposId + "> currently has " + queuedItems + " queued for indexing.");
					 }					 
				}
				rs.close();
			}

			
		} catch (Exception ex) {
			logger.warning(ex.toString());
		} finally {
			dbc.close();
		}
		
		return (queuedItems>0);		
	}
	
	public boolean queueReposForIndex(Repository repos, boolean enabled) throws RepositoryException {
		if (repos!=null) {
			DbContext dbc = new DbContext();			
			try {
				String reposId = repos.getId().getIdString();
				dbc.setConnection(DbConnection.getInstance().getConnection());
	 
				String modeStr = "'QUEUED'";
				if (!enabled) {
					modeStr = "null";
				}
				String sqlStr = "update "+ repContainerLabel + " set indexSession="+ modeStr +" where " + repId + " =? and reposAcs=?";
				int rsData = dbc.executePrepared(sqlStr, new String[]{reposId, repos.getSource().getAccess().name()});
				logger.fine(rsData + " items in <"+ reposId + "> " + (enabled?"queued":"de-queued"));

			} catch (SQLException e) {
				e.printStackTrace();
				throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
			} finally {
				dbc.close();				
			}
		}
		return true;
	}
	
	private List<Object> getIndexStatus(Repository repos, String assetId, String relatviePartStr, String hash) throws RepositoryException {
		List<Object> rsData = new ArrayList<Object>();
		DbContext dbc = new DbContext();
		try {
			dbc.setConnection(DbConnection.getInstance().getConnection());
						
			/*
			 * Value based
			String sqlStr = "select 1 from " + repContainerLabel 
					+" where " + DbIndexContainer.repId + "=? and " + DbIndexContainer.assetId +"=? and hash='" + hash + "'";
							
			ResultSet rs = dbc.executeQuery(sqlStr, new String[]{repId,id});
			*/
			
			String sqlStr = "select repositoryIndexId,hash from " + repContainerLabel 
					+" where " + DbIndexContainer.repId + "=? and reposAcs=? and " + DbIndexContainer.locationId +"=?";
							
			ResultSet rs = dbc.executeQuery(sqlStr, new String[]{repos.getId().getIdString(),repos.getSource().getAccess().name(), relatviePartStr});

			if (rs!=null) {
				if (rs.next()) { // exists				
					int id = rs.getInt(1);
					String indexedHash = rs.getString(2);
					if (hash!=null && hash.equals(indexedHash)) {
						rsData.add(IndexStatus.INDEXED);
					} else {
						rsData.add(IndexStatus.STALE);
					}
					rsData.add(new Integer(id));
				} else {					
					rsData.add(IndexStatus.NOT_INDEXED);				
				}
				rs.close();					
			} else {
				rsData.add(IndexStatus.NOT_INDEXED);
			}
			
			return rsData;
						
		} catch (Exception e) {
			throw new RepositoryException("Error " + e.toString());
		} finally {
			dbc.close();
		}
						
	}
		
	private String getHash(byte[] ba) {
		try {
			return new Hash().compute_hash(ba);
		} catch (Exception e) {
			logger.warning("Hash compute error  " + e.toString());
		}
		return "";
	}

	
	/**
	 * 
	 * @param assetId
	 * @param assetType
	 * @param property
	 * @return
	 
	public String getIndexedPropertyByAssetId(String assetId, String assetType, String  property) {
		try {
			
			DbContext dbc = new DbContext();
			dbc.setConnection(DbConnection.getInstance().getConnection());
			
			String dbCol  = getDbIndexedColumn(assetType, property);
			
			ResultSet rs = dbc.executeQuery("select "+ dbCol +" from "+repContainerLabel + " where " + DbIndexContainer.assetId + "='"+assetId+"'");
			String out = "";
			
			while (rs.next()) {
				out = rs.getString(getColumn(assetType,property));

			}
			dbc.close(rs);			

			return out;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
			
	}
	*/
	
	/**
	 * 
	 * @param assetId
	 * @param assetType
	 * @param property
	 * @return
	 */ 

	/**
	 *  
	 * @param repositories
	 * @param searchCriteria
	 * @return
	 */
	public CachedRowSet getAssetsBySearch(Repository[] repositories, SearchCriteria searchCriteria) {
		return getAssetsBySearch(repositories, searchCriteria, "");
	}
	
	public int getHitCount(Repository repos, SearchCriteria searchCriteria, String orderByStr) throws RepositoryException {
		int records=0;
		String searchCriteriaWhere = searchCriteria.toString();		

		String sqlStr = "select count(*)ct from " + repContainerLabel 
		+ " where " + repId + " = ? and reposAcs=? and( "+ searchCriteriaWhere + ")" ;
		logger.fine(sqlStr);
		
		DbContext dbc = new DbContext();
		try {					
				dbc.setConnection(DbConnection.getInstance().getConnection());
				ResultSet rs = dbc.executeQuery(sqlStr, new String[]{repos.getId().getIdString(), repos.getSource().getAccess().name()});

				if (rs!=null) {
					if (rs.next()) {
				          records = rs.getInt("ct");
				          logger.fine("search hit records: " + records);

					}
					rs.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				logger.warning("possible non-existent column in where clause? " + e.toString());
			} finally {
				dbc.close();
			}
		return records;
	}
	
	/**
	 * An Order by property is only limited to what is available as per the session container not per columns available in the index container.
	 * @param repositories
	 * @param searchCriteria
	 * @param orderBy
	 * @return
	 */
	public CachedRowSet getAssetsBySearch(Repository[] repositories, SearchCriteria searchCriteria, String orderByStr) {
		DbContext dbc = new DbContext();		
		try {
			// Make sure properties exist
			// ArrayList<String> searchProperties = searchCriteria.getProperties();
			String searchCriteriaWhere = searchCriteria.toString();
			logger.fine(searchCriteriaWhere);
	

			dbc.setConnection(DbConnection.getInstance().getConnection());
			
			String searchSession = "session."+PnIdentifier.getQuotedIdentifer("SearchResults" + new IdFactory().getNewId().getIdString());
			
			
			try {
				dbc.internalCmd("drop table "+searchSession);
			} catch (SQLException e) {
				; // Ignore if it does not exist
			}
			dbc.internalCmd("create table "+searchSession+"(repId varchar(64),assetId varchar(64), reposAcs varchar(40), reposOrder int, displayOrder int, createdDate varchar(64), propFile varchar(512))");
			
			
			// Search needs to limit search properties to the ones supported by assets belonging to those repositories -
			//  - to avoid searching for out-of-reach properties that do not apply to the repositories in question
			// This needs to be independent from the syncRep call because there columns might not have fully expanded 
			int orderBy=0;
			for (Repository rep : repositories) {
				
				String sqlStr = "insert into "+searchSession+"(repId,assetId,reposAcs,reposOrder,displayOrder,createdDate,propFile)"
						+"select " + DbIndexContainer.repId + ","+ DbIndexContainer.assetId + ",reposAcs," + (orderBy++) + "," + displayOrder + "," + createdDate + "," + locationId + " from " + repContainerLabel 
						+ " where " + repId + " = ? and reposAcs=? and( "+ searchCriteriaWhere + ")" ;
				
				try {					
				//	 dbc.internalCmd(sqlString);
					
//					int[] rsData = dbc.executePreparedId(sqlStr, new String[]{rep.getId().getIdString()});
//					logger.fine("rows affected: " + rsData[0]);
					
					int records = dbc.executePrepared(sqlStr, new String[]{rep.getId().getIdString(), rep.getSource().getAccess().name() });
					logger.fine("rows affected: " + records);
					
				} catch (SQLException e) {
					e.printStackTrace();
					logger.warning("possible non-existent column in where clause? " + e.toString());
				}
			}
					
			ResultSet rs = dbc.executeQuery("select repId,assetId,reposAcs,propFile from "+searchSession+" order by reposOrder" + ((orderByStr!=null && !"".equals(orderByStr))?","+orderByStr:"")); //group by repId,assetId,reposOrder,displayOrder order
			
			CachedRowSet crs = new CachedRowSetImpl();
    		crs.populate(rs);
    		rs.close();			
			dbc.close(rs);			
			
			return crs;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
			
	}
	
	/**
	 * 
	 * @param searchCriteria
	 * @return
	 
	private String getWhereClause(SearchTerm[] searchCriteria) {
		String whereClause = " where ";
		
		if (searchCriteria!=null) {
			for (SearchTerm st : searchCriteria) {
				if (st.getPropName()!=null && st.getValues()!=null) {
					
					String propertyClause;
					try {
						propertyClause = getDbIndexedColumn(st.getAssetType(), st.getPropName()) + " = ";
					} catch (RepositoryException e) {
						System.out.println("malformed property or asset type");
						e.printStackTrace();
					}
					int propValCt = 1;
					for (String propVal : st.getValues()) {
						// Handle date range here - 
						// see if the property is associated with Timestamp specifier from the Indexable Property List 
						
						propertyClause += "'" + propVal + "'";
						if (propValCt<st.getValues().length) {
							propertyClause += " or ";
						}
						
								
					}
				}
			}
		}
		
		return null;
		
	}
	*/

	
}