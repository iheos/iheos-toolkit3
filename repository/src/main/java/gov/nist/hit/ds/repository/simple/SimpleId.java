package gov.nist.hit.ds.repository.simple;

import gov.nist.hit.ds.repository.api.Id;
import gov.nist.hit.ds.repository.api.RepositoryException;

public class SimpleId implements Id {
	String guid;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8299302965195505251L;
	
	public SimpleId(String id) {
		this.guid = id;
	}

	@Override
	public String getIdString() throws RepositoryException {
		return guid;
	}

	@Override
	public boolean isEqual(Id id) throws RepositoryException {
		return id != null && id.getIdString().equals(guid);
	}

	public String toString() {
		return guid;
	}
	
}
