package gov.nist.hit.ds.simSupport.client;


import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.google.gwt.user.client.rpc.IsSerializable;
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
abstract public class AbstractActorSimConfigElement implements IsSerializable, Serializable {

	private static final long serialVersionUID = -5290538505675778269L;

	String name = null;
	ParamType type;
	String  value = null;
	boolean editable = false;

	public AbstractActorSimConfigElement() {   }

	public AbstractActorSimConfigElement setType(ParamType type) {
		this.type = type;
		return this;
	}

	public boolean isEditable() { return editable; }
	public AbstractActorSimConfigElement setEditable(boolean v) { editable = v; return this; }

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

	public AbstractActorSimConfigElement setValue(String value) {
		this.value = value;
		return this;
	}

//	public AbstractActorSimConfigElement setValue(Boolean o) { 
//		value = o.toString();
//		return this;
//	}

}