package gov.nist.toolkit.testengine.logging;

import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.util.*;

public class SectionLogMap {
	// section name => log
	Map<String, TestSectionLogContent> sectionLogs;
	List<String> sectionNames;   // this dictates the order of the sections

	public SectionLogMap() {
		sectionLogs = new HashMap<String, TestSectionLogContent>();
		sectionNames = new ArrayList<String>();
	}
	
	public List<SectionGoals> getGoals() {
		List<SectionGoals> goals = new ArrayList<SectionGoals>();
		for (String sectionName : sectionNames) {
			goals.add(sectionLogs.get(sectionName).getGoals());
		}
		return goals;
	}

	public TestSectionLogContent getLogForSection(String sectionName) {
		return sectionLogs.get(sectionName);
	}

	public void put(String sectionName, TestSectionLogContent log) throws XdsInternalException {
		if (log == null)
			throw new XdsInternalException("Null log for section " + sectionName);
		sectionNames.add(sectionName);
		sectionLogs.put(sectionName, log);
		Report.setSection(log.getReports(), sectionName);
	}

	public TestSectionLogContent get(String sectionName) throws XdsInternalException {
		TestSectionLogContent lf = sectionLogs.get(sectionName);
//		if (lf == null && !sectionName.equals("THIS"))
//			throw new XdsInternalException("Log for section " + sectionName + " is null");
		return lf;
	}

	public Collection<String> keySet() {
		return sectionNames;
	}

	public String toString() {
		return reportsToString();
	}
	
	public void remove(String sectionName) {
		sectionLogs.remove(sectionName);
	}

	public String reportsToString()  {
		StringBuffer buf = new StringBuffer();
		buf.append('[');

		for (String section : sectionLogs.keySet()) {
			buf.append("Section: ").append(section).append(": ");
			TestSectionLogContent log = sectionLogs.get(section);
			try {
				List<Report> reports = log.getReports();
				for (Report r : reports) {
					r.section = section;
				}
				buf.append(reports.toString());
			} catch (Exception e) {
				System.out.println("Cannot find Reports for section " + section);
			}
		}

		buf.append(']');
		return buf.toString();

	}


}
