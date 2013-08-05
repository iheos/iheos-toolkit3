package gov.nist.hit.ds.simSupport.client;


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
	SimId id;
	Date expires;
	boolean isExpired = false;
	List<SimulatorConfigElement> elements  = new ArrayList<SimulatorConfigElement>();
	
	
	public SimulatorConfig setExpiration(Date expires) {
		this.expires = expires;
		return this;
	}
	public SimulatorConfig setId(SimId id) {
		this.id = id;
		return this;
	}
	public boolean isExpired() { return isExpired; }
	public void setExpired(boolean is) { isExpired = is; }
	
	public boolean checkExpiration() {
		Date now = new Date();
		if (now.after(expires))
			isExpired = true;
		else
			isExpired = false;
		return isExpired;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("ActorSimulatorConfig:");
		buf.append(" id=").append(id);
		buf.append("\n\telements=[");
		for (SimulatorConfigElement asce : elements) {
			buf.append("\n\t\t").append(asce);
		}
		
		return buf.toString();
	}
	
	public SimulatorConfig() {
		
	}
	
	public SimulatorConfig add(List<SimulatorConfigElement> elementList) {
		elements.addAll(elementList);
		return this;
	}

	public SimulatorConfig add(SimulatorConfigElement confElement) {
		elements.add(confElement);
		return this;
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
	
	public List<SimulatorConfigElement> getAll() { return elements; }
	
	public List<SimulatorConfigElement> getEditable() {
		List<SimulatorConfigElement> user = new ArrayList<SimulatorConfigElement>();
		for (SimulatorConfigElement ele : elements) {
			if (ele.isEditable())
				user.add(ele);
		}
		return user;
	}
	
	public SimulatorConfigElement getByName(String name) {
		if (name == null)
			return null;
		
		for (SimulatorConfigElement ele : elements) {
			if (name.equals(ele.name))
				return ele;
		}
		return null;
	}
	
	public void deleteByName(String name) {
		SimulatorConfigElement ele = getByName(name);
		if (ele != null)
			elements.remove(ele);
	}	
	
	public SimId getId() {
		return id;
	}
	
}