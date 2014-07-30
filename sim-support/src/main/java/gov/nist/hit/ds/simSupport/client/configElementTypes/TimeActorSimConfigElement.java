package gov.nist.hit.ds.simSupport.client.configElementTypes;

import com.google.gwt.user.client.rpc.IsSerializable;
import gov.nist.hit.ds.simSupport.client.ParamType;

import java.io.Serializable;

public class TimeActorSimConfigElement extends AbstractActorSimConfigElement implements IsSerializable, Serializable {

	private static final long serialVersionUID = 2116238641744686908L;

	public TimeActorSimConfigElement(String name, String date) {
		this.name = name;
		this.type = ParamType.TIME;
		this.setValue(date);
	}
	
	public TimeActorSimConfigElement() { }
	
	public TimeActorSimConfigElement setName(String name) {
		this.name = name;
		return this;
	}
}
