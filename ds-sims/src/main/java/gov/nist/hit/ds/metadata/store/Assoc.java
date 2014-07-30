package gov.nist.hit.ds.metadata.store;


import java.io.Serializable;

public class Assoc extends Ro implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public String from;
	public String to;
	public RegIndex.AssocType type;
	
	public String getType() {
		return "Association(" + type + ")";
	}
	
	public RegIndex.AssocType getAssocType() {
		return type;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}
	
	public String toString() {
		return type.toString() + " source=" + from + " target=" + to;
	}

}
