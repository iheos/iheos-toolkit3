package gov.nist.hit.ds.utilities.initialization.tkProps.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Prop implements IsSerializable {
	public String name;
	public String value;
	
	public Prop(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public Prop() {}
}
