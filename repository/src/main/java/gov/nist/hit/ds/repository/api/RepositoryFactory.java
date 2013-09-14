package gov.nist.hit.ds.repository.api;


import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleRepository;
import gov.nist.hit.ds.repository.simple.SimpleRepositoryIterator;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.repository.simple.SimpleTypeIterator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;

/**
 * RepositoryManager
 * ToDo
 *  - Introduce other data types (xml, txt, serialized objects) instead of just byte[]
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
		
		assertAccess(Access.RW_EXTERNAL);
		assertType(repositoryType);	
		
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

		assertAccess(Access.RW_EXTERNAL);
		assertType(repositoryType);
		
		SimpleRepository rep = new SimpleRepository(repositoryName);
		rep.setSource(getSource());
		rep.setAutoFlush(false);
		rep.setType(repositoryType);
		rep.setDescription(description);
		rep.setDisplayName(displayName);
		rep.flush();
		return rep;
	}
	
	/**
	 * @param repositoryType
	 * @throws RepositoryException
	 */
	private void assertType(Type repositoryType)
			throws RepositoryException {
		Parameter p = new Parameter();
		
		p.setDescription("repositoryType");
		p.assertNotNull(repositoryType);
		
		// There are two things to look for with type
		// 1. is it a valid type
		//  a. is it of a repository domain
		
		if (!Parameter.isNullish(repositoryType.getDomain())) {
			p.setDescription("domain: " + repositoryType.getDomain());
			p.assertEquals(repositoryType.getDomain(), SimpleType.REPOSITORY);
		}
		
		p.setDescription("Cannot find repository type <" + repositoryType.getKeyword() + "> "
				+ " in " + Configuration.getRepositorySrc(Access.RW_EXTERNAL).getLocation().toString());
		p.assertEquals(
				new SimpleTypeIterator(Configuration.getRepositorySrc(Access.RW_EXTERNAL),repositoryType,SimpleType.REPOSITORY).hasNextType()
				,new Boolean(true));
	}

	private void assertAccess(Access acs) throws RepositoryException {
		Parameter p = new Parameter();
		p.assertEquals(acs, getSource().getAccess());				
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

		Parameter req = new Parameter();
		req.assertEquals(Access.RW_EXTERNAL, this.getSource().getAccess());
		
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
		

		Parameter req = new Parameter();
		req.assertEquals(Access.RW_EXTERNAL, this.getSource().getAccess());
		
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
