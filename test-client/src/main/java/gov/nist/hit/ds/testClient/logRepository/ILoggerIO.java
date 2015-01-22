package gov.nist.hit.ds.testClient.logRepository;

import gov.nist.hit.ds.testClient.logging.LogMap;
import gov.nist.hit.ds.testClient.results.XdstestLogId;
import gov.nist.hit.ds.xdsExceptions.XdsException;

import java.io.File;

public interface ILoggerIO {

	public  void logOut(XdstestLogId id, LogMap log, File logDir)
			throws XdsException;

	public LogMap logIn(XdstestLogId id, File logDir) throws Exception;

}
