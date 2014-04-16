package gov.nist.toolkit.testengine.logging;

import java.io.Serializable;

public class LogMapItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -290403612300468696L;
	public String testName;
	public TestSectionLogContent log;
	
	public LogMapItem(String testName, TestSectionLogContent log) {
		this.testName = testName;
		this.log = log;
	}
	
	public String toString() {
		return "[LogMapItem: testName=" + testName + " log=" + log.toString() + "]";
	}
}
