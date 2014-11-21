package gov.nist.hit.ds.simSupport.config;

import com.google.gwt.user.client.rpc.IsSerializable

public class TextSimConfigElement extends AbstractSimConfigElement implements IsSerializable, Serializable {
    String value;

	private static final long serialVersionUID = 3744552934075609429L;

	public TextSimConfigElement() { }
	
	public TextSimConfigElement(String name, String value) {
		setName(name);
		this.value = value;
	}

    public String getValue() { return value; }
    public void setValue(String text) { value = text; }
}
