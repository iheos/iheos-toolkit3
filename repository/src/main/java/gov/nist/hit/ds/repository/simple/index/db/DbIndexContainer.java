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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
	
	private static final String CONTAINER_VERSION = "2013-12-11";
	private static final String CONTAINER_VERSION_ID = "VERSION";	
	private static final String BYPASS_VERSION = "BYPASS";
	
	/* Use an upgrade script to update existing tables in case a newer version of TTT (new ArtRep API) runs against an older copy of the repositoryIndex table in the database */
	private static final String repContainerDefinition = 
	"(repositoryIndexId integer not null  generated always as identity," 	/* (Internal use) This is the primary key */
	+ repId + " varchar(64) not null,"								/* This is the repository Id as it appears on the filesystem */
	+ assetId + " varchar(64)," /* */							/* This is the asset Id of the asset under the repository folder */
	+ locationId + " varchar(512),"								/* This is the file path */
	+ parentId +  " varchar(64),"									/* The parent asset Id. A null-value indicates top-level asset and no children */
	+ assetType + " varchar(32)," 									/* Asset type - usually same as the keyword property */
	+"hash varchar(40),"											/* (Internal use) The hash of the property file */
	+"reposAcs varchar(40),"										/* (Internal use) src enum string */	
	+ displayOrder + " int,"         						    	/* This is a reserved keyword for sorting purpose */
	+"repoSession varchar(64))";									/* (Internal use) Stores the indexer repository session id -- later used for removal of stale assets */				
			
	
	@Override
	public String getIndexContainerDefinition() {
		String repContainerHead = 
		"create table " + repContainerLabel;				/* This is the master container for all indexable asset properties */
	
		logger.fine("using label " + repContainerLabel);
		
		return repContainerHead + repContainerDefinition;
		
	}
	
	public int getIndexCount() throws RepositoryException {
		
		try {
			return getQuickCount("select count(*)ct from "+repContainerLabel);
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
				
			
				
		} catch (Exception e) {
			logger.info(e.toString());
		} finally {
			dbc.close(rs);
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
					
					String index = "create unique index \"repAssetUniqueIdx" + repContainerDefinition.hashCode() + "\" on " + repContainerLabel + " (repositoryIndexId,repoSession)";  // ,"+ repId +"," +  assetId +" These may not be unique anymore with multiple rep sources 
					dbc.internalCmd(index);					
					index = "create index \"repAssetIdx" + repContainerDefinition.hashCode() + "\" on " + repContainerLabel + " ("+ repId +"," +  assetId +"," + assetType +",reposAcs,hash,"+ locationId + ")";
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
		int[] rsData = null;
		DbContext dbc = new DbContext();
		try {
			dbc.setConnection(DbConnection.getInstance().getConnection());
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
		} finally {
			dbc.close();
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
	public void updateIndex(Repository repos, String assetId, String assetType, String locationStr, String propCol, String value)
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

	public void updateIndex(int idKey, String propCol, String value)
			throws RepositoryException {
		DbContext dbc = new DbContext();		
		try {				
			dbc.setConnection(DbConnection.getInstance().getConnection());
						
				// Path based
				String sqlStr = "update "+ repContainerLabel + " set "+propCol+"=? where repositoryIndexId="+idKey						   
						+" and ("+propCol+" is null or "+propCol+" != ?)";
				
				int[] rsData = dbc.executePreparedId(sqlStr, new String[]{value, value});				
				logger.fine("rows affected: " + rsData[0]);


		} catch (SQLException e) {
			e.printStackTrace();
			throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
		} finally {
			dbc.close();
		}
		
	}

	public void updateIndexKeys(int idKey, String setFragment, String[] val)
			throws RepositoryException {
		DbContext dbc = new DbContext();		
		try {				
			dbc.setConnection(DbConnection.getInstance().getConnection());
						
				// Path based
				String sqlStr = "update "+ repContainerLabel + " set " + setFragment + " where repositoryIndexId=" + idKey;
				
				int[] rsData = dbc.executePreparedId(sqlStr, val);				
				logger.fine("rows affected: " + rsData[0]);


		} catch (SQLException e) {
			e.printStackTrace();
			throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
		} finally {
			dbc.close();
		}
		
	}

	
	@Override
	public void removeIndex(String reposId, String sessionId) throws RepositoryException {
		if (reposId!=null && !"".equals(reposId)) {
			DbContext dbc = new DbContext();			
			try {

				dbc.setConnection(DbConnection.getInstance().getConnection());
	 
				String sqlStr = "delete from "+ repContainerLabel + " where " + repId + " = ? and repoSession !=?";
				int[] rsData = dbc.executePreparedId(sqlStr, new String[]{reposId,sessionId});
				logger.fine("rows affected: " + rsData[0]);
				

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
	
	
	/**
	 * This method will extend the container to allow for new indexable properties.
	 */
	public void expandContainer(String[] column) throws RepositoryException {		 
			expandContainer(column, null);	
	}
	
	/**
	 * This method will extend the container to allow for new indexable properties.
	 */
	public synchronized void expandContainer(String[] column, String assetType) throws RepositoryException {
		DbContext dbc = new DbContext();
		try {			
			dbc.setConnection(DbConnection.getInstance().getConnection());
			String sqlStr = "";
			String index = "";
			
			if (column!=null) {
				int cx=0; 
				for (String c : column) {
					String dbCol = getDbIndexedColumn(assetType, c);									
					
					if (!isIndexed(dbCol)) {
						if (cx++>0) index+=",";
						index += dbCol;

						// Can only add one at a time
						sqlStr = "alter table "+ repContainerLabel + " add column " + dbCol + " varchar(64)";					
						dbc.internalCmd(sqlStr);
					} else {
						logger.fine("Column "+ c +" already exists " + ((assetType!=null)?"for assetType: "+assetType:""));
					}
					
				}
				
			}
			try {
				if (sqlStr!="") {
					index = "create index \"repAssetIdxp" + sqlStr.hashCode() + "\" on " + repContainerLabel + " ("+ index +")";
					dbc.internalCmd(index);					
				}
			} catch (SQLException e) {
				logger.fine("Index probably exists.");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RepositoryException(RepositoryException.INDEX_ERROR, e);
		} finally {
			dbc.close();
		}
		
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
				 
//				System.out.println ("desc:" + t.getDescription());
//				System.out.println ("domain:" + t.getDomain());
//				System.out.println ("indexes:" + t.getIndexes());
//				
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

	private static enum RefreshParamIndex {
		UNDEFINED,
		REPOSID,
		REPOSACS				
	};
	/**
	 * @param totalAssetsIndexed
	 * @param properties
	 * @param assetType
	 * @param repos
	 * @return
	 * @throws RepositoryException
	 */
	public int indexRep(Repository repos, ArrayList<String> properties) throws RepositoryException {
		SimpleAssetIterator iter = null;
		int totalAssetsIndexed = 0;
		
		if (repos==null || repos.getId()==null) {
			logger.warning("Null repository or repository id");
			return -1; 
		}		
		
		if (!doesIndexContainerExist()) {
			createIndexContainer();
			logger.fine("New index container created.");
		}
		
		String reposId = repos.getId().getIdString();		
		
		iter = new SimpleAssetIterator(repos);
		 		
		if (!iter.hasNextAsset()) {
			logger.fine("Nothing to index in " + repos.getId().getIdString());
			return -1;
		}
		StringBuffer sbParam = new StringBuffer();
		
		// Start fresh asset marker
		String[] paramValues = new String[iter.getSize() + RefreshParamIndex.values().length-1]; 
		int cx=0;
		paramValues[RefreshParamIndex.REPOSID.ordinal()] = reposId; 
		paramValues[RefreshParamIndex.REPOSACS.ordinal()] = repos.getSource().getAccess().name(); 
		
		while (iter.hasNextAsset()) {
			Asset a = iter.nextAsset();
			File propFile = a.getPropFile();
			String relativePartStr = a.getPropFileRelativePart(); // Make paths relative to repository root 
			
			
			logger.fine("found indexable asset property: " + relativePartStr);
			
			String assetId = (a.getId()!=null)?a.getId().getIdString():null; 
			if (assetId == null || "".equals(assetId)) {
				logger.fine("Missing asset Id for " + propFile);
			}

			String typeKeyword = (a.getAssetType()==null?null:a.getAssetType().getKeyword());
			// Properties should already be loaded by getAsset call by the Iterator
			Properties assetProps = a.getProperties();
						
			String hash ="";
			try {
				hash = getHash(assetProps.toString().getBytes());
				
				sbParam.append("?"+ (iter.hasNextAsset()?",":""));
				// paramValues[cx++] = assetId;
				paramValues[cx++] = relativePartStr;
				
			} catch (Exception ex) {
				logger.warning(ex.toString());
			}
			
			List<Object> rsData = getIndexStatus(repos, assetId, relativePartStr, hash);
			IndexStatus idxStatus = (IndexStatus)rsData.get(0);
			int idxId = -1;
			if (rsData.size()==2 && rsData.get(1) instanceof Integer) {
				idxId = ((Integer)rsData.get(1)).intValue(); 
			}
			if (IndexStatus.NOT_INDEXED.equals(idxStatus) || IndexStatus.STALE.equals(idxStatus)) {
					
				// TODO: ? "Potential duplicate data warning (import scenario): Asset's repository id does not match the filesystem folder! Found: " + fsFolderName + " expected: "  + codedRepId + " <assetId: " + a.getId().getIdString() +">");					
				
				expandContainer(assetProps.stringPropertyNames().toArray(new String[assetProps.size()]));
			
				for (String propertyName : assetProps.stringPropertyNames() ) { /* use properties for partial index */ 						
					try {
						String propertyValue = a.getProperty(propertyName);
						logger.fine("prop-" + propertyName + " -- " + propertyValue);
						if (propertyValue!=null && !"".equals(propertyValue)) {
							
							if (IndexStatus.NOT_INDEXED.equals(idxStatus)) {
								idxId = addIndex(reposId,assetId,typeKeyword,relativePartStr,getDbIndexedColumn(typeKeyword,propertyName),propertyValue);
								idxStatus = IndexStatus.STALE;
							} else if (idxId!=-1) {
								updateIndex(idxId, getDbIndexedColumn(typeKeyword,propertyName), propertyValue);									
							}

							totalAssetsIndexed++;
						}
					} catch (Exception e)  {
						; // Ignore if property doesn't exist
					}					
				}

				updateIndexKeys(idxId, "hash=?,reposAcs=?,location=?", new String[]{hash, repos.getSource().getAccess().name(), relativePartStr}); // Note the use of unquoted identifier vs. quoted identifiers for asset property references 								
			
		} else if (IndexStatus.INDEXED.equals(idxStatus) ) {
			logger.fine("Already indexed: " + reposId + " : " + idxId);
		}
	}		
		// Take care of stale assets (i.e., assets were indexed at one point but have been removed on filesystem later on)
		// A search would result in ghost assets when stale assets were not removed from the index
		
		// removeIndex(reposId,repoSession);
		
		refreshIndex(sbParam, paramValues);	// Could use idKeys 
		return totalAssetsIndexed;
	}

	/**
	 * @param sbParam
	 * @param paramValues
	 */
	private void refreshIndex(StringBuffer sbParam, String[] paramValues) {
		DbContext dbc = new DbContext();
		try {
			dbc.setConnection(DbConnection.getInstance().getConnection());
			
			/*
			String sqlStr = "delete from " + repContainerLabel 
					+ " where " + repId + "=? and " + assetId + " not in(" + sbParam.toString() + ")";
			int rowsAffected = dbc.executePrepared(sqlStr, paramValues);
			*/

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
						+ " where " + repId + " = ? and( "+ searchCriteriaWhere + ")" ;
				
				try {					
				//	 dbc.internalCmd(sqlString);
					
//					int[] rsData = dbc.executePreparedId(sqlStr, new String[]{rep.getId().getIdString()});
//					logger.fine("rows affected: " + rsData[0]);
					
					int records = dbc.executePrepared(sqlStr, new String[]{rep.getId().getIdString()});
					logger.fine("rows affected: " + records);
					
				} catch (SQLException e) {
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