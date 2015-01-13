package gov.nist.hit.ds.ebDocsrcSim.engine;

import gov.nist.hit.ds.ebDocsrcSim.soap.SecurityParams;

public class TransactionSettings {
	/**
	 * Should Patient ID be assigned from xdstest config on Submissions?
	 */
	public Boolean assignPatientId = null;   // allows for null (unknown)
	public String patientId = null;
	public String altPatientId = null;
//	public LogRepository logRepository = null;
	public boolean writeLogs = false;
//	public SiteSpec siteSpec;
//	public AssertionResults res = null;
	public String user = null;
	
	public SecurityParams securityParams = null;
	
//	public TransactionSettings() {
//		res = new AssertionResults();
//	}
}
