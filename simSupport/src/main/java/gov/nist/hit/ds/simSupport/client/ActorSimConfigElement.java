package gov.nist.hit.ds.simSupport.client;


import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ActorSimConfigElement implements IsSerializable, Serializable {

	private static final long serialVersionUID = -5290538505675778269L;
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

	public ActorSimConfigElement() {   }

	public ActorSimConfigElement setType(ParamType type) {
		this.type = type;
		return this;
	}
	public ActorSimConfigElement setValue(String value) {
		this.value = value;
		return this;
	}
	public ActorSimConfigElement setName(String name) {
		this.name = name;
		return this;
	}

	public ActorSimConfigElement(String name, ParamType type, Boolean value) {
		this.name = name;
		this.type = type;
		setValue(value);
	}

	public ActorSimConfigElement(String name, ParamType type, String value) {
		this.name = name;
		this.type = type;
		setValue(value);
	}

	public boolean isEditable() { return editable; }
	public ActorSimConfigElement setEditable(boolean v) { editable = v; return this; }

	public String asString() {
		return value;
	}
	
	public ParamType getType() { return type; }
	public String getValue() { return value; }
	public String getName() { return name; }

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

	public ActorSimConfigElement setValue(Boolean o) { 
		value = o.toString();
		return this;
	}

}