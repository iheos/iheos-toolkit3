package gov.nist.hit.ds.repository.api;

import java.io.File;

public class RepositorySource {
	private File location;
	private Access access = null;
	private boolean isValid = false;

	public RepositorySource(File location) {
		super();
		this.location = location;
	}
	
	public RepositorySource(File location, Access access) {
		super();
		this.location = location;
		this.access = access;
	}

	public enum Access { 
		RO_RESIDENT /* This is the read-only resident source used to access static test definitions */
		, RW_EXTERNAL /* This is the general purpose read-write source */ }; 
	
	public File getLocation() {
		return location;
	}

	public void setLocation(File location) {
		this.location = location;
	}

	public Access getAccess() {
		return access;
	}

	public void setAccess(Access access) {
		this.access = access;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}


	

}
