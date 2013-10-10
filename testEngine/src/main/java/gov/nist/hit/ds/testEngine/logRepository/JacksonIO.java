package gov.nist.hit.ds.testEngine.logRepository;

import gov.nist.hit.ds.testEngine.logging.LogMap;
import gov.nist.hit.ds.testEngine.results.client.XdstestLogId;
import gov.nist.hit.ds.xdsException.XdsException;

import java.io.File;

public class JacksonIO implements ILoggerIO {

	@Override
	public void logOut(XdstestLogId id, LogMap log, File logDir)
			throws XdsException {
//		throw new NotImplemented();	
		}

	@Override
	public LogMap logIn(XdstestLogId id, File logDir) throws Exception {
//		throw new NotImplemented();	
		return null;
		}
}
