package gov.nist.hit.ds.repositoryDirectory;

import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleType;

public class Directory {
	RepositoryFactory externalFactory;
	RepositoryFactory internalFactory;

	public Directory() throws RepositoryException {
		externalFactory = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
//		internalFactory = new RepositoryFactory(Configuration.getRepositorySrc(Access.RO_RESIDENT));
	}

	public RepositoryFactory getExternalFactory() {
		return externalFactory;
	}

	public RepositoryFactory getInternalFactory() {
		return internalFactory;
	}
	
	public Repository getExternalSimSiteRepository() throws RepositoryException {
		return externalFactory.createNamedRepository("SimSites", "", new SimpleType("siteRepo"), "ExternalSimSites");
	}

	public Repository getInternalSimSiteRepository() throws RepositoryException {
		return internalFactory.createNamedRepository("SimSites", "", new SimpleType("siteRepo"), "InternalSimSites");
	}

	public Repository getExternalSiteRepository() throws RepositoryException {
		return externalFactory.createNamedRepository("Sites", "", new SimpleType("siteRepo"), "ExternalSites");
	}

	public Repository getInternalSiteRepository() throws RepositoryException {
		return internalFactory.createNamedRepository("Sites", "", new SimpleType("siteRepo"), "InternalSites");
	}
	
	public SimpleType getActorAssetType() throws RepositoryException { return new SimpleType("siteAsset"); }
}
