package gov.nist.hit.ds.testEngine.logRepository;

import gov.nist.hit.ds.testEngine.logging.LogMap;
import gov.nist.hit.ds.testEngine.results.client.XdstestLogId;
import gov.nist.hit.ds.xdsException.XdsException;

import java.io.File;

public class LogRepository  {
	// Both of these are initialized by LogRepositoryFactory
	File logDir;
	ILoggerIO logger;
	
	// Create through LogRepositoryFactory only
	LogRepository() {}
	
	public void logOut(XdstestLogId id, LogMap log)
			throws XdsException {
		logger.logOut(id, log, logDir);
	}
	
	public LogMap logIn(XdstestLogId id) throws Exception {
		return logger.logIn(id, logDir);
	}
	
	public File logDir() {
		return logDir;
	}
}
