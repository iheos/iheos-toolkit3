package gov.nist.hit.ds.simSupport.client.configElementTypes;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class TextSimConfigElement extends SimConfigElement implements IsSerializable, Serializable {
    String value;

	private static final long serialVersionUID = 3744552934075609429L;

	public TextSimConfigElement() { }
	
	public TextSimConfigElement(String name, String value) {
		setName(name);
		this.value = value;
	}
}
