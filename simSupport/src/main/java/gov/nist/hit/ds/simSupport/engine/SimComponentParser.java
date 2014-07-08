package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.utilities.string.StringUtil;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class SimComponentParser {
	Properties properties;
	List<Properties> componentProperties = new ArrayList<Properties>();
	boolean hasRun = false;
	
	public SimComponentParser(Properties properties) {
		this.properties = properties;
	}
	
	public List<Properties> getComponentProperties() {
		run();
		return componentProperties;
	}
	
	public int size() {
		run();
		return componentProperties.size();
	}
	
	void run() {
		if (hasRun)
			return;
		hasRun = true;
		for (int i=1; i<9999; i++) {
			String prefix = getElementName(i);
			Properties p = getPropertiesThatStartWith(prefix);
			if (p.isEmpty())
				break;
			componentProperties.add(p);
		}
	}
	
	String getElementName(int i) {
		return "element" + i;
	}
	
	/**
	 * Remove prefix before stuffing into new Properties object.
	 * @param prefix
	 * @return
	 */
	Properties getPropertiesThatStartWith(String prefix) {
		Properties props = new Properties();
		if (!prefix.endsWith("."))
			prefix = prefix + ".";
		for (Enumeration<?> en = properties.propertyNames(); en.hasMoreElements(); ) {
			String name = (String) en.nextElement();
			String value = properties.getProperty(name);
			if (name.startsWith(prefix)) {
				String newName = StringUtil.removePrefix(name, prefix);
				props.setProperty(newName, value);
			}
		}
		return props;
	}
	
}
