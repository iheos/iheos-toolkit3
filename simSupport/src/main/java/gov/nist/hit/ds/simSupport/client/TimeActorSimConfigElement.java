package gov.nist.hit.ds.simSupport.client;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TimeActorSimConfigElement extends AbstractActorSimConfigElement implements IsSerializable, Serializable {

	private static final long serialVersionUID = 2116238641744686908L;

	public TimeActorSimConfigElement(String name, Date date) {
		this.name = name;
		this.type = ParamType.TIME;
		this.setValue(date.toString());
	}
	
	public TimeActorSimConfigElement setName(String name) {
		this.name = name;
		return this;
	}
	
	
}
