package gov.nist.hit.ds.simSupport.client;


import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.EndpointLabel;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Definition for an actor simulator.
 * @author bill
 *
 */
public class ActorSimConfig implements IsSerializable, Serializable {

	private static final long serialVersionUID = -736965164284950123L;
	/**
	 * Globally unique id for this simulator
	 */
	ActorType actorType;
	Date expires;
	boolean isExpired = false;
	List<ActorSimConfigElement> elements  = new ArrayList<ActorSimConfigElement>();
	
	
	public ActorSimConfig setExpiration(Date expires) {
		this.expires = expires;
		return this;
	}
	public boolean isExpired() { return isExpired; }
	public void setExpired(boolean is) { isExpired = is; }
	
	public boolean hasExpired() {
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
		buf.append("\n\telements=[");
		for (ActorSimConfigElement asce : elements) {
			buf.append("\n\t\t").append(asce);
		}
		
		return buf.toString();
	}
	
	public ActorSimConfig(ActorType actorType) {
		this.actorType = actorType;
	}
	
	public ActorSimConfig add(List<ActorSimConfigElement> elementList) {
		elements.addAll(elementList);
		return this;
	}

	public ActorSimConfig add(ActorSimConfigElement confElement) {
		elements.add(confElement);
		return this;
	}

	public Date getExpiration() {
		return expires;
	}
	
	public List<ActorSimConfigElement> getFixed() {
		List<ActorSimConfigElement> fixed = new ArrayList<ActorSimConfigElement>();
		for (ActorSimConfigElement ele : elements) {
			if (!ele.isEditable())
				fixed.add(ele);
		}
		return fixed;
	}
	
	public List<ActorSimConfigElement> getAll() { return elements; }
	
	public List<ActorSimConfigElement> getEditable() {
		List<ActorSimConfigElement> user = new ArrayList<ActorSimConfigElement>();
		for (ActorSimConfigElement ele : elements) {
			if (ele.isEditable())
				user.add(ele);
		}
		return user;
	}
	
	public ActorSimConfigElement getByName(String name) {
		if (name == null)
			return null;
		
		for (ActorSimConfigElement ele : elements) {
			if (name.equals(ele.name))
				return ele;
		}
		return null;
	}
	
	public List<ActorSimConfigElement> findConfigs(TransactionType[] transTypes, TlsType[] tlsTypes, AsyncType[] asyncTypes) {
		List<ActorSimConfigElement> simEles = new ArrayList<ActorSimConfigElement>();
		
		class Tls {
			TlsType[] tlsTypes;
			Tls(TlsType[] types) { tlsTypes = types; }
			boolean has(TlsType type) {
				if (tlsTypes == null) return false;
				for (int i=0; i<tlsTypes.length; i++)
					if (type == tlsTypes[i]) return true;
				return false;
			}
		}
		
		class Async {
			AsyncType[] asyncTypes;
			Async(AsyncType[] types) { asyncTypes = types; }
			boolean has(AsyncType type) {
				if (asyncTypes == null) return false;
				for (int i=0; i<asyncTypes.length; i++)
					if (type == asyncTypes[i]) return true;
				return false;
			}
		}
		
		class Type {
			TransactionType[] transTypes;
			Type(TransactionType[] types) { transTypes = types; }
			boolean has(TransactionType type) {
				if (transTypes == null) return false;
				for (int i=0; i<transTypes.length; i++) 
					if (type == transTypes[i]) return true;
				return false;
			}
		}
		
		Type types = new Type(transTypes);
		Tls tls = new Tls(tlsTypes);
		Async async = new Async(asyncTypes);
		
		for (ActorSimConfigElement ele : getAll()) {
			if (ele.getType() != ParamType.ENDPOINT) continue;
			String value = ele.getValue();
			EndpointLabel elabel = new EndpointLabel(value);
			if (!types.has(elabel.getTransType())) continue;
			if (!tls.has(elabel.getTlsType())) continue;
			if (!async.has(elabel.getAsyncType())) continue;
			simEles.add(ele);
		}
		
		return simEles;
	}
	
	public void deleteByName(String name) {
		ActorSimConfigElement ele = getByName(name);
		if (ele != null)
			elements.remove(ele);
	}	
	
	public ActorType getActorType() {
		return actorType;
	}
	
}