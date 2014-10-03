package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
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
import java.util.Properties;

/**
 * BaseRepository provides functions common to all types of repositories, which is filesystem based.
 */
public abstract class BaseRepository implements Repository {
	private static final long serialVersionUID = 7947876287785415118L;
	static final String REPOSITORY_PROPERTY_FILE = "repository.props.txt";

	File root = null;  // directory holding this repository
	boolean loaded = false;
	Properties properties = new Properties();
	boolean isNew;
	
	private RepositorySource source;
	private ArtifactId reposId;

    /**
     *
     * @return
     * @throws RepositoryException
     */
	@Override
	public File getRoot() throws RepositoryException {
        if (root==null)
            throw new RepositoryException(RepositoryException.CONFIGURATION_ERROR + ": Repository root is not set.");
		return root;
	}

    /**
     * It is the directory holding this repository. This is the root of the repository within a repository source, not to be confused with the repository source base itself.
     * @param root
     */
	public void setRoot(File root) {
		this.root = root;
	}

    /**
     * Indicates the repository state, in other words the repository properties are available for working with assets.
     * @return
     */
	public boolean isLoaded() {
		return loaded;
	}

    /**
     * Indicates whether this repository is newly created or previously loaded from an existing root.
     * @return
     */
	public boolean isNew() {
		return isNew;
	}

    /**
     * A {@code true} value indicates this repository is newly created or previously loaded from an existing root.
     * @param isNew
     */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	
	/**
	 * Open an existing repository.
	 * @param id ArtifactId The ArtifactId contains the name of the repository. This is invariably true for repository Ids unlike Assets Ids.
	 * @throws RepositoryException
	 */
	public BaseRepository(ArtifactId id) throws RepositoryException {
		setReposId(id);
		isNew = false;
	}
	
	/**
	 * Create new Repository with an auto-generated Id.
	 * @throws RepositoryException
	 */
	public BaseRepository() throws RepositoryException {
		isNew = true;
		setReposId(new IdFactory().getNewId());
		properties.setProperty(PropertyKey.ASSET_ID.toString(), getReposId().getIdString());
	}
	
	/**
	 * Create new named Repository.
     * @param name The repository name. Any special characters not allowed by the underlying system will be converted to a safe-name.
	 * @throws RepositoryException
	 */
	public BaseRepository(String name) throws RepositoryException {
		isNew = true;
		setReposId(new SimpleId(name));
		properties.setProperty(PropertyKey.ASSET_ID.toString(), getReposId().getIdString());
	}

    /**
     * Loads the repository assuming the {@code root} and the {@code RepositorySource} is configured properly.
     * @throws RepositoryException
     */
	public void load() throws RepositoryException {
		
		if (!isNew && !isLoaded()) {
			File propFile = getRepositoryPropFile();
			properties = new Properties();
            FileReader fr = null;
			try {
				fr = new FileReader(propFile);
				properties.load(fr);
                loaded = true;
			} catch (Exception e) {
				throw new RepositoryException(RepositoryException.UNKNOWN_REPOSITORY + " : " + root, e);
			} finally {
                if (fr!=null) {
                    try {
                        fr.close(); // This will release the file lock
                    } catch (Throwable t) {}
                }
			}
		}
	}

    /**
     * Sets the type of the repository. See the types folder in the {@code RepositorySource}.
     * @param type {@code Type}
     * @throws RepositoryException
     */
	public void setType(Type type) throws RepositoryException {
//		load();
		properties.setProperty(PropertyKey.REPOSITORY_TYPE.toString(), type.toString());
	}

    /**
     *
     * @return Type of the repository
     * @throws RepositoryException
     */
	@Override
	public Type getType() throws RepositoryException {
//		load();
		return new SimpleType(properties.getProperty(PropertyKey.REPOSITORY_TYPE.toString()), "");
	}

    /**
     *
     * @return
     * @throws RepositoryException
     */
	@Override
	public String getDisplayName() throws RepositoryException {
//		load();
		return properties.getProperty(PropertyKey.DISPLAY_NAME.toString());
	}

    /**
     *
     * @return
     * @throws RepositoryException
     */
	@Override
	public ArtifactId getId() throws RepositoryException {
//		load();
		
		if (!loaded) {
			return getReposId();
		} else {
			String idString = properties.getProperty(PropertyKey.ASSET_ID.toString(), "");
			if (idString.equals("")) {
				throw new RepositoryException(RepositoryException.UNKNOWN_ID + 
						" - loading repository but Repository.id is empty");
			}
			ArtifactId id = new SimpleId(idString);
			return id;
			
		}			
		
	}

    /**
     *
     * @return
     * @throws RepositoryException
     */
	@Override
	public String getDescription() throws RepositoryException {
		return properties.getProperty(PropertyKey.DESCRIPTION.toString());
	}


    /**
     * Retrieves an immediate child by name.
     * @param name Name is case-sensitive. See {@link gov.nist.hit.ds.repository.simple.SimpleRepository#createNamedAsset(String, String, Type, String)}
     * @return
     * @throws RepositoryException
     */
    @Override
    public Asset getChildByName(String name) throws RepositoryException {
        File[] assetFileByName = new FolderManager().getAssetFileByName(name, getRoot(),"$topLevelAsset");
        Asset asset = new SimpleAsset(getSource());

        try {
            FolderManager.loadProps(asset.getProperties(), assetFileByName[0]);
            asset.setPath(assetFileByName[0]);
            asset.setContentPath(assetFileByName[1]);
        } catch (Exception e) {
            throw new RepositoryException(RepositoryException.UNKNOWN_ID + " : " +
                    "properties cannot be loaded: [" +
                    (assetFileByName==null?"null":assetFileByName[0])
                    + "]", e);
        }

        return asset;
    }



    /**
     * This method returns an asset by the assetId.
     * @param assetId
     *
     * @return {@code Asset} The asset object is primarily used for read-only operations.
     * @throws RepositoryException {@code RepositoryException.UNKNOWN_REPOSITORY} is thrown when the repository could not be loaded
     */
	@Override
	public Asset getAsset(ArtifactId assetId) throws RepositoryException {
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

    /**
     * Gets an asset using a relative path
     * @param assetPath The path off the repository root
     * @return {@code Asset}
     * @throws RepositoryException
     */
	public Asset getAssetByRelativePath(File assetPath) throws RepositoryException {
		try {
			File fullPath = new File(getRoot(), assetPath.toString());

            return getAssetByPath(fullPath);
		} catch (Exception ex) {
			throw new RepositoryException(RepositoryException.ERROR_ASSIGNING_CONFIGURATION + ex.toString());
		}
	
	}

    /**
     * Gets an asset using the full path, which is the absolute path from the system root.
     * @param assetPath absolute path
     * @return
     * @throws RepositoryException
     */
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

    /**
     * Provides an iterator for all assets within the repository.
     * @return {@code AssetIterator}
     * @throws RepositoryException
     */
	@Override
	public AssetIterator getAssets() throws RepositoryException {
		
//		SimpleRepository repos = new SimpleRepository(getId());
//		repos.setSource(getSource());
		return new SimpleAssetIterator(this);
	}

    /**
     * Provides an iterator for all assets within the repository based on the asset type.
     * @param assetType {@code Type}
     * @return {@code AssetIterator}
     * @throws RepositoryException
     */
	@Override
	public AssetIterator getAssetsByType(Type assetType)
			throws RepositoryException {
//        SimpleRepository repos = new SimpleRepository(getRepository());
//        repos.setSource(getSource());

        return new SimpleAssetIterator(this, assetType);
	}

    /**
     *
     * @return {@code TypeIterator}
     * @throws RepositoryException
     */
	@Override
	public TypeIterator getAssetTypes() throws RepositoryException {
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

    /**
     * Retrieves asset property value by key
     * @param key The property key
     * @return {@code String}
     */
	public String getPropertyValueByKey(String key) {
		return properties.getProperty(key);
	}


    /**
     *
     * @param key The property key
     * @param value The value
     */
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}

    /**
     * Not implemented.
     * @param propertiesType
     * @return
     * @throws RepositoryException
     */
	@Override
	public Properties getPropertiesByType(Type propertiesType)
			throws RepositoryException {
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

    /**
     * Not implemented.
     * @return
     * @throws RepositoryException
     */
	@Override
	public TypeIterator getPropertyTypes() throws RepositoryException {
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

    /**
     * Returns the {@code properties} object.
     * @return
     */
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
	public Type getStatus(ArtifactId assetId) throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public boolean validateAsset(ArtifactId assetId) throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public void invalidateAsset(ArtifactId assetId) throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public Asset getAssetByDate(ArtifactId assetId, long date)
			throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	@Override
	public LongValueIterator getAssetDates(ArtifactId assetId)
			throws RepositoryException {
//		load();
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}


	@Override
	public ArtifactId copyAsset(Asset asset) throws RepositoryException {
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
		// return new File(root.toString() + File.separator + REPOSITORY_PROPERTY_FILE);
        return new File(root.toString(),REPOSITORY_PROPERTY_FILE);
	}


	public boolean isConfigured() throws RepositoryException {
//		load();
		File propFile = getRepositoryPropFile();
		return propFile.exists();
	}

    @Override
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

    public String getProperty(PropertyKey pk) throws RepositoryException {
        return getProperty(pk.toString());
    }

	@Override
	public RepositorySource getSource() throws RepositoryException {
		
		return source;
	}

    /**
     * Associates the source of the repository base with this repository.
     * @param source The {@code RepositorySource} to associate with this repository
     * @throws RepositoryException An exception will be thrown if the repository could not be initialized or loaded from the data path.
     */
	@Override
	public void setSource(RepositorySource source) throws RepositoryException {
		if (source==null) {
			throw new RepositoryException(RepositoryException.CONFIGURATION_ERROR + ": source cannot be null.");
		}
		
		this.source = source;		
		
		setRoot(new File(Configuration.getRepositoriesDataDir(getSource()), getId().getIdString()));
		
		load();
	}

	
	private ArtifactId getReposId() {
		return reposId;
	}

	private void setReposId(ArtifactId reposId) {
		this.reposId = reposId;
	}


	


}
