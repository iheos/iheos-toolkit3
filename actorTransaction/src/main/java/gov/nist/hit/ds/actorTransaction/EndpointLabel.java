package gov.nist.hit.ds.actorTransaction;

/**
 * Generate labels for endpoints for when they are stored in 
 * SimulatorConfig objects.
 * @author bill
 *
 */
public class EndpointLabel {
	TransactionType transType;
	boolean tls;
	boolean async;
	
	public EndpointLabel(TransactionType transType, TlsType tlsType, AsyncType asyncType) {
		this.transType = transType;
		this.tls = tlsType == TlsType.TLS;
		this.async = asyncType == AsyncType.ASYNC;
	}
	
	public EndpointLabel(String label) {
		String[] parts = label.split("_");
		try {
			transType = TransactionType.find(parts[0]);
			tls = "TLS".equals(parts[1]);
			if (tls) 
				async = "ASYNC".equals(parts[2]);
			else
				async = "ASYNC".equals(parts[1]);
		} catch (ArrayIndexOutOfBoundsException e ) {};
	}
	
	public String get() {
		return transType.getName() + ((tls) ? "_TLS" : "") + ((async) ? "_ASYNC" : "");
	}

	public TransactionType getTransType() {
		return transType;
	}

	public EndpointLabel setTransType(TransactionType transType) {
		this.transType = transType;
		return this;
	}

	public boolean isTls() {
		return tls;
	}

	public EndpointLabel setTls(boolean tls) {
		this.tls = tls;
		return this;
	}

	public boolean isAsync() {
		return async;
	}

	public EndpointLabel setAsync(boolean async) {
		this.async = async;
		return this;
	}
	
	public TlsType getTlsType() { return (tls) ? TlsType.TLS : TlsType.NOTLS; }
	
	public AsyncType getAsyncType() { return (async) ? AsyncType.ASYNC : AsyncType.SYNC; }
	
}
