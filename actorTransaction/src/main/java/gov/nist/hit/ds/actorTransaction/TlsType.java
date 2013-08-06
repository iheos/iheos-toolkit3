package gov.nist.hit.ds.actorTransaction;

import java.util.List;

public enum TlsType {
	NOTLS, TLS;
	
	public boolean isTls() {
		return TLS.equals(this.name());
	}
	
	public TlsType[] getAll() {
		return TlsType.values();
	}
}
