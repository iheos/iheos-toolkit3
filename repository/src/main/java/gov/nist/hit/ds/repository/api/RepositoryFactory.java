package gov.nist.hit.ds.repository.api;


import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleRepository;
import gov.nist.hit.ds.repository.simple.SimpleRepositoryIterator;
import gov.nist.hit.ds.repository.simple.SimpleTypeIterator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;

/**
 * RepositoryManager
 * ToDo
 * 	- Create Abstract Repository and refactor simple (in prep for derby based repository)
 *  - Introduce other data types (xml, txt, serialized objects) instead of just byte[]
 *  - Introduce use of type element domain to indicate a type is a repository vs others :
 *      domain=repository
 *  - The DisplayName property should be displayName (repository.props)
 * @author bmajur
 *
 */
public class RepositoryFactory implements RepositoryManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4491794003213633389L;
	private RepositorySource source;

	
	/**
	 * 
	 * @param src
	 */
	// Keep RepositorySource as a constructor param because it makes Junit testing more meaningful by avoiding a dependency on the instantiation/setup of Configuration    
	public RepositoryFactory(RepositorySource src) { 
		super();
		
		setSource(src);
	}


	@Override
	public Repository createRepository(String displayName, String description,
			Type repositoryType) throws RepositoryException {
		
		if (! (this.getSource()!=null && Access.RW_EXTERNAL.equals(this.getSource().getAccess()))) {
			throw new RepositoryException(RepositoryException.PERMISSION_DENIED + ": Cannot update non read-write repository source.");
		}
		
		
	
		SimpleRepository rep = new SimpleRepository();
		rep.setSource(getSource());
		rep.setAutoFlush(false);
		rep.setType(repositoryType);
		rep.setDescription(description);
		rep.setDisplayName(displayName);
		rep.flush();
		return rep;
	
		
	}
	

	@Override
	public Repository createNamedRepository(String displayName,
			String description, Type repositoryType, String repositoryName)
			throws RepositoryException {
		SimpleRepository rep = new SimpleRepository(repositoryName);
		rep.setSource(getSource());
		rep.setAutoFlush(false);
		rep.setType(repositoryType);
		rep.setDescription(description);
		rep.setDisplayName(displayName);
		rep.flush();
		return rep;
	}	


	@Override
	public OsidContext getOsidContext() throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void assignOsidContext(OsidContext context) throws RepositoryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void assignConfiguration(Properties configuration)
			throws RepositoryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void osidVersion_2_0() throws RepositoryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRepository(Id repositoryId) throws RepositoryException {
		if (Configuration.repositoryExists(getSource(), repositoryId))
			return;
		SimpleRepository repos = new SimpleRepository(repositoryId);
		repos.setSource(getSource());
		repos.delete();
	}
	
	@Override
	public RepositoryIterator getRepositories() throws RepositoryException {
		return new SimpleRepositoryIterator(getSource().getLocation());
	}

	@Override
	public RepositoryIterator getRepositoriesByType(Type repositoryType)
			throws RepositoryException {
		return new SimpleRepositoryIterator(getSource().getLocation(), repositoryType);
	}		

	@Override
	public Repository getRepository(Id id) throws RepositoryException {
		SimpleRepository repos = new SimpleRepository(id);
		repos.setSource(getSource());
		repos.load();
		return repos;
	}
	
	@Override
	public Asset getAsset(Id assetId) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Asset getAssetByDate(Id assetId, long date)
			throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LongValueIterator getAssetDates(Id assetId)
			throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssetIterator getAssetsBySearch(Repository[] repositories,
			Serializable searchCriteria, Type searchType,
			gov.nist.hit.ds.repository.api.Properties searchProperties)
					throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Id copyAsset(Repository repository, Id assetId)
			throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeIterator getRepositoryTypes() throws RepositoryException {
		return new SimpleTypeIterator(getSource().getLocation());
	}

	public TypeIterator getRepositoryTypes(RepositorySource rs) throws RepositoryException {
		ArrayList<RepositorySource> rss = new ArrayList<RepositorySource>();
		rss.add(rs);
		return new SimpleTypeIterator(rss,null);
	}
	
	public RepositorySource getSource() throws RepositoryException {
		if (source==null) {
			throw new RepositoryException(RepositoryException.NULL_ARGUMENT + ": source cannot be null");
		}
		return source;
	}


	public void setSource(RepositorySource source) {
		this.source = source;
	}






}
