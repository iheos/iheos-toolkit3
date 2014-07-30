package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.RepositoryException;

/**
 * A SimpleId is mainly used to hold a uniquely identifying piece of information that is commonly associated with assets in a repository. The scope of the uniqueness, such as 'to be unique within a repository' and the enforcement thereof is not part of this class.
 */
public class SimpleId implements ArtifactId {
	String guid;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8299302965195505251L;
	private boolean autoGenerated = false;
    private String userFriendlyName;

    /**
     *
     * @param id The {@code String} to assign as "unique."
     */
	public SimpleId(String id) {
		this.guid = id;
	}

    /**
     *
     * @return
     * @throws RepositoryException
     */
	@Override
	public String getIdString() throws RepositoryException {
		return guid;
	}

    /**
     * Compare two ArtifactIds (using {@code String.equals})
     * @param id
     *
     * @return
     * @throws RepositoryException
     */
	@Override
	public boolean isEqual(ArtifactId id) throws RepositoryException {
		return id != null && id.getIdString().equals(guid);
	}

	public String toString() {
		return guid;
	}

    /**
     *
     * @return boolean
     */
	public boolean isAutoGenerated() {
		return autoGenerated;
	}

    /**
     *
     * @param name The name
     */
    @Override
    public void setUserFriendlyName(String name) {
        this.userFriendlyName = name;
    }

    /**
     *
     * @return
     */
    @Override
    public String getUserFriendlyName() {
        return userFriendlyName;
    }

    /**
     *
     * @param autoGenerated Usually indicates non-human readable format
     */
	public void setAutoGenerated(boolean autoGenerated) {
		this.autoGenerated = autoGenerated;
	}




	
}
