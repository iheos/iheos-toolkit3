package gov.nist.hit.ds.simSupport.client;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TextActorSimConfigElement extends AbstractActorSimConfigElement implements IsSerializable, Serializable {

	private static final long serialVersionUID = 3744552934075609429L;

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
