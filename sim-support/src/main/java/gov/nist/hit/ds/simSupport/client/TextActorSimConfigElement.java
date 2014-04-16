package gov.nist.hit.ds.simSupport.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import gov.nist.hit.ds.actorTransaction.client.ParamType;

import java.io.Serializable;

public class TextActorSimConfigElement extends AbstractActorSimConfigElement implements IsSerializable, Serializable {

	private static final long serialVersionUID = 3744552934075609429L;

	public TextActorSimConfigElement() { }
	
	public TextActorSimConfigElement(String name, String value) {
		this.name = name;
		this.type = ParamType.TEXT;
		this.setValue(value);
	}
	
	public TextActorSimConfigElement setName(String name) {
		this.name = name;
		return this;
	}

}
