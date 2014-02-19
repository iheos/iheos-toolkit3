package gov.nist.hit.ds.repository.simple.index;

import gov.nist.hit.ds.repository.api.RepositoryException;

public interface Index {
	int addIndex(String repositoryId, String assetId, String assetType, String locationStr, String property, String value) throws RepositoryException;
	void removeIndex(String reposId, String acs, String sessionId) throws RepositoryException;
}
