package gov.nist.hit.ds.simSupport.client;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SimId implements IsSerializable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5119201016599189870L;
	String id;
	
	public SimId() { }
	
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
		return otherId != null && otherId.id != null && otherId.id.equals(id);
	}
}
