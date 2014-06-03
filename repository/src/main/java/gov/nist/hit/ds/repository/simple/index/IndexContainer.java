package gov.nist.hit.ds.repository.simple.index;

import gov.nist.hit.ds.repository.api.RepositoryException;

public interface IndexContainer {
	String getIndexContainerDefinition();
	boolean doesIndexContainerExist() throws RepositoryException;
	void createIndexContainer() throws RepositoryException;
	void removeIndexContainer() throws RepositoryException;
}