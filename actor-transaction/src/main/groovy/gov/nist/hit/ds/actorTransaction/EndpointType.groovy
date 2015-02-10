package gov.nist.hit.ds.actorTransaction;

import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException;
import org.apache.log4j.Logger;


/**
 * Generate labels for endpoints for when they are stored in 
 * SimulatorConfig objects.
 * @author bill
 *
 */
public class EndpointType {
    ActorType actorType
    String label
	TransactionType transType;
	TlsType tls;
	AsyncType async;
    static Logger logger = Logger.getLogger(EndpointType.class);

    public EndpointType() {}

    public EndpointType(TransactionType transType, TlsType tlsType, AsyncType asyncType) {
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
	public EndpointType(ActorType _actorType, String _label)  {
        actorType = _actorType
        label = _label
        if (actorType == null) throw new ToolkitRuntimeException("ActorType is null");
		String[] parts = label.split("_");
		tls = TlsType.NOTLS;
		async = AsyncType.SYNC;
		if (parts == null || parts.length < 1)  {
            // may be client or server - check
			transType = actorType.findTransactionType(label, false);
            if (transType) return
            transType = actorType.findTransactionType(label, true)
            return
		}
        // again, may be client or server - check both
		transType = actorType.findTransactionType(parts[0], false);
        if (!transType) transType = actorType.findTransactionType(parts[0], true)
		int i=1;
		tls = (i < parts.length && "TLS".equalsIgnoreCase(parts[i])) ? TlsType.TLS : TlsType.NOTLS;
		if (isTls()) i++;
		async = (i < parts.length && "ASYNC".equalsIgnoreCase(parts[i])) ? AsyncType.ASYNC : AsyncType.SYNC;
	}

    boolean isValid() { transType }

    String nonValidErrorMsg() { "EndpointLabel:  ${actorType} does not contain transaction for label ${label}"}
	
	public String label() {
		return transType.code + ((isTls()) ? "_TLS" : "") + ((isAsync()) ? "_ASYNC" : "");
	}

	public TransactionType getTransType() {
		return transType;
	}

	public EndpointType setTransType(TransactionType transType) {
		this.transType = transType;
		return this;
	}

	public boolean isTls() {
		return tls == TlsType.TLS;
	}

	public EndpointType setTls(TlsType tls) {
		this.tls = tls;
		return this;
	}

	public boolean isAsync() {
		return async == AsyncType.ASYNC;
	}

	public EndpointType setAsync(AsyncType async) {
		this.async = async;
		return this;
	}
	
	public TlsType getTlsType() { return tls; }
	
	public AsyncType getAsyncType() { return async; }
	
}
