package gov.nist.hit.ds.simSupport.client;

import gov.nist.hit.ds.actorTransaction.EndpointLabel;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EndpointActorSimConfigElement extends
		AbstractActorSimConfigElement implements IsSerializable, Serializable {

	private static final long serialVersionUID = 532031604752465534L;

	public EndpointActorSimConfigElement(EndpointLabel label, String endpoint) {
		this.name = label.get();
		this.type = ParamType.ENDPOINT;
		this.setValue(endpoint);
	}
	
	public EndpointActorSimConfigElement setName(EndpointLabel label) {
		this.name = label.get();
		return this;
	}

}

