package gov.nist.hit.ds.testClient.logRepository;

import gov.nist.hit.ds.testClient.logging.LogMap;
import gov.nist.hit.ds.testClient.results.XdstestLogId;
import gov.nist.hit.ds.xdsException.XdsException;

import java.io.File;

public class JacksonIO implements ILoggerIO {

	 
	public void logOut(XdstestLogId id, LogMap log, File logDir)
			throws XdsException {
//		throw new NotImplemented();	
		}

	 
	public LogMap logIn(XdstestLogId id, File logDir) throws Exception {
//		throw new NotImplemented();	
		return null;
		}
}
