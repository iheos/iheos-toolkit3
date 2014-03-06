package gov.nist.toolkit.testengine.logging;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogMap implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6977145786942237537L;
	List<LogMapItem> items = new ArrayList<LogMapItem>();
	transient Map<String, TestSectionLogContent> logFileContent = new HashMap<String, TestSectionLogContent>();
	
	public LogMap() {
	}
	
	public void add(String testName, TestSectionLogContent log) {
		logFileContent.put(testName, log);
		items.add(new LogMapItem(testName, log));
	}
	
	public List<LogMapItem> getItems() { return items; }
	
	public Map<String, TestSectionLogContent> getLogFileContentMap() { return logFileContent; }
	
	public List<String> getKeys() {
		List<String> keys = new ArrayList<String>();
		
		for (LogMapItem item : items) {
			keys.add(item.testName);
		}
		
		return keys;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("[LogMap: ");
		
		for (LogMapItem i : items) {
			buf.append(i.toString());
		}
		
		buf.append("]");
		
		return buf.toString();
	}
	
}
