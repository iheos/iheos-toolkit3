package gov.nist.hit.ds.testClient.results;

import java.io.Serializable;

public class XdstestLogId implements Serializable {
	String id = null; 
	
	
	public XdstestLogId() {
		
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public XdstestLogId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public boolean equals(XdstestLogId xid) {
		if (id == null)
			return false;
		return id.equals(xid.id);
	}

}
