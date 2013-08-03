package gov.nist.hit.ds.simSupport.client;


import com.google.gwt.user.client.rpc.IsSerializable;

public class SimulatorConfigElement implements IsSerializable {

	/**
	 * Parameter name
	 */
	public String name = null;
	/**
	 * Parameter type
	 */
	ParamType type;

	String  value = null;
	boolean editable = false;
	
	public SimulatorConfigElement() {   }

	public SimulatorConfigElement(String name, ParamType type, Boolean value) {
		this.name = name;
		this.type = type;
		setValue(value);
	}

	public SimulatorConfigElement(String name, ParamType type, String value) {
		this.name = name;
		this.type = type;
		setValue(value);
	}

	public boolean isEditable() { return editable; }
	public void setEditable(boolean v) { editable = v; }

	public String asString() {
		return value;
	}

	public Boolean asBoolean() { 
		return Boolean.valueOf(value);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("name=").append(name);
		buf.append(" type=").append(type);
		buf.append(" value=").append(value);

		buf.append(" editable=").append(isEditable());

		return buf.toString();
	}

	public void setValue(Boolean o) { value = o.toString(); }
	public void setValue(String o) { value = o; }

}