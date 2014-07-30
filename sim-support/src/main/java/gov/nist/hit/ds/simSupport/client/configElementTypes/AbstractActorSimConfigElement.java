package gov.nist.hit.ds.simSupport.client.configElementTypes;

import com.google.gwt.user.client.rpc.IsSerializable;
import gov.nist.hit.ds.simSupport.client.ParamType;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
abstract public class AbstractActorSimConfigElement implements IsSerializable, Serializable {

	private static final long serialVersionUID = -5290538505675778269L;

	String name = null;
	ParamType type;
	String  value = null;
	boolean editable = false;

    public OMElement toXML() {
        OMElement top = XmlUtil.om_factory.createOMElement(type.toString(), null);
        OMAttribute typeAtt = XmlUtil.om_factory.createOMAttribute("name", null, name);
        OMAttribute valueAtt = XmlUtil.om_factory.createOMAttribute("value", null, value);
        top.addAttribute(typeAtt);
        top.addAttribute(valueAtt);
        return top;
    }

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

//		buf.append(" editable=").append(isEditable());

		return buf.toString();
	}

	public AbstractActorSimConfigElement setValue(String value) {
		this.value = value;
		return this;
	}

}