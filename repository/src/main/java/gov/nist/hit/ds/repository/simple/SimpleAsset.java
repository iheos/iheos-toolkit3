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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public class SimpleAsset implements Asset, Flushable {	
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
		if (autoFlush) flush();
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
		
		return new File(Configuration.getRepositoriesDataDir(getSource()) + File.separator + getRepository() + File.separator + assetId.getIdString());
	}

	File getPropertyFile() throws RepositoryException {
		return new File(getAssetBaseFile(this.getId()).toString() + "." + Configuration.PROPERTIES_FILE_EXT);
	}
		
	File getContentFile() throws RepositoryException {
		return new File(getAssetBaseFile(this.getId()).toString() + "." + Configuration.CONTENT_FILE_EXT);
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
	public byte[] getContent() throws RepositoryException {

		if (!loadContentAttempted) {
			this.load(this.getId(), getAssetBaseFile(this.getId()));
		}

		return content;
	}

	@Override
	public void updateContent(byte[] content) throws RepositoryException {
		this.content = content;
		if (autoFlush) flush();
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
		asset.setProperty(PropertyKey.PARENT_ID, getId().getIdString());
		if (autoFlush) flush();
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
		File assetPropFile = getPropertyFile();
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
		
		if (!getSource().getLocation().exists())
			throw new RepositoryException(RepositoryException.CONFIGURATION_ERROR + " : " +
					"source directory [" + getSource().getLocation().toString() + "] does not exist");		
		return new File(getAssetBaseFile(this.getId()).toString() + "." + Configuration.PROPERTIES_FILE_EXT);
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
	 * @param assetBaseFile - path of asset without file extension
	 * @param repositoryId
	 * @return
	 * @throws RepositoryException
	 */
	public SimpleAsset load(Id assetId, File assetBaseFile) throws RepositoryException {
		File assetPropFile = new File(assetBaseFile.toString() + "." + Configuration.PROPERTIES_FILE_EXT);
		File assetContentFile = new File(assetBaseFile.toString() + "." + Configuration.CONTENT_FILE_EXT);
		properties = new Properties();
		try {
			FileReader fr = new FileReader(assetPropFile);
			properties.load(fr);
			fr.close();
		} catch (Exception e) {
			throw new RepositoryException(RepositoryException.UNKNOWN_ID + " : " + 
					"properties cannot be loaded: [" +
					assetBaseFile.toString()
					+ "]", e);
		}

		String[] ext = getContentExtension();
		if (Configuration.CONTENT_TEXT_EXT.equals(ext[0])) {
			try {
				loadContentAttempted = true;
				content = FileUtils.readFileToByteArray(getContentFile(ext[2]));
			} catch (RepositoryException e) {
				// content may not exist
			} catch (IOException e) {
				// content may not exist
			}
		} else {
			try {
				loadContentAttempted = true;
				content = FileUtils.readFileToByteArray(assetContentFile);
			} catch (Exception e) {
				// content may not exist
			} 
		}
		return this;
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

	@Override
	public void flush() throws RepositoryException {
		autoFlush = true;
		try {			
			setPropertyTemp(PropertyKey.MODIFIED_DATE, new Hl7Date().now());	
			setExipration();		
			
			FileWriter writer = new FileWriter(getPropFile());
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
											System.out.println("lifetime: " + days);
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
