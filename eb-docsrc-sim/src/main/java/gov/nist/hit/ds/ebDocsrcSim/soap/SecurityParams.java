package gov.nist.hit.ds.ebDocsrcSim.soap;

import gov.nist.hit.ds.xdsException.EnvironmentNotSelectedException;

import java.io.File;
import java.io.IOException;

public interface SecurityParams {
	File getCodesFile() throws EnvironmentNotSelectedException;
	File getKeystore() throws EnvironmentNotSelectedException;
	String getKeystorePassword() throws IOException, EnvironmentNotSelectedException;
	public String getKeystoreAlias() throws IOException, EnvironmentNotSelectedException;
	public File getKeystoreDir() throws EnvironmentNotSelectedException;
}
