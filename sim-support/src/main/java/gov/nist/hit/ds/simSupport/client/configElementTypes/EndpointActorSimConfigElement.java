package gov.nist.hit.ds.simSupport.client.configElementTypes;

import com.google.gwt.user.client.rpc.IsSerializable;
import gov.nist.hit.ds.actorTransaction.EndpointLabel;
import gov.nist.hit.ds.simSupport.client.ParamType;
import gov.nist.hit.ds.simSupport.endpoint.Endpoint;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import java.io.Serializable;

public class EndpointActorSimConfigElement extends
        AbstractActorSimConfigElement implements IsSerializable, Serializable {

    String transactionName;
    EndpointLabel endpointLabel;

	private static final long serialVersionUID = 532031604752465534L;

	public EndpointActorSimConfigElement() { }

    public String getName() {
        throw new ToolkitRuntimeException("EndpointActorSimConfigElement.getName() should not be used.");
    }

    // The name attribute is used as the transaction name which is contained
    // in the EndpointLabel
	public EndpointActorSimConfigElement(EndpointLabel label, Endpoint endpoint) {
		this.transactionName = label.getTransType().getName();
        this.name = transactionName;
        this.endpointLabel = label;
		this.type = ParamType.ENDPOINT;
		this.setValue(endpoint.getValue());
	}

    public String getTransactionName() { return transactionName; }
    public EndpointLabel getEndpointLabel() { return endpointLabel; }
	
	public EndpointActorSimConfigElement setName(EndpointLabel label) {
		this.name = label.getTransType().getName();
		return this;
	}

}

