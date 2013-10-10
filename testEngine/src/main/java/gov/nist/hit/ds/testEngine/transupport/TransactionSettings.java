package gov.nist.hit.ds.testEngine.transupport;

import gov.nist.hit.ds.securityCommon.SecurityParams;
import gov.nist.hit.ds.testEngine.logRepository.LogRepository;
import gov.nist.hit.ds.testEngine.results.client.AssertionResults;
import gov.nist.hit.ds.testEngine.results.client.SiteSpec;


public class TransactionSettings {
	/**
	 * Should Patient ID be assigned from xdstest config on Submissions?
	 */
	public Boolean assignPatientId = null;   // allows for null (unknown)
	public String patientId = null;
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
