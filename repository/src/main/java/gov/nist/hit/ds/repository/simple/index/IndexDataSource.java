package gov.nist.hit.ds.repository.simple.index;

import gov.nist.hit.ds.repository.api.RepositoryException;

import java.sql.Connection;


public interface IndexDataSource {
	void setupDataSource() throws RepositoryException;
	Connection getConnection() throws RepositoryException;
}