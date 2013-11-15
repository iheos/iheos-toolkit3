package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.api.TypeIterator;
import gov.nist.hit.ds.utilities.datatypes.Hl7Date;
import gov.nist.hit.ds.utilities.io.Io;

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

import org.apache.commons.io.FileUtils;

public class SimpleAsset implements Asset, Flushable {
	private static Logger logger = Logger.getLogger(SimpleAsset.class.getName());
	String classNameForSerializable;
	Properties properties = new Properties();
	byte[] content = null;
	boolean loadContentAttempted = false;
	boolean autoFlush = true;
	
	transient boolean indexable = false;
	transient RepositorySource source;
	
	public SimpleAsset(RepositorySource source) throws RepositoryException {
		super();
		setCreatedDate(new Hl7Date().now());
		setSource(source);
	}

	public void setRepository(Id repositoryId) throws RepositoryException {
		setProperty(PropertyKey.REPOSITORY_ID, repositoryId.getIdString());
	}

	public void setType(Type type) throws RepositoryException {
		setProperty(PropertyKey.ASSET_TYPE, type.getKeyword());
	}

	public void setId(Id id) throws RepositoryException {
		setProperty(PropertyKey.ASSET_ID, id.getIdString());
	}

	/**
	 * Temporary update. This method does not flush contents immediately.
	 * @param key
	 * @param value
	 * @throws RepositoryException
	 */
	private void setPropertyTemp(PropertyKey key, String value) throws RepositoryException {
		properties.setProperty(key.toString(), value);
	}
	
	@Override
	public void setProperty(PropertyKey key, String value) throws RepositoryException {
		setProperty(key.toString(), value);
	}

	/**
	 * An alternative to {@link SimpleAsset#setProperty(PropertyKey,String)} 
	 * in cases where PropertyKeyLabel is not yet defined
	 */
	public void setProperty(String key, String value) throws RepositoryException { 
			properties.setProperty(key, value);		
			if (autoFlush) flush(getPropFile());		
	}



	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	@Override
	public String getProperty(PropertyKey key) {
		return properties.getProperty(key.toString());
	}

	File getAssetBaseFile(Id assetId) throws RepositoryException {
		
		// get repository source from property
		// two cases 
		// 1) set on new repos.createasset
		// 2) get asset
		
		return new File(getBasePath(assetId) + File.separator + assetId.getIdString());
	}
	
	private File getBasePath(Id assetId) throws RepositoryException {				
		File reposDir = getReposDir();
		
		try {			
			File[] assetPath = new FolderManager().findById(reposDir, assetId.getIdString(), null);
			return assetPath[0].getParentFile();
			
		} catch (Exception ex) {
			return reposDir;
		}
			
									
	}
		
	private File getReposDir() throws RepositoryException {
		return new File(Configuration.getRepositoriesDataDir(getSource()).toString()  + File.separator + getRepository().getIdString());
	}

	
	@Override
	public Properties getProperties() throws RepositoryException {
		return this.properties;
	}
		
	@Override
	public void updateDisplayName(String displayName)
			throws RepositoryException {
		setProperty(PropertyKey.DISPLAY_NAME,displayName);
	}


	@Override
	public void updateExpirationDate(String expirationDate)
			throws RepositoryException {
		setProperty(PropertyKey.EXPIRATION_DATE,expirationDate);
	}

	@Override
	public String getDisplayName() throws RepositoryException {
		return getProperty(PropertyKey.DISPLAY_NAME);		
	}

	@Override
	public String getDescription() throws RepositoryException {
		return getProperty(PropertyKey.DESCRIPTION);		
	}

	@Override
	public Id getId() throws RepositoryException {		
		return new SimpleId(getProperty(PropertyKey.ASSET_ID));
	}

	@Override
	public Type getAssetType() throws RepositoryException {		
		String type = getProperty(PropertyKey.ASSET_TYPE);
		if (type != null) {
			return new SimpleType(type);
		} else
			return null;
	}


	@Override
	public String getExpirationDate() throws RepositoryException {		
		return getProperty(PropertyKey.EXPIRATION_DATE);
	}

	@Override
	public String getMimeType() throws RepositoryException {		
		return getProperty(PropertyKey.MIME_TYPE);
	}


	@Override
	public void setCreatedDate(String createdDate)
			throws RepositoryException {
		// Without flush
		setPropertyTemp(PropertyKey.CREATED_DATE, createdDate);
						
	}

	@Override
	public String getCreatedDate() throws RepositoryException {
		return getProperty(PropertyKey.CREATED_DATE);		
	}
	
	@Override
	public void setOrder(int order) throws RepositoryException {
		setProperty(PropertyKey.DISPLAY_ORDER,Integer.toString(order));
	}

	@Override
	public void setMimeType(String mimeType) throws RepositoryException {
		setProperty(PropertyKey.MIME_TYPE, mimeType);
	}
	
	@Override
	public String getOrder() throws RepositoryException {
		return getProperty(PropertyKey.DISPLAY_ORDER);
	}

	@Override
	public void setParentId(Id id) throws RepositoryException {
		setProperty(PropertyKey.PARENT_ID, id.getIdString());
	}
	

	@Override
	public void updateDescription(String description)
			throws RepositoryException {
		if (description!=null && !"".equals(description)) {
			setProperty(PropertyKey.DESCRIPTION,description);
		}
	}

	@Override
	public Id getRepository() throws RepositoryException {
		return new SimpleId(getProperty(PropertyKey.REPOSITORY_ID));
	}
	@Override	
	public File getContentFile() throws RepositoryException {
	
		File assetContentFile = new File(getAssetBaseFile(this.getId()).toString() + "." + Configuration.CONTENT_FILE_EXT);
		
		String[] ext = getContentExtension();
		if (Configuration.CONTENT_TEXT_EXT.equals(ext[0])) {
			try {
				return getContentFile(ext[2]);
			} catch (RepositoryException e) {
				// content may not exist
			}
		} else {
			try {
				return assetContentFile;
			} catch (Exception e) {
				// content may not exist
			} 
		}
		
		return null;
		
	}

	@Override	
	public boolean hasContent() throws RepositoryException {
		File f = getContentFile();
	
		if (f!=null) {
			return f.exists();
		}
		return false;
	}

	@Override
	public byte[] getContent() throws RepositoryException {

		if (properties.size()==0) {
			this.load(getId()); // , getAssetBaseFile(getId())
		}
		if (!loadContentAttempted && hasContent()) {
			loadContent(getContentFile());
		}
		
		return content;
	}

	@Override
	public void updateContent(byte[] content) throws RepositoryException {
		this.content = content;
		if (autoFlush) flush(getPropFile());
	}

	@Override
	public void updateContent(String content, String mimeType) throws RepositoryException {
		this.content = content.getBytes();
		setProperty(PropertyKey.MIME_TYPE,mimeType);		
	}

	@Override
	public void addAsset(Id assetId) throws RepositoryException {
		Id repositoryId = getRepository();   // this does not support cross-repository linking
		Repository repository = new RepositoryFactory(getSource()).getRepository(repositoryId);
		SimpleAsset asset = (SimpleAsset) repository.getAsset(assetId);
		if (assetId.isEqual(getId())) 
			throw new RepositoryException(RepositoryException.CIRCULAR_OPERATION + " : " +
					"trying to create parent relationship between asset [" + assetId.getIdString() + 
					"] and itself (repository [" + repositoryId.getIdString() + "]");
	

		asset.setAutoFlush(true);
		asset.setParentId(getId());
		
		File parentFolder = new FolderManager().makeFolder(getReposDir(), getId().getIdString());
		
		File srcPropFile = asset.getPropFile();
		File srcConFile = asset.getContentFile();
		File[] txSrc = new File[]{srcPropFile, srcConFile};		

		File dstPropFile = new File(parentFolder + File.separator + asset.getPropFile().getName());
		File dstConFile = new File(parentFolder + File.separator + asset.getContentFile().getName());
		File[] txDst = new File[]{dstPropFile, dstConFile};
		
		FolderManager.moveChildToParent(txSrc, txDst);			
		
	}
	
	@Override
	public void removeAsset(Id assetId, boolean includeChildren)
			throws RepositoryException {
		throw new RepositoryException(RepositoryException.UNIMPLEMENTED);
	}

	/**
	 * Simple delete of this Asset - no recursion. Not part of the API. 
	 * Supports SimpleRepository.deleteAsset(id)
	 * @throws RepositoryException
	 */
	public void deleteAsset() throws RepositoryException {
		File assetPropFile = getPropFile();
		File assetContentFile = getContentFile();
		if (assetPropFile.exists()) {
			assetPropFile.delete();
			if (assetContentFile.exists()) {
				assetContentFile.delete();
			}
		}
		
	}

	@Override
	public AssetIterator getAssets() throws RepositoryException {
		
		SimpleRepository repos = new SimpleRepository(getRepository());
		repos.setSource(getSource());
		
		return new SimpleAssetIterator(repos);
	}

	@Override
	public AssetIterator getAssetsByType(Type assetType)
			throws RepositoryException {
		SimpleRepository repos = new SimpleRepository(getRepository());
		repos.setSource(getSource());
		
		return new SimpleAssetIterator(repos, assetType);
	}

	@Override
	public boolean isAutoFlush() {
		return autoFlush;
	}

	@Override
	public void setAutoFlush(boolean autoFlush) {
		this.autoFlush = autoFlush;
	}
	
	@Override
	public File getPropFile() throws RepositoryException {
		return getPropFile(this.getId());
	}
	
	@Override
	public File getPropFile(Id id) throws RepositoryException {
		
		if (!getSource().getLocation().exists())
			throw new RepositoryException(RepositoryException.CONFIGURATION_ERROR + " : " +
					"source directory [" + getSource().getLocation().toString() + "] does not exist");		
		
		String names[] = new String[] {getDisplayName() ,getDescription()};
		File[] assetPath = new FolderManager().findById(getReposDir(), id.getIdString(), names);

		if (assetPath!=null && assetPath[0]!=null) {
			return assetPath[0];
		} 
		throw new RepositoryException(RepositoryException.NULL_ARGUMENT);
		// Not safe to assume the base dir residence anymore after the introduction of folder based storage
		// return new File(getAssetBaseFile(id) + "." + Configuration.PROPERTIES_FILE_EXT);		
	}
	


	File getContentFile(String ext) throws RepositoryException {
		 				
		if (!getSource().getLocation().exists())
			throw new RepositoryException(RepositoryException.CONFIGURATION_ERROR + " : " +
					"source directory [" + getSource().getLocation().toString() + "] does not exist");

		return new File(getAssetBaseFile(this.getId()).toString()  + "." + ext);
	}

	String partTwo(String in, String separater) {
		String[] parts = in.split(separater);
		if (parts == null || parts.length < 2) return in;
		return parts[1];
	}

	String partOne(String in, String separater) {
		String[] parts = in.split(separater);
		if (parts == null || parts.length < 2) return in;
		return parts[0];
	}

	/**
	 * Load asset off disk into memory.
	 * @param assetId
	 * @param repositoryId
	 * @return
	 * @throws RepositoryException
	 */
	public SimpleAsset load(Id assetId) throws RepositoryException {
		File assetPropFile =  getPropFile(assetId); // new File(assetBaseFile.toString() + "." + Configuration.PROPERTIES_FILE_EXT);

		properties.clear();
		try {
			FolderManager.loadProps(properties, assetPropFile);
		} catch (Exception e) {
			throw new RepositoryException(RepositoryException.UNKNOWN_ID + " : " + 
					"properties cannot be loaded: [" +
					assetPropFile.toString()
					+ "]", e);
		}
		
		return this;
	}	
	

	/**
	 * @param assetContentFile
	 */
	private void loadContent(File assetContentFile) {
			try {				
				loadContentAttempted = true;
				content = FileUtils.readFileToByteArray(assetContentFile);
			} catch (Exception e) {
				logger.info("Content load fail <" + assetContentFile.toString()  +">: " + e.toString());
			} 		
	}
	
	
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
	
	public void flush() throws RepositoryException {
		flush(getPropFile());
	}

	@Override
	public void flush(File propFile) throws RepositoryException {
						
		if (getProperty(PropertyKey.UPDATED_DATE)==null)
			assertUniqueName(propFile);
				
		try {			
			setPropertyTemp(PropertyKey.UPDATED_DATE, new Hl7Date().now());	
			setExipration();		
			
			FileWriter writer = new FileWriter(propFile);
			properties.store(writer, "");
			writer.close();
			if (content != null) {
				String[] ext = getContentExtension();
				if (Configuration.CONTENT_TEXT_EXT.equals(ext[0])) {					
					Io.stringToFile(getContentFile(ext[2]), new String(content));
				} else {
					OutputStream os = new FileOutputStream(getContentFile());
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
											logger.fine("asset " + getId().getIdString() +  " lifetime: " + days);
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
			// This is a non-blocking method
		}
	}


	/**
	 * @return the indexable
	 */
	public boolean isIndexable() {
		return indexable;
	}

	/**
	 * @param indexable the indexable to set
	 */
	public void setIndexable(boolean indexable) {
		this.indexable = indexable;
	}

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
	
	public boolean isText() throws RepositoryException {
		String mimeType = getMimeType();
		return (mimeType!=null && mimeType.startsWith("text/"));
	}

	@Override
	public RepositorySource getSource() {
		return source;
	}

	@Override
	public void setSource(RepositorySource source) {
		this.source = source;
	}
	
}
