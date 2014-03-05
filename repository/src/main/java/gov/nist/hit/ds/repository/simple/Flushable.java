package gov.nist.hit.ds.repository.simple;

import java.io.File;

import gov.nist.hit.ds.repository.api.RepositoryException;

public interface Flushable {
	boolean isAutoFlush();
	void setAutoFlush(boolean autoFlush);
	void flush(File propFile) throws RepositoryException;
}
