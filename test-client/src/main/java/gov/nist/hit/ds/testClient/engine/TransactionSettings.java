package gov.nist.hit.ds.testClient.engine;

import gov.nist.hit.ds.testClient.logRepository.LogRepository;
import gov.nist.hit.ds.testClient.results.AssertionResults;
import gov.nist.hit.ds.testClient.results.SiteSpec;
import gov.nist.hit.ds.testClient.soap.SecurityParams;

public class TransactionSettings {
	/**
	 * Should Patient ID be assigned from xdstest config on Submissions?
	 */
	public Boolean assignPatientId = null;   // allows for null (unknown)
	public String patientId = null;
	public String altPatientId = null;
//	public File logDir = null;
	public LogRepository logRepository = null;
	public boolean writeLogs = false;
	public SiteSpec siteSpec;
	public AssertionResults res = null;
	public String user = null;
	
	public SecurityParams securityParams = null;
	
	public TransactionSettings() {
		res = new AssertionResults();
	}
}
