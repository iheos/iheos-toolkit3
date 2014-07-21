package gov.nist.hit.ds.simSupport.datatypes;

import gov.nist.hit.ds.simSupport.client.SimId;

public class SimEndpoint {
	private String simid = null;
	private String actor = null;
	private String transaction = null;
	
	
	public SimId getSimId() {
		return new SimId(simid);
	}
	public SimEndpoint setSimId(String simid) {
		this.simid = simid;
		return this;
	}
	public String getActor() {
		return actor;
	}
	public SimEndpoint setActor(String actor) {
		this.actor = actor;
		return this;
	}
	public String getTransaction() {
		return transaction;
	}
	public SimEndpoint setTransaction(String transaction) {
		this.transaction = transaction;
		return this;
	}

}
