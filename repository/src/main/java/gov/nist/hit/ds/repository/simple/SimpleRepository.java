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

		Parameter p = new Parameter();
		p.assertEquals(Access.RW_EXTERNAL, this.getSource().getAccess());
		
		p.setDescription("type parameter cannot be null");
		p.assertNotNull(assetType);
		
		p.setDescription("Cannot find type <" + assetType.getKeyword() + ">");
		p.assertEquals(
				new SimpleTypeIterator(Configuration.getRepositorySrc(Access.RW_EXTERNAL),assetType,SimpleType.ASSET).hasNextType()
				,new Boolean(true));

		
		SimpleAsset a = new SimpleAsset(getSource());
		a.setAutoFlush(false);
		a.setRepository(getId());
		a.setType(assetType);
		a.setId(new IdFactory().getNewId());
		a.updateDisplayName(displayName);
		a.updateDescription(description);
		a.flush();
		return a;
	}
	
	@Override
	public Asset createNamedAsset(String displayName, String description,
			Type assetType, String name) throws RepositoryException {

		Parameter p = new Parameter();
		p.assertEquals(Access.RW_EXTERNAL, this.getSource().getAccess());
		p.assertParam(name);
		p.assertEquals(name.equals(Configuration.REPOSITORY_PROP_FILE_BASENAME), new Boolean(false));
		
		p.setDescription("type cannot be null");
		p.assertNotNull(assetType);
		
		p.setDescription("Cannot find type <" + assetType.getKeyword() +">");
		p.assertEquals(
				new SimpleTypeIterator(Configuration.getRepositorySrc(Access.RW_EXTERNAL),assetType,SimpleType.ASSET).hasNextType()
				,new Boolean(true));



		SimpleAsset a = new SimpleAsset(getSource());	
		a.setAutoFlush(false);
		a.setRepository(getId());
		a.setType(assetType);
		a.setId(new SimpleId(name));
		a.updateDisplayName(displayName);
		a.updateDescription(description);
		a.flush();
		return a;
	}
	
	@Override
	public void deleteAsset(Id assetId) throws RepositoryException {
		

		Parameter p = new Parameter("delete Id");
		p.assertNotNull(assetId);
		p.setDescription("delete external Id:" + assetId.getIdString());
		p.assertEquals(Access.RW_EXTERNAL, this.getSource().getAccess());
		
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
		

		Parameter req = new Parameter();
		req.assertEquals(Access.RW_EXTERNAL, this.getSource().getAccess());
		
		autoFlush = true;
		try {
			File propFile = getRepositoryPropFile();
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
