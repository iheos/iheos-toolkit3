package gov.nist.hit.ds.simSupport.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyResourceFactory {
	String propertiesPath;
	Properties props = null;

	public PropertyResourceFactory(String propertiesPath) {
		this.propertiesPath = propertiesPath;
	}

	public void load() throws IOException {
		props = new Properties();
		InputStream in = getClass().getClassLoader().getResourceAsStream(propertiesPath);
		if (in == null)
			throw new IOException("Cannot loadFromPropertyBasedResource Property Resource from <" + propertiesPath + ">");
		props.load(in);
	}

	public Properties getProperties() throws IOException {
		if (props == null)
			load();
		return props;
	}
}
