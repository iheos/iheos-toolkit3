package gov.nist.hit.ds.registrySim.actor;

import java.util.Properties;

public class RegistryConfiguration {
	Properties properties;
	
	public RegistryConfiguration(Properties properties) {
		this.properties = properties;
	}
	
	public boolean isExtraMetadataSupported() {
		return "true".equalsIgnoreCase(properties.getProperty("ExtraMetadata", "false"));
	}
	
	public RegistryConfiguration setExtraMetadataSupported(boolean supported) {
		properties.setProperty("ExtraMetadata", (supported) ? "true" : "false");
		return this;
	}

	public boolean isMetadataUpdateSupported() {
		return "true".equalsIgnoreCase(properties.getProperty("MetadataUpdate", "false"));
	}
	
	public RegistryConfiguration setMetadataUpdateSupported(boolean supported) {
		properties.setProperty("MetadataUpdate", (supported) ? "true" : "false");
		return this;
	}
}
