package gov.nist.hit.ds.actorTransaction;

/**
 * Generate labels for endpoints for when they are stored in 
 * SimulatorConfig objects.
 * @author bill
 *
 */
public class EndpointLabel {
	TransactionType transType;
	
	public EndpointLabel(TransactionType transType) {
		this.transType = transType;
	}
	
	public String get(TlsType tlsType, AsyncType asyncType) {
		String tls = (tlsType == TlsType.TLS) ? "_TLS" : "";
		String async = (asyncType == AsyncType.ASYNC) ? "_ASYNC" : "";
		return transType.getName() + tls + async;
	}
	
}
