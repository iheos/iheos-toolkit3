package gov.nist.toolkit.testengine.logrepository;

import gov.nist.hit.ds.xdsException.XdsException;
import gov.nist.toolkit.results.client.XdstestLogId;
import gov.nist.toolkit.testengine.logging.LogMap;

import java.io.File;

public interface ILoggerIO {

	public  void logOut(XdstestLogId id, LogMap log, File logDir)
			throws XdsException;

	public  LogMap logIn(XdstestLogId id, File logDir) throws Exception;

}