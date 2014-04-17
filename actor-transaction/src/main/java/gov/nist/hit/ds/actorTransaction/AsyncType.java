package gov.nist.hit.ds.actorTransaction;

public enum AsyncType {
	SYNC, ASYNC;
	
	public boolean isAsync() {
		return ASYNC.equals(this.name());
	}

	public AsyncType[] getAll() {
		return AsyncType.values();
	}

}
