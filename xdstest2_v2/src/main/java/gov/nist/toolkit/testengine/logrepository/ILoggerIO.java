package gov.nist.toolkit.testengine.logrepository;

import java.io.File;

import gov.nist.toolkit.results.client.XdstestLogId;
import gov.nist.toolkit.testengine.LogMap;
import gov.nist.hit.ds.xdsExceptions.XdsException;

public interface ILoggerIO {

	public  void logOut(XdstestLogId id, LogMap log, File logDir)
			throws XdsException;

	public  LogMap logIn(XdstestLogId id, File logDir) throws Exception;

}
