package gov.nist.hit.ds.repository.simple.index;

import gov.nist.hit.ds.repository.api.RepositoryException;

public interface Index {
	void addIndex(String repositoryId) throws RepositoryException;
	void addIndex(String repositoryId, String assetId, String assetType /* We should create an Enum type */, String property, String value) throws RepositoryException;
	void removeIndex(String reposId, String sessionId) throws RepositoryException;
}
