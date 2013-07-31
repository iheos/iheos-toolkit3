package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.RepositoryException;

public interface Flushable {
	boolean isAutoFlush();
	void setAutoFlush(boolean autoFlush);
	void flush() throws RepositoryException;
}
