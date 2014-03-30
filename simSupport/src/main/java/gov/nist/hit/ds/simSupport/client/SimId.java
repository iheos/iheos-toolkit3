package gov.nist.hit.ds.simSupport.client;

import gov.nist.hit.ds.utilities.other.UuidAllocator;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SimId implements IsSerializable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5119201016599189870L;
	String id;
	
	public SimId() { 
		setId(getNewId());
	}
	
	public SimId(String id) {
		setId(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = asDirectoryName(id);
	}

	String asDirectoryName(String id) {
		return id.replaceAll("\\.", "_");
	}
	
	public String toString() {
		return id;
	}
	
	public boolean equals(SimId otherId) {
		if (otherId == null) return false;
		if (id == null && otherId.id == null) return true;
		return otherId.id.equals(id);
	}
	
	String getNewId() {
		String id = UuidAllocator.allocate();
		String[] parts = id.split(":");
		id = parts[2];
		//		id = id.replaceAll("-", "_");

		return id;
	}

}
