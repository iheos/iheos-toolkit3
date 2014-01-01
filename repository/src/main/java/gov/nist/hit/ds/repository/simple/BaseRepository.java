package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.LongValueIterator;
import gov.nist.hit.ds.repository.api.PropertiesIterator;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.api.TypeIterator;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.Properties;

public abstract class BaseRepository implements Repository {
	private static final long serialVersionUID = 7947876287785415118L;
	static final String REPOSITORY_PROPERTY_FILE = "repository.props.txt";

	File root = null;  // directory holding this repository
	boolean initialized = false;
	boolean loaded = false;
	Properties properties = new Properties();
	boolean isNew;
	
	private RepositorySource source;
	private Id reposId;

	@Override
	public File getRoot() throws RepositoryException {		
		return root;
	}

	public void setRoot(File root) {
		this.root = root;
	}	
	public boolean isLoaded() {
		return loaded;
	}
	
	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	
	/**
	 * Open an existing repository.
	 * @param root - filesystem directory that holds the repository contents
	 * @throws RepositoryException
	 */
	public BaseRepository(Id id) throws RepositoryException {
		setReposId(id);
		isNew = false;
	}
	
	/**
	 * Create new Repository.
	 * @throws RepositoryException
	 */
	public BaseRepository() throws RepositoryException {
		isNew = true;
		setReposId(new IdFactory().getNewId());
		properties.setProperty(PropertyKey.ASSET_ID.toString(), getReposId().getIdString());
	}
	
	/**
	 * Create new named Repository.
	 * @throws RepositoryException
	 */
	public BaseRepository(String name) throws RepositoryException {
		isNew = true;
		setReposId(new SimpleId(name));
		properties.setProperty(PropertyKey.ASSET_ID.toString(), getReposId().getIdString());
	}

	public void load() throws RepositoryException {
		
		if (!isNew && !isLoaded()) {
			File typeDescriptorFile = new File(
					root + File.separator + 
					REPOSITORY_PROPERTY_FILE);
			properties = new Properties();
			try {
				FileReader fr = new FileReader(typeDescriptorFile);
				properties.load(fr);
				fr.close(); // This will release the file lock
			} catch (Exception e) {
				throw new RepositoryException(RepositoryException.UNKNOWN_REPOSITORY + " : " + root, e);
			} finally {
				loaded = true;
			}
		}
	}
	
	
	public void setType(Type type) throws RepositoryException {
//		load();
		properties.setProperty("repositoryType", type.toString());
	}

	@Override
	public Type getType() throws RepositoryException {
//		load();
		return new SimpleType(properties.getProperty("repositoryType"), "");
	}


	@Override
	public String getDisplayName() throws RepositoryException {
//		load();
		return properties.getProperty(PropertyKey.DISPLAY_NAME.toString());
	}

	@Override
	public Id getId() throws RepositoryException {
//		load();
		
		if (!loaded) {
			return getReposId();
		} else {
			String idString = properties.getProperty("id", "");
			if (idString.equals("")) {
				throw new RepositoryException(RepositoryException.UNKNOWN_ID + 
						" - loading repository but Repository.id is empty");
			}
			Id id = new SimpleId(idString);
			return id;
			
		}			
		
	}

	@Override
	public String getDescription() throws RepositoryException {
		return properties.getProperty("description");
	}


	@Override
	/**
	 * This method returns an asset that is primarily used for read-only operations. To update an asset return by this method, setAutoFlush to true.
	 */
	public Asset getAsset(Id assetId) throws RepositoryException {
		// File reposDir =  new File(Configuration.getRepositoriesDataDir(getSource()).toString()  + File.separator + getId().getIdString());
		File reposDir =  getRoot();
		if (!reposDir.exists() || !reposDir.isDirectory())
			throw new RepositoryException(RepositoryException.UNKNOWN_REPOSITORY + " : " +
					"directory for repositoryId [" + getId() + "] does not exist");
		// File assetBaseFile = new File(reposDir.toString() + File.separator + assetId.getIdString());
		SimpleAsset a = new SimpleAsset(getSource());
		a.setAutoFlush(false);
		a.setRepository(getReposId());
		a.setId(assetId);
		a.load(assetId);
		
		return a;
	}
	
	public Asset getAssetByRelativePath(File assetPath) throws RepositoryException {
		try {
			SimpleAsset a = new SimpleAsset(getSource());
			File fullPath = new File(getRoot() + assetPath.toString());
			FolderManager.loadProps(a.getProperties(), fullPath);
			a.setPath(fullPath);
			return a;			
		} catch (Exception ex) {
			throw new RepositoryException(RepositoryException.ERROR_ASSIGNING_CONFIGURATION + ex.toString());
		}
	
	}
	
	public Asset getAssetByPath(File assetPath) throws RepositoryException {
		try {
			SimpleAsset a = new SimpleAsset(getSource());
			FolderManager.loadProps(a.getProperties(), assetPath);
			a.setPath(assetPath);
			return a;			
		} catch (Exception ex) {
			throw new RepositoryException(RepositoryException.ERROR_ASSIGNING_CONFIGURATION + ex.toString());
		}

	}

	@Override
	public AssetIterator getAssets() throws RepositoryException {
		
		SimpleRepository repos = new SimpleRepository(getId());
		repos.setSource(getSource());
		return new SimpleAssetIterator(repos);
	}

	@Override
	public AssetIterator getAssetsByType(Type assetType)
			throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public TypeIterator getAssetTypes() throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	public String getPropertyValueByKey(String key) {
		return properties.getProperty(key);
	}
	
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	@Override
	public Properties getPropertiesByType(Type propertiesType)
			throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public TypeIterator getPropertyTypes() throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	public Properties getRepositoryProperties() {
		return properties;
	}

	@Override
	public PropertiesIterator getPropertiesIterator() throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}
	
	@Override
	public Properties getProperties() throws RepositoryException {
		return properties;
	}

	@Override
	public TypeIterator getSearchTypes() throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public TypeIterator getStatusTypes() throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public Type getStatus(Id assetId) throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public boolean validateAsset(Id assetId) throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public void invalidateAsset(Id assetId) throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public Asset getAssetByDate(Id assetId, long date)
			throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public LongValueIterator getAssetDates(Id assetId)
			throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public AssetIterator getAssetsBySearch(Serializable searchCriteria,
			Type searchType, Properties searchProperties)
					throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public Id copyAsset(Asset asset) throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public boolean supportsVersioning() throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public boolean supportsUpdate() throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	protected File getRepositoryPropFile() throws RepositoryException {
//		load();
		//return new File (Configuration.getRepositoriesDataDir(getSource()) + File.separator + getId() + File.separator + REPOSITORY_PROPERTY_FILE); 
		return new File(root.toString() + File.separator + REPOSITORY_PROPERTY_FILE);
	}


	public boolean isConfigured() throws RepositoryException {
//		load();
		File propFile = getRepositoryPropFile();
		return propFile.exists();
	}

	public void delete() throws RepositoryException {
//		load();
		if (!isConfigured()) 
			return;
		delete(root);
	}

	void delete(File fileToDelete) throws RepositoryException {
//		load();
		File[] files = fileToDelete.listFiles();
		if (files != null) {
			for (int i=0; i<files.length; i++) {
				File file = files[i];
				delete(file);
			}
		}
		fileToDelete.delete();
	}

	public String getProperty(String name) throws RepositoryException {
//		load();
		return properties.getProperty(name);
	}

	@Override
	public RepositorySource getSource() throws RepositoryException {
		
		return source;
	}

	@Override
	public void setSource(RepositorySource source) throws RepositoryException {
		if (source==null) {
			throw new RepositoryException(RepositoryException.CONFIGURATION_ERROR + ": source cannot be null.");
		}
		
		this.source = source;		
		
		root = new File(Configuration.getRepositoriesDataDir(getSource()) + File.separator + getId().getIdString());
		
		load();
	}

	
	private Id getReposId() {
		return reposId;
	}

	private void setReposId(Id reposId) {
		this.reposId = reposId;
	}


	


}
