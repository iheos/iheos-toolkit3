package gov.nist.hit.ds.initialization.installation;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ExtendedPropertyManager {

	static Logger logger = Logger.getLogger(ExtendedPropertyManager.class);
	static Properties properties = null;
	
	
	static public void load() {
		if (properties != null) 
			return;
		properties = new Properties();

		File propFile = Installation.installation().getExtendedPropertiesFile();
		if (propFile == null || !propFile.exists()) {
			logger.warn("Extended properties file not found");
			return;
		}
		try {
			properties.load(new FileInputStream(propFile));
		} catch (Exception e) {
			logger.error("Cannot load extended.properties", e);
		}
	}
	
	static public String getProperty(String propName)  {
		if (properties == null) {
			
			load();
			
			// This is obsolete 
			if (properties==null) {			
				RuntimeException e = new RuntimeException("Extended properties not loaded");
				logger.error("Extended Properties queried before they are loaded", e);
				throw e;
			}
		}
		return properties.getProperty(propName);
	}
	
	static public String getProperty(@SuppressWarnings("rawtypes") Class clas, String propShortName)  {
		String val = getProperty(clas.getName() + "." + propShortName);
		return val;
	}
}
