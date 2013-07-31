package gov.nist.hit.ds.simSupport.config;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Definition for an actor simulator.
 * @author bill
 *
 */
public class SimulatorConfig implements IsSerializable {

	/**
	 * Globally unique id for this simulator
	 */
	String id;
	String type;
	Date expires;
	boolean isExpired = false;
	List<SimulatorConfigElement> elements  = new ArrayList<SimulatorConfigElement>();
	
	
	
	public boolean isExpired() { return isExpired; }
	public void isExpired(boolean is) { isExpired = is; }
	
	public boolean checkExpiration() {
		Date now = new Date();
		if (now.after(expires))
			isExpired = true;
		else
			isExpired = false;
		return isExpired;
	}
	
	// not sure what to do with the other attributes, leave alone for now
	public void add(SimulatorConfig asc) {
		for (SimulatorConfigElement ele : asc.elements) {
			if (getFixedByName(ele.name) == null)
				elements.add(ele);
		}
	}
			
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("ActorSimulatorConfig:");
		buf.append(" id=").append(id);
		buf.append(" type=").append(type);
		buf.append("\n\telements=[");
		for (SimulatorConfigElement asce : elements) {
			buf.append("\n\t\t").append(asce);
		}
		
		return buf.toString();
	}
	
	public SimulatorConfig() {
		
	}
	
	public SimulatorConfig(String id, String type, Date expiration) {
		this.id = id;
		this.type = type;
		expires = expiration;
	}
	
	public List<SimulatorConfigElement> elements() {
		return elements;
	}
	
	public void add(List<SimulatorConfigElement> elementList) {
		elements.addAll(elementList);
	}
	
	public Date getExpiration() {
		return expires;
	}
	
	public List<SimulatorConfigElement> getFixed() {
		List<SimulatorConfigElement> fixed = new ArrayList<SimulatorConfigElement>();
		for (SimulatorConfigElement ele : elements) {
			if (!ele.isEditable())
				fixed.add(ele);
		}
		return fixed;
	}
	
	public List<SimulatorConfigElement> getElements() { return elements; }
	
	public List<SimulatorConfigElement> getUser() {
		List<SimulatorConfigElement> user = new ArrayList<SimulatorConfigElement>();
		for (SimulatorConfigElement ele : elements) {
			if (ele.isEditable())
				user.add(ele);
		}
		return user;
	}
	
	public SimulatorConfigElement	getUserByName(String name) {
		if (name == null)
			return null;
		
		for (SimulatorConfigElement ele : elements) {
			if (name.equals(ele.name))
				return ele;
		}
		return null;
	}
	
	public SimulatorConfigElement	getFixedByName(String name) {
		if (name == null)
			return null;
		
		for (SimulatorConfigElement ele : elements) {
			if (name.equals(ele.name))
				return ele;
		}
		return null;
	}
	
	public void deleteFixedByName(String name) {
		SimulatorConfigElement ele = getFixedByName(name);
		if (ele != null)
			elements.remove(ele);
	}
	
	public void deleteUserByName(String name) {
		SimulatorConfigElement ele = getUserByName(name);
		if (ele != null)
			elements.remove(ele);
	}
	
	
	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public SimulatorConfigElement get(String name) {
		for (SimulatorConfigElement ele : elements) {
			if (ele.name.equals(name))
				return ele;
		}
		return null;
	}
		
	public String getDefaultName() {
		return get("Name").asString() + "." + getType();
	}
	
}