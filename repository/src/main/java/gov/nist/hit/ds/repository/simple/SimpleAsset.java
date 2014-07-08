package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.Parameter;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.api.TypeIterator;
import gov.nist.hit.ds.utilities.datatypes.Hl7Date;
import gov.nist.hit.ds.utilities.io.Io;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * A SimpleAsset has two main parts on the filesystem: an asset properties file and an optional content file. The content file extension is based on the mimeType {@code setMimeType}. Parent-child relations transform the file to a folder on the filesystem and the parent properties are nested in the parent folder.
 */
public class SimpleAsset implements Asset, Flushable {
	private static Logger logger = Logger.getLogger(SimpleAsset.class.getName());
	Properties properties = new Properties();
	byte[] content = null;
	boolean loadContentAttempted = false;
	boolean autoFlush = true;
	ArtifactId id = null;
	
	transient boolean indexable = false;
	transient RepositorySource source;
	private transient File path;
	private transient File contentPath = null;

    /**
     * Creates a new instance of an asset only in memory, which is eventually intended towards flushing to disk.
     * @param source {@code RepositorySource}
     * @throws RepositoryException
     */
	public SimpleAsset(RepositorySource source) throws RepositoryException {
		super();
		setCreatedDate(new Hl7Date().now());
		setSource(source);
	}

    /**
     * Sets the repository Id
     * @param repositoryId repository Id
     * @throws RepositoryException
     */
	public void setRepository(ArtifactId repositoryId) throws RepositoryException {
		setProperty(PropertyKey.REPOSITORY_ID, repositoryId.getIdString());
	}

    /**
     *
     * @param type The type of asset, which is within the asset domain
     * @throws RepositoryException
     */
	public void setType(Type type) throws RepositoryException {
		setProperty(PropertyKey.ASSET_TYPE, type.getKeyword());
	}

	/**
	 *
	 * @param id ArtifactId is case sensitive
	 * @throws RepositoryException
	 */
	public void setId(ArtifactId id) throws RepositoryException {		
		setPropertyTemp(PropertyKey.ASSET_ID, id.getIdString());
		this.id = id;
	}

	/**
	 * Temporary update. This method does not flush contents immediately.
	 * @param key The property key
	 * @param value The value
	 * @throws RepositoryException
	 */
	private void setPropertyTemp(PropertyKey key, String value) throws RepositoryException {
		properties.setProperty(key.toString(), value);
	}

    /**
     * Set a property using the {@link gov.nist.hit.ds.repository.api.PropertyKey} enumeration using the Java Properties API.
     * @param key The property key
     * @param value The value
     * @throws RepositoryException
     */
	@Override
	public void setProperty(PropertyKey key, String value) throws RepositoryException {
        Parameter p = new Parameter();
        p.setDescription("key");
        p.assertNotNull(key);
        p.setDescription("value");
        p.assertNotNull(value);

        setProperty(key.toString(), value);
	}

	/**
	 * An alternative to {@link SimpleAsset#setProperty(PropertyKey,String)} 
	 * in cases where PropertyKeyLabel is not yet defined
     * @param key The property key
     * @param value The value
     */
	public void setProperty(String key, String value) throws RepositoryException {
        Parameter p = new Parameter();
        p.setDescription("key");
        p.assertNotNull(key);
        p.setDescription("value");
        p.assertNotNull(value);

        properties.setProperty(key, value);
        if (autoFlush) flush(getPropFile());
	}

    /**
     * Get a property based on the key, which cannot be null.
     * @param key The property key
     * @return String 
     */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

    /**
     * Get a property based on the key, which cannot be null.
     * @param key The property key
     * @return String
     */
	@Override
	public String getProperty(PropertyKey key) {
		return properties.getProperty(key.toString());
	}

    /**
     * Gets the base file, that is just the name part without any additional naming-extensions following the base file name.
     * @param assetId The asset Id
     * @return The partial file path, which is not readily usable.
     * @throws RepositoryException
     */
	File getAssetBaseFile(ArtifactId assetId) throws RepositoryException {
		
		// get repository source from property
		// two cases 
		// 1) set on new repos.createasset
		// 2) get asset
		
		return new File(getBasePath(assetId) + File.separator + assetId.getIdString());
	}

    /**
     * Gets the path up till the repository or the parent folder, whichever is applicable.
     * @param assetId The asset Id
     * @return File
     * @throws RepositoryException
     */
	private File getBasePath(ArtifactId assetId) throws RepositoryException {
		
		if (getPath()!=null) {
			return getPath().getParentFile();
		} else
			return getReposDir();
			
	}

    /**
     * Gets the repository root.
     * @return File
     * @throws RepositoryException
     */
	private File getReposDir() throws RepositoryException {
		return new File(Configuration.getRepositoriesDataDir(getSource()).toString() , getRepository().getIdString());
	}


    /**
     * Gets the Java properties object
     * @return Properties
     * @throws RepositoryException
     */
	@Override
	public Properties getProperties() throws RepositoryException {
		return this.properties;
	}

    /**
     * Updates the property
     * @param displayName The display name
     *
     * @throws RepositoryException
     */
	@Override
	public void updateDisplayName(String displayName)
			throws RepositoryException {
		setProperty(PropertyKey.DISPLAY_NAME,displayName);
	}




    /**
     * Updates the property
     * @param expirationDate in HL7 2.4 format: "YYYY[MM[DD]]"
     *
     * @throws RepositoryException
     */
	@Override
	public void updateExpirationDate(String expirationDate)
			throws RepositoryException {
		setProperty(PropertyKey.EXPIRATION_DATE,expirationDate);
	}

    /**
     * Gets the property
     * @return String
     * @throws RepositoryException
     */
	@Override
	public String getDisplayName() throws RepositoryException {
		return getProperty(PropertyKey.DISPLAY_NAME);		
	}

    /**
     * Gets the property
     * @return String
     * @throws RepositoryException
     */
	@Override
	public String getDescription() throws RepositoryException {
		return getProperty(PropertyKey.DESCRIPTION);		
	}

	/**
	 * ArtifactId is case sensitive
	 */
	@Override
	public ArtifactId getId() throws RepositoryException {
		// return new SimpleId(getProperty(PropertyKey.ASSET_ID));
		
		if (id==null) {
			id = new SimpleId(getProperty(PropertyKey.ASSET_ID));
            id.setUserFriendlyName(getName());
		} /* else if (!id.getIdString().equals(getProperty(PropertyKey.ASSET_ID))) {
			id = new SimpleId(getProperty(PropertyKey.ASSET_ID));
		} */

		return id;
	}

    /**
     * Gets the asset name.
     * @return
     * @throws RepositoryException
     */
    @Override
    public String getName() throws RepositoryException {
       return getProperty(PropertyKey.ASSET_NAME);
    }

    /**
     * Sets the asset name.
     *
     * @param name
     * @throws gov.nist.hit.ds.repository.api.RepositoryException
     */
    @Override
    public void setName(String name) throws RepositoryException {
        Parameter p = new Parameter("name");
        p.assertNotNull(name);

        setProperty(PropertyKey.ASSET_NAME, name);
    }


    /**
     * Gets the asset domain type, as specified in the {@code types} folder in the repository source.
     * @return Type
     * @throws RepositoryException
     */
	@Override
	public Type getAssetType() throws RepositoryException {		
		String type = getProperty(PropertyKey.ASSET_TYPE);
		if (type != null) {
			return new SimpleType(type);
		} else
			return null;
	}

    /**
     * Gets the expiration date if available
     * @return String
     * @throws RepositoryException
     */
	@Override
	public String getExpirationDate() throws RepositoryException {		
		return getProperty(PropertyKey.EXPIRATION_DATE);
	}

    /**
     *
     * @return String
     * @throws RepositoryException
     */
	@Override
	public String getMimeType() throws RepositoryException {		
		return getProperty(PropertyKey.MIME_TYPE);
	}


    /**
     *
     * @param createdDate in HL7 2.4 format: "YYYY[MM[DD]]"
     *
     * @throws RepositoryException
     */
	@Override
	public void setCreatedDate(String createdDate)
			throws RepositoryException {
		// Without flush
		setPropertyTemp(PropertyKey.CREATED_DATE, createdDate);
						
	}

    /**
     *
     * @return A string containing date in HL7 2.4 format
     * @throws RepositoryException
     */
	@Override
	public String getCreatedDate() throws RepositoryException {
		return getProperty(PropertyKey.CREATED_DATE);		
	}

    /**
     * Sets the display order of the asset for sorting purposes
     * @param order The display order
     * @throws RepositoryException
     */
	@Override
	public void setOrder(int order) throws RepositoryException {
		setProperty(PropertyKey.DISPLAY_ORDER,Integer.toString(order));
	}

    /**
     * When the mimeType changes, the previous content file is removed from the repository.
     * @param mimeType The mimeType
     * @throws RepositoryException
     */
	@Override
	public void setMimeType(String mimeType) throws RepositoryException {
		
		// Cleanup any previous content file to avoid confusion from the old content file based on previous (or the lack of) mimeType value
		File oldContentFile = getContentFile();
		String oldMimeType = getMimeType();
		
		if (mimeType==null) {
			
			// Case 1. mimeType can be null			
			setProperty(PropertyKey.MIME_TYPE, null);
			
		} else if (oldMimeType==null || !mimeType.equalsIgnoreCase(oldMimeType)) { 
		
			// Case 2. non-null mimeType
			setProperty(PropertyKey.MIME_TYPE, mimeType.toLowerCase());			
		}
		
		// Remove OLD content file in both cases
		deleteContent(oldContentFile);

	}

    /**
     * Gets the display order of the asset.
     * @return String
     * @throws RepositoryException
     */
	@Override
	public String getOrder() throws RepositoryException {
		return getProperty(PropertyKey.DISPLAY_ORDER);
	}

    /**
     * For use with parent-child relationship.
     * @param id The id
     * @throws RepositoryException
     */
	@Override
	public void setParentId(String id) throws RepositoryException {
		setProperty(PropertyKey.PARENT_ID, id);
	}


    /**
     *
     * @param description The asset description
     * @throws RepositoryException
     */
	@Override
	public void updateDescription(String description)
			throws RepositoryException {
		if (description!=null && !"".equals(description)) {
			setProperty(PropertyKey.DESCRIPTION,description);
		}
	}

    /**
     *
     * @return The artifact
     * @throws RepositoryException
     */
	@Override
	public ArtifactId getRepository() throws RepositoryException {
		return new SimpleId(getProperty(PropertyKey.REPOSITORY_ID));
	}

    /**
     * Gets the content file associated with the asset. There can be only one physical file at the moment. It is possible to use a Zip archive for bundling files.
     * @return File
     * @throws RepositoryException
     */
	@Override
	public File getContentFile() throws RepositoryException {
		return getContentFile(null);
	}

    /**
     *
     * @param part Part of the file
     * @return File The content file
     * @throws RepositoryException
     */
	@Override	
	public File getContentFile(File part) throws RepositoryException {
		String assetContentFilePart = null;
		File initialContentFile = null;
		File finalContentFile = null;
		
		 if (part == null && getContentPath()!=null) {
			 initialContentFile = getContentPath();
		} 
		if (part!=null) {
			assetContentFilePart = part.toString();
		} else if (getPath()!=null) {
			assetContentFilePart = getPath().getParent() + File.separator + FolderManager.getAssetIdFromFilename(getPath().getName());
		} else {
			String names[] = new String[] {getDisplayName() ,getDescription()};
			assetContentFilePart = new FolderManager().getFile(getReposDir(), names, getId())[1].toString();						
		}
		
		String[] ext = getContentExtension();
		if (Configuration.CONTENT_TEXT_EXT.equals(ext[0])) {			
				finalContentFile = new File(assetContentFilePart + Configuration.DOT_SEPARATOR + ext[2]);			
		} else {
			try {
				finalContentFile = new File(assetContentFilePart + Configuration.DOT_SEPARATOR + Configuration.CONTENT_FILE_EXT);
			} catch (Exception e) {
				// content may not exist
			} 
		}
		
		if (initialContentFile!=null && finalContentFile!=null) {
			if (!initialContentFile.equals(finalContentFile)) {
				return finalContentFile;
			} else 
				return initialContentFile;
		} else if (initialContentFile!=null) {
			return initialContentFile;
		} else if (finalContentFile!=null) {
			return finalContentFile;
		}
		
		return null;
		
	}

    /**
     * Indicates whether a content file is associated with this asset and accessible.
     * @return boolean
     * @throws RepositoryException
     */
	@Override	
	public boolean hasContent() throws RepositoryException {
		File f = getContentFile();
	
		if (f!=null) {
			if (f.exists()) {
				setContentPath(f);
				return true;
			} else {
				logger.fine("Content file does not exist: " + f.toString());
			}
		}
		return false;
	}

    /**
     *
     * @return byte[]
     * @throws RepositoryException
     */
	@Override
	public byte[] getContent() throws RepositoryException {

		/* Is this still needed?
		if (properties.size()==0) {
			this.load(getId()); // , getAssetBaseFile(getId())
		}
		*/
		
		if (!loadContentAttempted && hasContent()) {
			loadContent(getContentFile());
		}
		
		return content;
	}

    /**
     * Updates the content file using the existing mimeType. Use {@link gov.nist.hit.ds.repository.simple.SimpleAsset#setContent(String, String)} to reset both the content and the mimeType.
     * @param content raw data
     *
     * @throws RepositoryException
     */
	@Override
	public void updateContent(byte[] content) throws RepositoryException {
		this.content = content;
		if (autoFlush) flush(getContentFile()); // getPropFile()
	}

    /**
     * Set content.
     * @param content Text content
     * @param mimeType The mimeType
     * @throws RepositoryException
     */
	@Override
	public void setContent(String content, String mimeType) throws RepositoryException {
		this.content = content.getBytes();
		setMimeType(mimeType);
	}

    	/*
	@Override
	public Asset addChild(Id assetId) throws RepositoryException {
		Id repositoryId = getRepository();   // this does not support cross-repository linking
		Repository repository = new RepositoryFactory(getSource()).getRepository(repositoryId);
		SimpleAsset asset = (SimpleAsset) repository.getAsset(assetId);
		if (assetId.isEqual(getId()))
			throw new RepositoryException(RepositoryException.CIRCULAR_OPERATION + " : " +
					"trying to create parent relationship between asset [" + assetId.getIdString() +
					"] and itself (repository [" + repositoryId.getIdString() + "]");

		return addChild(asset);
	}
	*/


    /**
     * Associates a child asset to the parent asset. At this point if the parent is a single-file, it is automatically converted to a folder on the filesystem. The properties of the parent are nested inside the parent folder, not at the same-level.
     * @param asset The child asset
     * @return Asset
     * @throws RepositoryException
     */
	@Override
	public Asset addChild(Asset asset) throws RepositoryException {
		
		Parameter p = new Parameter();
		p.setDescription("Asset");
		p.assertNotNull(asset);

        if (asset.getPropFile()!=null && asset.getPropFile().equals(getPropFile())) {
            throw new RepositoryException(RepositoryException.CIRCULAR_OPERATION + " : " +
                    "trying to create parent relationship between asset [" + asset.getPropFile() +
                    "] and itself ([" + getPropFile() + "]");
        }
				
		String folderName = FolderManager.getSafeName(new String[]{
				getProperty(PropertyKey.DISPLAY_NAME)
				,getProperty(PropertyKey.DESCRIPTION)
				,getProperty(PropertyKey.ASSET_ID)
				,FolderManager.LOST_AND_FOUND
		});

		File[] parentFolder = new FolderManager().makeFolder(this, folderName);
		
		
		File srcPropFile = asset.getPropFile();
		File srcConFile = asset.getContentFile();			

		File[] txSrc = new File[]{srcPropFile, srcConFile};		

		File dstPropFile = new File(parentFolder[0] + File.separator + asset.getPropFile().getName());
		File dstConFile = new File(parentFolder[0] + File.separator + asset.getContentFile().getName());
		File[] txDst = new File[]{dstPropFile, dstConFile};
		
		asset.setParentId(getPropFileRelativePart());
		File[] loc = FolderManager.moveChildToParent(txSrc, txDst);
		asset.setPath(loc[0]);
		asset.setContentPath(loc[1]);
	
		return asset;
	}

    /**
     * Retrieves a child by name. If there is more than one by the same name, the first one found is retrieved.
     * @param name see {@link gov.nist.hit.ds.repository.simple.SimpleRepository#createNamedAsset(String, String, Type, String)}
     * @return
     * @throws RepositoryException
     */
    @Override
    public Asset getChildByName(String name) throws RepositoryException {
        File[] assetFileByName = new FolderManager().getAssetFileByName(name, getPropFile().getParentFile());
        Asset child = new SimpleAsset(getSource());

        try {
            logger.info("getAssetFileByName return 0 : " +  assetFileByName[0]);
            logger.info("getAssetFileByName return 1 : " +  assetFileByName[1]);
            FolderManager.loadProps(child.getProperties(), assetFileByName[0]);
            child.setContentPath(assetFileByName[1]);
        } catch (Exception e) {
            throw new RepositoryException(RepositoryException.UNKNOWN_ID + " : " +
                    "properties cannot be loaded: [" +
                    (assetFileByName==null?"null":assetFileByName[0])
                    + "]", e);
        }

        return child;
    }
	


	/**
	 * Deletes this Asset. If it is a parent folder, then all items in that parent folder will be deleted as well.
	 * If the asset is a simple child asset, then it removes both the property file and the content file from the repository directory.
	 * Supports SimpleRepository.deleteAsset(id)
	 * @throws RepositoryException
	 */
    @Override
	public void deleteAsset() throws RepositoryException {
		File assetPropFile = getPropFile();
		
		if (assetPropFile.exists()) {

            if (new FolderManager().doesParentAssetFolderExist(assetPropFile)) {
                logger.fine("Deleting parent folder:"+ assetPropFile.toString());
                try {
                    File parentFolder = assetPropFile.getParentFile();
                    FileUtils.cleanDirectory(parentFolder);
                    parentFolder.delete();
                } catch (IOException ioEx) {
                    throw  new RepositoryException(RepositoryException.IO_ERROR +  ": Could not delete asset folder!" + assetPropFile.toString() + " : " + ioEx.toString());
                }
            } else {
                logger.fine("Deleting property file:"+ assetPropFile.toString());
                if (!assetPropFile.delete()) {
                    logger.warning("Delete failed: " +assetPropFile.toString());
                }
                deleteContent(getContentFile());
            }
		}
	}

    /**
     *
     * @param assetContentFile The content file
     * @throws RepositoryException
     */
	private void deleteContent(File assetContentFile) throws RepositoryException {

		if (assetContentFile!=null && assetContentFile.exists()) {
			logger.fine("Deleting content file:"+ assetContentFile.toString());
			if (!assetContentFile.delete()) {
                logger.warning("File delete failed:" +assetContentFile.toString());
            }
		}		
	}

    /**
     *
     * @return AssetIterator
     * @throws RepositoryException
     */
	@Override
	public AssetIterator getAssets() throws RepositoryException {
		
		SimpleRepository repos = new SimpleRepository(getRepository());
		repos.setSource(getSource());
		
		return new SimpleAssetIterator(repos);
	}

    /**
     *
     * @param assetType The asset type
     * @return AssetIterator
     * @throws RepositoryException
     */
	@Override
	public AssetIterator getAssetsByType(Type assetType)
			throws RepositoryException {
		SimpleRepository repos = new SimpleRepository(getRepository());
		repos.setSource(getSource());
		
		return new SimpleAssetIterator(repos, assetType);
	}

    /**
     *
     * @return boolean
     */
	@Override
	public boolean isAutoFlush() {
		return autoFlush;
	}

    /**
     *
     * @param autoFlush A flag to indicate that all property level updates are immediately flushed to disk.
     */
	@Override
	public void setAutoFlush(boolean autoFlush) {
		this.autoFlush = autoFlush;
	}

    /**
     * The asset path off the repository root.
     * @return String
     * @throws RepositoryException
     */
	@Override
	public String getPropFileRelativePart() throws RepositoryException {
		return getPropFile(this.getId()).toString().replace(getReposDir().toString(), "").replace("\\", "/");
	}

    /**
     * The absolute path of an asset.
     * @return File
     * @throws RepositoryException
     */
	@Override
	public File getPropFile() throws RepositoryException {
		return getPropFile(this.getId());
	}

    /**
     *
     * @param id ArtifactId is case sensitive
     * @return File
     * @throws RepositoryException
     */
	@Override
	public File getPropFile(ArtifactId id) throws RepositoryException {
		
		if (!getSource().getLocation().exists())
			throw new RepositoryException(RepositoryException.CONFIGURATION_ERROR + " : " +
					"source directory [" + getSource().getLocation().toString() + "] does not exist");		
		
		if (getPath()!=null) {
			return getPath();
		}

        String firstPrefName = getDisplayName();

		// New file
        if (getId()!=null && !(getId().getUserFriendlyName()==null)) {
            String userFriendlyName =  getId().getUserFriendlyName();
            if (!"".equals(userFriendlyName)) {
                firstPrefName = userFriendlyName;
            }
        }
		
		String names[] = new String[] {firstPrefName, getDescription()};

		File[] assetPath = new FolderManager().getFile(getReposDir(), names, null);

		if (assetPath!=null && assetPath[0]!=null) {
			setContentPath(getContentFile(assetPath[1])); // base path w/o extension
			if (assetPath[3]!=null) { // incremented id due to duplicate file conflict
				setId(new SimpleId(assetPath[2].toString()));
			}
			return assetPath[0];
		} 
		throw new RepositoryException(RepositoryException.NULL_ARGUMENT);
		// Not safe to assume the base dir residence anymore after the introduction of folder based storage
		// return new File(getAssetBaseFile(id) + "." + Configuration.PROPERTIES_FILE_EXT);		
	}


	String partTwo(String in, String separator) {
		String[] parts = in.split(separator);
		if (parts == null || parts.length < 2) return in;
		return parts[1];
	}

	String partOne(String in, String separator) {
		String[] parts = in.split(separator);
		if (parts == null || parts.length < 2) return in;
		return parts[0];
	}

	/**
	 * Load an asset off the disk to memory.
	 * @param assetId The asset Id
	 * @return SimpleAsset
	 * @throws RepositoryException
	 */
	public SimpleAsset load(ArtifactId assetId) throws RepositoryException {
		Parameter p = new Parameter();
		p.setDescription("assetId");
		p.assertNotNull(assetId);
		
		File[] assetPath = new FolderManager().getFile(getReposDir(), new String[] {assetId.getIdString()}, assetId);

		
		if (assetPath[0]!=null) {
			setPath(assetPath[0]);
		} else {
			throw new RepositoryException(RepositoryException.IO_ERROR + " : " + 
					"File not found for assetId: [" +
					assetId.getIdString()
					+ "]");
		}

		properties.clear();
		try {
			FolderManager.loadProps(properties, assetPath[0]);
			setContentPath(getContentFile(assetPath[1])); // w/o ext, this needs to happen after prop load to get the mimeType
		} catch (Exception e) {
			throw new RepositoryException(RepositoryException.UNKNOWN_ID + " : " + 
					"properties cannot be loaded: [" +
					assetPath[0].toString()
					+ "]", e);
		}
		
		return this;
	}	
	

	/**
	 * @param assetContentFile The content file
	 */
	private void loadContent(File assetContentFile) {
			try {				
				loadContentAttempted = true;
				content = FileUtils.readFileToByteArray(assetContentFile);
			} catch (Exception e) {
				logger.info("Content load fail <" + assetContentFile.toString()  +">: " + e.toString());
			} 		
	}

    /**
     * Returns the file extension based on the {@code mimeType}
     * @return String[]
     */
	@Override
	public String[] getContentExtension() {
		String[] sPart = new String[]{"","",""};
		String mimeType = getProperty(PropertyKey.MIME_TYPE);	
		if (mimeType != null && mimeType.startsWith("text/")) {
			sPart[0] = "text";
			sPart[1] = partTwo(mimeType, "\\/");
			if (sPart[1].equals("*")||sPart[1].equals("plain")) { 
				sPart[2] = "txt";
			} else {
				sPart[2] = sPart[1];
			}
			
		} else {
			sPart[2] = Configuration.CONTENT_FILE_EXT;
		}
		return sPart;
	}

    /**
     * Immediately updates the asset on disk.
     * @see {@link SimpleAsset#flush(java.io.File)}
     * @throws RepositoryException
     */
	public void flush() throws RepositoryException {
		File path = getPropFile();
		setPath(path);
		flush(path);
	}

    /**
     * Updates the file on disk.
     * @param propFile The property file
     * @throws RepositoryException
     */
	@Override
	public void flush(File propFile) throws RepositoryException {					
		if (!getSource().getLocation().exists())
		throw new RepositoryException(RepositoryException.CONFIGURATION_ERROR + " : " +
				"source directory [" + getSource().getLocation().toString() + "] does not exist");

//		if (getProperty(PropertyKey.UPDATED_DATE)==null) {
//			assertUniqueName(propFile);
//		}
				
		try {			
			setPropertyTemp(PropertyKey.UPDATED_DATE, new Hl7Date().now());	
			setExipration();		
			
			FileWriter writer = new FileWriter(propFile);
			properties.store(writer, "");
			writer.close();
			if (content != null) {
				File contentFile = getContentFile();
				String[] ext = getContentExtension();
				if (Configuration.CONTENT_TEXT_EXT.equals(ext[0])) {					
					Io.stringToFile(contentFile, new String(content));
				} else {
					OutputStream os = new FileOutputStream(contentFile);
					os.write(content);
					os.close();
				}
			}
		} catch (IOException e) {
			throw new RepositoryException(RepositoryException.IO_ERROR, e);
		}
	}

	/**
	 * @param propFile
	 * @throws RepositoryException
	 */
	/*
	private void assertUniqueName(File propFile) throws RepositoryException {
		// Make sure the file name is not null and another file doesn't already exist with the same name in the case of a new asset only		
		if (propFile!=null) {
			if (propFile.exists()) 
				throw new RepositoryException("An asset property file already exists with the same name: " + propFile.toString());
		} else {
			throw new RepositoryException("Naming convention error: propFile name is null. ");
		}
		return;
	}
	*/

	/**
	 * 
	 */
	private void setExipration() {
		try {
			if (getCreatedDate()!=null) {
				SimpleDateFormat sdf = new SimpleDateFormat(Hl7Date.parseFmt);
				sdf.parse(getCreatedDate());
				Calendar c = sdf.getCalendar();
				if (c!=null) {
					Type t = getAssetType(); 
					if (t != null) {			
						TypeIterator it;
			
							it = new SimpleTypeIterator(this.getSource().getLocation(), t);
							if (it.hasNextType()) {				
								Type assetType = it.nextType();
								String lifetime = assetType.getLifetime();
								if (lifetime!=null) {
										Integer days = Integer.parseInt(lifetime.substring(0,lifetime.indexOf(" days")));
										if (days!=null) {
											logger.fine("asset " + getPropFile() +  " lifetime: " + days);
											if (getExpirationDate()==null) {
												c.add(Calendar.DATE, days);
												Date expr = c.getTime();
												setPropertyTemp(PropertyKey.EXPIRATION_DATE, sdf.format(expr));												
											}
										}
											
								}
								
							}
					}			
				}
		
			}
		} catch (Exception e) {
			logger.fine("Error while calculating expiration date: " + e.toString());
			// This is a non-blocking method
		}
	}




    /**
     * Returns the display name or Id string, whichever is available first.
     * @return String
     */
	@Override
	public String toString()  {

		String name = null;
		try {
			name = getDisplayName();
			if (name!=null && !"".equals(name)) {
				return name;
			} else {
				name = getId().getIdString();
			}
		} catch (RepositoryException e) {
			;
		}
		return name;
			
	
	}

    /**
     * Determines if the mimeType startsWith "text".
     * @return boolean
     * @throws RepositoryException
     */
	public boolean isText() throws RepositoryException {
		String mimeType = getMimeType();
		return (mimeType!=null && mimeType.startsWith("text/"));
	}

    /**
     *
     * @return RepositorySource
     */
	@Override
	public RepositorySource getSource() {
		return source;
	}

    /**
     *
     * @param source RepositorySource
     */
	@Override
	public void setSource(RepositorySource source) {
		this.source = source;
	}

    /**
     *
     * @return File
     */
	public File getPath() {
		return path;
	}

    /**
     *
     * @param path The file path
     */
	public void setPath(File path) {
		this.path = path;
	}

    /**
     *
     * @return File
     */
	public File getContentPath() {
		return contentPath;
	}

    /**
     *
     * @param contentPath The content file path
     */
	@Override
	public void setContentPath(File contentPath) {
		this.contentPath = contentPath;
	}
	
}
