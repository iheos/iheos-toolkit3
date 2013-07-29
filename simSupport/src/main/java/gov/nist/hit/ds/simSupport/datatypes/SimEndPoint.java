package gov.nist.hit.ds.simSupport.datatypes;

public class SimEndPoint {
	private String simid = null;
	private String actor = null;
	private String transaction = null;
	
	
	public String getSimId() {
		return simid;
	}
	public SimEndPoint setSimId(String simid) {
		this.simid = simid;
		return this;
	}
	public String getActor() {
		return actor;
	}
	public SimEndPoint setActor(String actor) {
		this.actor = actor;
		return this;
	}
	public String getTransaction() {
		return transaction;
	}
	public SimEndPoint setTransaction(String transaction) {
		this.transaction = transaction;
		return this;
	}



}
