package gov.nist.hit.ds.actorTransaction;

import gov.nist.hit.ds.actorTransaction.client.TransactionType;

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
	
	/**
	 * Parse a display lqbel for an endpoint.
	 * @param label
	 */
	public EndpointLabel(String label)  {
		String[] parts = label.split("_");
		tls = false;
		async = false;
		if (parts == null || parts.length <= 1)  {
			transType = TransactionTypeFactory.find(label);
			return;
		}
		transType = TransactionTypeFactory.find(parts[0]);
		
		int i=1;
		tls = i < parts.length && "TLS".equalsIgnoreCase(parts[i]);
		if (tls) i++;
		async = i < parts.length && "ASYNC".equalsIgnoreCase(parts[i]);
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
