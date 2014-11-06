package gov.nist.hit.ds.simSupport.client.configElementTypes;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class BooleanSimConfigElement extends SimConfigElement implements IsSerializable, Serializable {
    Boolean value;
	private static final long serialVersionUID = -1150340414189551656L;

    public BooleanSimConfigElement() { }
	
	public BooleanSimConfigElement(String name, boolean value) {
        setName(name);
        setValue(value);
	}

	public void setValue(Boolean o) { value = o; }
    public Boolean getValue() { return value; }

}
