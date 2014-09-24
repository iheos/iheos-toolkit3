package gov.nist.hit.ds.simSupport.client.configElementTypes;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

abstract public class SimConfigElement implements IsSerializable, Serializable {

	private static final long serialVersionUID = -5290538505675778269L;

	String name = null;
	boolean editable = false;

    public SimConfigElement() {   }

	public boolean isEditable() { return editable; }
	public SimConfigElement setEditable(boolean v) { editable = v; return this; }

	public String getName() { return name; }
    public void setName(String name) { this.name = name; }

}