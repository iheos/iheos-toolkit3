package gov.nist.hit.ds.simSupport.client;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BooleanActorSimConfigElement extends AbstractActorSimConfigElement implements IsSerializable, Serializable {

	private static final long serialVersionUID = -1150340414189551656L;

	public BooleanActorSimConfigElement() { }
	
	public BooleanActorSimConfigElement(String name, boolean value) {
		this.name = name;
		this.type = ParamType.BOOLEAN;
		this.setValue(value);
	}

	public BooleanActorSimConfigElement setName(String name) {
		this.name = name;
		return this;
	}

	public AbstractActorSimConfigElement setValue(Boolean o) { 
		value = o.toString();
		return this;
	}

}
