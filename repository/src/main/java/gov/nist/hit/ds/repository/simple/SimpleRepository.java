package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.Parameter;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.api.Type;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SimpleRepository extends BaseRepository implements Flushable {
	private static final long serialVersionUID = 7941866267155906518L;

	boolean autoFlush = true;
	boolean isNew;

	/**
	 * Open an existing repository.
	 * @param root - filesystem directory that holds the repository contents
	 * @return 
	 * @throws RepositoryException
	 */
	public SimpleRepository(Id id) throws RepositoryException {
		super(id);
	}

	/**
	 * Create new Repository.
	 * @throws RepositoryException
	 */
	public SimpleRepository() throws RepositoryException {
		super();
	}
	
	/**
	 * Create new named Repository.
	 * @throws RepositoryException
	 */
	public SimpleRepository(String name) throws RepositoryException {
		super(name);
	}

	
	
	public boolean isAutoFlush() {
		return autoFlush;
	}

	/**
	 * Control whether repository objects are flushed to disk after every set method.
	 * AutoFlush == false requires that eventually the flush() method be called to force
	 * the data to disk.
	 * AutoFlush is reset to true at next flush.
	 * @param autoFlush
	 */
	public void setAutoFlush(boolean autoFlush) {
		this.autoFlush = autoFlush;
	}


	@Override
	public void setDisplayName(String displayName)
			throws RepositoryException {
//		load();
		properties.setProperty("DisplayName", displayName);
		if (autoFlush)
			flush();
	}

	@Override
	public void setDescription(String description)
			throws RepositoryException {
//		load();
		if (description!=null && !"".equals(description)) {
			properties.setProperty("description", description);
			if (autoFlush)
				flush();
		}
	}

//	@Override
	public Asset createAsset(String displayName, String description,
			Type assetType) throws RepositoryException {

		Parameter param = new Parameter();
		param.assertEquals(Access.RW_EXTERNAL, this.getSource().getAccess());
		
		param.setDescription("type parameter cannot be null");
		param.assertNotNull(assetType);
		
		param.setDescription("Cannot find type <" + assetType.getKeyword() + ">");
		param.assertEquals(
				new SimpleTypeIterator(Configuration.getRepositorySrc(Access.RW_EXTERNAL),assetType,SimpleType.ASSET).hasNextType()
				,new Boolean(true));

		
		SimpleAsset a = new SimpleAsset(getSource());
		a.setAutoFlush(false);
		a.setRepository(getId());
		a.setType(assetType);
		a.setId(new IdFactory().getNewId());
		a.updateDisplayName(displayName);
		a.updateDescription(description);
		a.setAutoFlush(true);
		a.flush();
		return a;
	}
	
	@Override
	public Asset createNamedAsset(String displayName, String description,
			Type assetType, String name) throws RepositoryException {

		Parameter param = new Parameter();
		param.setDescription("External source");
		param.assertEquals(Access.RW_EXTERNAL, this.getSource().getAccess());
		
		param.setDescription("Asset name conflicts with repository base name.");
		param.assertParam(name);
		param.assertEquals(name.equals(Configuration.REPOSITORY_PROP_FILE_BASENAME), new Boolean(false));
		
		param.setDescription("Asset Type cannot be null");
		param.assertNotNull(assetType);
		
		param.setDescription("Cannot find type <" + assetType.getKeyword() +">");
		param.assertEquals(
				new SimpleTypeIterator(Configuration.getRepositorySrc(Access.RW_EXTERNAL),assetType,SimpleType.ASSET).hasNextType()
				,new Boolean(true));

		SimpleAsset a = new SimpleAsset(getSource());	
		a.setAutoFlush(false);
		a.setRepository(getId());
		a.setType(assetType);
		a.setId(new SimpleId(name));
		a.updateDisplayName(displayName);
		a.updateDescription(description);
		a.setAutoFlush(true);		
		a.flush();

		return a;
	}
	
	@Override
	public void deleteAsset(Id assetId) throws RepositoryException {

		Parameter param = new Parameter("delete Id");
		param.assertNotNull(assetId);
		param.setDescription("delete external Id:" + assetId.getIdString());
		param.assertEquals(Access.RW_EXTERNAL, this.getSource().getAccess());
		
//		load();
//		try {
		SimpleAsset a = (SimpleAsset) getAsset(assetId);
			if (a!=null) {
				a.deleteAsset();
			}
			
//		} catch (Exception e) {
//
//		}
	}
	
	public void flush() throws RepositoryException {
		flush(getRepositoryPropFile());
	}

	public void flush(File propFile) throws RepositoryException {

		Parameter param = new Parameter();
		param.setDescription("External access");
		param.assertEquals(Access.RW_EXTERNAL, this.getSource().getAccess());
		
		param.setDescription("Repository propFile" + propFile);
		param.assertNotNull(propFile);
		
		autoFlush = true;
		try {
			root.mkdirs();
			FileWriter writer = new FileWriter(propFile);
			properties.store(writer, "");
			writer.close();
		} catch (IOException e) {
			throw new RepositoryException(RepositoryException.IO_ERROR, e);
		}
		isNew = false;
	}


}
