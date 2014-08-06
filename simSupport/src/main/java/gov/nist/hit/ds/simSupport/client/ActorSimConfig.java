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
 * Definition for an actor simulator. This focuses on a single
 * actor.  The class Simulator is a collection of ActorSimConfig 
 * objects since a simulator can contain multiple actors.
 * @author bill
 *
 */
public class ActorSimConfig implements IsSerializable, Serializable {

	private static final long serialVersionUID = -736965164284950123L;
	ActorType actorType;
	Date expires;
	boolean isExpired = false;
	List<AbstractActorSimConfigElement> elements  = new ArrayList<AbstractActorSimConfigElement>();
	Object actorState = null;
	
	public ActorSimConfig() { }
	
	/**
	 * Set expiration date
	 * @param expires
	 * @return
	 */
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
		for (AbstractActorSimConfigElement asce : elements) {
			buf.append("\n\t\t").append(asce);
		}
		
		return buf.toString();
	}
	
	/**
	 * Build an ActorSimConfig based on the ActorType
	 * @param actorType
	 */
	public ActorSimConfig(ActorType actorType) {
		this.actorType = actorType;
	}
	
	/**
	 * Add a collection of config elements
	 * @param elementList
	 * @return
	 */
	public ActorSimConfig add(List<AbstractActorSimConfigElement> elementList) {
		elements.addAll(elementList);
		return this;
	}

	/**
	 * Add a config element.
	 * @param confElement
	 * @return
	 */
	public ActorSimConfig add(AbstractActorSimConfigElement confElement) {
		elements.add(confElement);
		return this;
	}

	public Date getExpiration() {
		return expires;
	}
	
	/**
	 * Return all the fixed (not editable in the UI) config elements.
	 * @return
	 */
	public List<AbstractActorSimConfigElement> getFixed() {
		List<AbstractActorSimConfigElement> fixed = new ArrayList<AbstractActorSimConfigElement>();
		for (AbstractActorSimConfigElement ele : elements) {
			if (!ele.isEditable())
				fixed.add(ele);
		}
		return fixed;
	}
	
	/**
	 * Return all config elements.
	 * @return
	 */
	public List<AbstractActorSimConfigElement> getAll() { return elements; }
	
	/**
	 * Return all the config elements that can be edited in the UI.
	 * @return
	 */
	public List<AbstractActorSimConfigElement> getEditable() {
		List<AbstractActorSimConfigElement> user = new ArrayList<AbstractActorSimConfigElement>();
		for (AbstractActorSimConfigElement ele : elements) {
			if (ele.isEditable())
				user.add(ele);
		}
		return user;
	}
	
	/**
	 * Get the config element based on its name. 
	 * @param name
	 * @return config element or null if not defined.
	 */
	public AbstractActorSimConfigElement getByName(String name) {
		if (name == null)
			return null;
		
		for (AbstractActorSimConfigElement ele : elements) {
			if (name.equals(ele.name))
				return ele;
		}
		return null;
	}
	
	/**
	 * Get endpoint given the type information. 
	 * @param transType
	 * @param tlsType
	 * @param asyncType
	 * @return endpoint or null if no endpoint matches the type information.
	 */
	public String getEndpoint(TransactionType transType, TlsType tlsType, AsyncType asyncType) {
		List<AbstractActorSimConfigElement> configs = findConfigs(new TransactionType[] { transType }, new TlsType[] { tlsType }, new AsyncType[] { asyncType });
		if (configs.isEmpty())
			return null;
		return configs.get(0).getValue();
	}
	
	/**
	 * Get config elements that match all the parameters.
	 * @param transTypes
	 * @param tlsTypes
	 * @param asyncTypes
	 * @return
	 */
	public List<AbstractActorSimConfigElement> findConfigs(TransactionType[] transTypes, TlsType[] tlsTypes, AsyncType[] asyncTypes)  {
		List<AbstractActorSimConfigElement> simEles = new ArrayList<AbstractActorSimConfigElement>();
		
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
		
		class TTypes {
			TransactionType[] transTypes;
			TTypes(TransactionType[] types) { transTypes = types; }
			boolean has(TransactionType type) {
				if (transTypes == null) return false;
				for (int i=0; i<transTypes.length; i++) 
					if (type == transTypes[i]) return true;
				return false;
			}
		}
		
		TTypes tTypes = new TTypes(transTypes);
		Tls tls = new Tls(tlsTypes);
		Async async = new Async(asyncTypes);
		
		for (AbstractActorSimConfigElement ele : getAll()) {
			if (ele.getType() != ParamType.ENDPOINT) continue;
			String name = ele.getName();
			EndpointLabel elabel = new EndpointLabel(name);
			if (!tTypes.has(elabel.getTransType())) continue;
			if (!tls.has(elabel.getTlsType())) continue;
			if (!async.has(elabel.getAsyncType())) continue;
			simEles.add(ele);
		}
		
		return simEles;
	}
	
	/**
	 * Delete the config element based on its name.
	 * @param name
	 */
	public void deleteByName(String name) {
		AbstractActorSimConfigElement ele = getByName(name);
		if (ele != null)
			elements.remove(ele);
	}	
	
	public ActorType getActorType() {
		return actorType;
	}

	public boolean isActorType(ActorType actorType2) {
		return actorType.equals(actorType2);
	}
	
	public Object getActorState() {
		return actorState;
	}
}