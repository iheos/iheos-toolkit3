package gov.nist.hit.ds.repository.api;

import java.io.File;

/**
 * A repository source can be defined as the root directory that contains two main parts. One part is a collection of repositories in the {@code data} folder and the second part being a collection of domain types in the {@code types} folder.
 */
public class RepositorySource {
	private File location;
	private Access access = null;
	private boolean isValid = false;

    /**
     *
     * @param location The root directory of the entire repositories collection.
     */
	public RepositorySource(File location) {
		super();
		this.location = location;
	}

    /**
     *
     * @param location The root directory of the entire repositories collection.
     * @param access The application access to the repository source.
     */
	public RepositorySource(File location, Access access) {
		super();
		this.location = location;
		this.access = access;
	}

    /**
     * The application access to the repository source.
     *
     */
	public enum Access {
        /* This is the read-only resident source used to access static test definitions */
		RO_RESIDENT
        /* This is the general purpose read-write source */
		, RW_EXTERNAL
    };

    /**
     * Gets the repository source root location.
     * @return
     */
	public File getLocation() {
		return location;
	}

    /**
     * Sets the repository source root location.
     * @return
     */
	public void setLocation(File location) {
		this.location = location;
	}

    /**
     * Gets the application access to the repository source.
     * @return
     */
	public Access getAccess() {
		return access;
	}

    /**
     * Sets the application access to the repository source.
     * @return
     */
	public void setAccess(Access access) {
		this.access = access;
	}

    /**
     *
     * @return Returns true if the repository source root valid.
     */
	public boolean isValid() {
		return isValid;
	}

    /**
     *
     * @return Sets a flag to indicate the source root validity.
     */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}


	

}
