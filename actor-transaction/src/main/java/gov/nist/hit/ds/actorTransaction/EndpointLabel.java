package gov.nist.hit.ds.actorTransaction;

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;
import org.apache.log4j.Logger;


/**
 * Generate labels for endpoints for when they are stored in 
 * SimulatorConfig objects.
 * @author bill
 *
 */
public class EndpointLabel {
	TransactionType transType;
	TlsType tls;
	AsyncType async;
    static Logger logger = Logger.getLogger(EndpointLabel.class);

    public EndpointLabel() {}

    public EndpointLabel(TransactionType transType, TlsType tlsType, AsyncType asyncType) {
        if (transType == null) {
            logger.error("TransactionType is null");
            throw new ToolkitRuntimeException("TransactionType is null");
        }
		this.transType = transType;
		this.tls = tlsType;
		this.async = asyncType;
	}

    public String toString() {
        return "EndpointLabel:" + transType + ":" + tls + ":" + async;
    }
	
	/**
	 * Parse a display lqbel for an endpoint. An example is:  Register_TLS_ASYNC
	 * @param label
	 */
	public EndpointLabel(ActorType actorType, String label)  {
        if (actorType == null) throw new ToolkitRuntimeException("ActorType is null");
		String[] parts = label.split("_");
		tls = TlsType.NOTLS;
		async = AsyncType.SYNC;
		if (parts == null || parts.length <= 1)  {
			transType = actorType.find(label);
			return;
		}
		transType = actorType.find(parts[0]);
		
		int i=1;
		tls = (i < parts.length && "TLS".equalsIgnoreCase(parts[i])) ? TlsType.TLS : TlsType.NOTLS;
		if (isTls()) i++;
		async = (i < parts.length && "ASYNC".equalsIgnoreCase(parts[i])) ? AsyncType.ASYNC : AsyncType.SYNC;
	}
	
	public String label() {
		return transType.getName() + ((isTls()) ? "_TLS" : "") + ((isAsync()) ? "_ASYNC" : "");
	}

	public TransactionType getTransType() {
		return transType;
	}

	public EndpointLabel setTransType(TransactionType transType) {
		this.transType = transType;
		return this;
	}

	public boolean isTls() {
		return tls == TlsType.TLS;
	}

	public EndpointLabel setTls(TlsType tls) {
		this.tls = tls;
		return this;
	}

	public boolean isAsync() {
		return async == AsyncType.ASYNC;
	}

	public EndpointLabel setAsync(AsyncType async) {
		this.async = async;
		return this;
	}
	
	public TlsType getTlsType() { return tls; }
	
	public AsyncType getAsyncType() { return async; }
	
}
