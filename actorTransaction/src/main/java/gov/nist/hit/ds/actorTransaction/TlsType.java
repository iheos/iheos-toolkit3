package gov.nist.hit.ds.actorTransaction;


public enum TlsType {
	NOTLS, TLS;
	
	public boolean isTls() {
		return TLS.equals(this);
	}
	
	public TlsType[] getAll() {
		return TlsType.values();
	}
}
