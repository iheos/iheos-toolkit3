package gov.nist.toolkit.testengine.logrepository;

import gov.nist.toolkit.results.client.XdstestLogId;
import gov.nist.toolkit.testengine.LogMap;
import gov.nist.hit.ds.xdsExceptions.XdsException;

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
