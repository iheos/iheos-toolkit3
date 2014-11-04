package gov.nist.hit.ds.simSupport.client;

import gov.nist.hit.ds.actorTransaction.*;
import gov.nist.hit.ds.simSupport.client.configElementTypes.SimConfigElement;
import gov.nist.hit.ds.simSupport.client.configElementTypes.TransactionSimConfigElement;
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue;
import groovy.transform.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Definition for an actor simulator. This focuses on a single
 * actor.  The class Simulator is a collection of ActorSimConfig 
 * objects since a simulator can contain multiple actors.
 * @author bill
 *
 */
@ToString(excludes="serialVersionUID, expires, isExpired, actorState")
public class ActorSimConfig {

	private static final long serialVersionUID = -736965164284950123L;
	public ActorType actorType;
	public List<SimConfigElement> elements  = new ArrayList<SimConfigElement>();

	public ActorSimConfig() { }

	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("ActorSimulatorConfig:");
		for (SimConfigElement asce : elements) {
			buf.append("\n\t").append(asce);
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
	public ActorSimConfig add(List<SimConfigElement> elementList) {
		elements.addAll(elementList);
		return this;
	}

	/**
	 * Add a config element.
	 * @param confElement
	 * @return
	 */
	public ActorSimConfig add(SimConfigElement confElement) {
		elements.add(confElement);
		return this;
	}

//	public Date getExpiration() {
//		return expires;
//	}
	
	/**
	 * Return all the fixed (not editable in the UI) config elements.
	 * @return
	 */
	public List<SimConfigElement> getFixed() {
		List<SimConfigElement> fixed = new ArrayList<SimConfigElement>();
		for (SimConfigElement ele : elements) {
			if (!ele.isEditable())
				fixed.add(ele);
		}
		return fixed;
	}
	
	/**
	 * Return all config elements.
	 * @return
	 */
	public List<SimConfigElement> getElements() { return elements; }

    public List<SimConfigElement> getElements(Class clazz) {
        List<SimConfigElement> eles = new ArrayList<SimConfigElement>();
        for (SimConfigElement ele : elements) {
            if (ele.getClass().getName().equals(clazz.getName())) eles.add(ele);
        }
        return eles;
    }
	
	/**
	 * Return all the config elements that can be edited in the UI.
	 * @return
	 */
	public List<SimConfigElement> getEditable() {
		List<SimConfigElement> user = new ArrayList<SimConfigElement>();
		for (SimConfigElement ele : elements) {
			if (ele.isEditable())
				user.add(ele);
		}
		return user;
	}
	
	/**
	 * Get the config element based on its displayName.
	 * @param name
	 * @return config element or null if not defined.
	 */
	public SimConfigElement getByName(String name) {
		if (name == null)
			return null;
		
		for (SimConfigElement ele : elements) {
			if (name.equals(ele.getName()))
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
	public EndpointValue getEndpoint(TransactionType transType, TlsType tlsType, AsyncType asyncType) {
        List<TransactionSimConfigElement> configs = findConfigs(
                new ArrayList<TransactionType>(Arrays.asList(transType)),
                new ArrayList<TlsType>(Arrays.asList(tlsType)),
                new ArrayList<AsyncType>(Arrays.asList(asyncType)));
		if (configs.isEmpty())
			return null;
		return configs.get(0).getEndpointValue();
	}

    public SimConfigElement getByClass(Class claz) {
        for (SimConfigElement e : elements) {
            if (e.getClass().getName().equals(claz.getName())) return e;
        }
        return null;
    }
	
	/**
	 * Get config elements that match all the parameters.
	 * @param transTypes
	 * @param tlsTypes
	 * @param asyncTypes
	 * @return
	 */
    public List<TransactionSimConfigElement> findConfigs(List<TransactionType> transTypes, List<TlsType> tlsTypes, List<AsyncType> asyncTypes)  {
        List<TransactionSimConfigElement> simEles = new ArrayList<TransactionSimConfigElement>();

        class Tls {
            List<TlsType> tlsTypes;
            Tls(List<TlsType> types) { tlsTypes = types; }
            boolean has(TlsType type) {
                if (tlsTypes == null) return false;
                for (int i=0; i<tlsTypes.size(); i++)
                    if (type == tlsTypes.get(i)) return true;
                return false;
            }
        }

        class Async {
            List<AsyncType> asyncTypes;
            Async(List<AsyncType> types) { asyncTypes = types; }
            boolean has(AsyncType type) {
                if (asyncTypes == null) return false;
                for (int i=0; i<asyncTypes.size(); i++)
                    if (type == asyncTypes.get(i)) return true;
                return false;
            }
        }

        class TTypes {
            List<TransactionType> transTypes;
            TTypes(List<TransactionType> types) { transTypes = types; }
            boolean has(TransactionType type) {
                if (transTypes == null) return false;
                for (int i=0; i<transTypes.size(); i++)
                    if (type == transTypes.get(i)) return true;
                return false;
            }
        }

        TTypes tTypes = new TTypes(transTypes);
        Tls tls = new Tls(tlsTypes);
        Async async = new Async(asyncTypes);

        for (SimConfigElement ele : getElements()) {
            if (!(ele instanceof TransactionSimConfigElement)) continue;
            TransactionSimConfigElement endp = (TransactionSimConfigElement) ele;
            EndpointType elabel = endp.getEndpointType();
            if (!tTypes.has(elabel.getTransType())) continue;
            if (!tls.has(elabel.getTlsType())) continue;
            if (!async.has(elabel.getAsyncType())) continue;
            simEles.add(endp);
        }

        return simEles;
    }

	/**
	 * Delete the config element based on its displayName.
	 * @param name
	 */
	public void deleteByName(String name) {
		SimConfigElement ele = getByName(name);
		if (ele != null)
			elements.remove(ele);
	}	
	
	public ActorType getActorType() {
		return actorType;
	}

	public boolean isActorType(ActorType actorType2) {
		return actorType.equals(actorType2);
	}
	
}