package gov.nist.hit.ds.simSupport.config
import gov.nist.hit.ds.actorTransaction.*
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import groovy.transform.ToString
/**
 * Definition for an actor simulator. This focuses on a single
 * actor.  The class Simulator is a collection of ActorSimConfig 
 * objects since a simulator can contain multiple actors.
 * @author bill
 *
 */
@ToString(excludes="serialVersionUID, expires, isExpired, actorState")
public class SimConfig {
	ActorType actorType;
	List<TransactionSimConfigElement> elements  = new ArrayList<TransactionSimConfigElement>();

	SimConfig() { }
    SimConfig(ActorType _actorType) { actorType = _actorType; }

	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("SimulatorConfig:");
		for (TransactionSimConfigElement asce : elements) {
			buf.append("\n\t").append(asce);
		}
		
		return buf.toString();
	}

	SimConfig add(TransactionSimConfigElement confElement) {
		elements.add(confElement);
		return this;
	}
	List<TransactionSimConfigElement> getElements() { return elements; }

	/**
	 * Get the config element based on its displayName.
	 * @param name
	 * @return config element or null if not defined.
	 */
    TransactionSimConfigElement getByName(String name) {
		if (name == null)
			return null;
		
		for (TransactionSimConfigElement ele : elements) {
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

	/**
	 * Get config elements that match all the parameters.
	 * @param transTypes
	 * @param tlsTypes
	 * @param asyncTypes
	 * @return
	 */
    public List<TransactionSimConfigElement> findConfigs(List<TransactionType> transTypes, List<TlsType> tlsTypes, List<AsyncType> asyncTypes)  {
        List<TransactionSimConfigElement> simEles = new ArrayList<TransactionSimConfigElement>();


        TTypes tTypes = new TTypes(transTypes);
        Tls tls = new Tls(tlsTypes);
        Async async = new Async(asyncTypes);

        for (AbstractSimConfigElement ele : getElements()) {
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

    // Accessor functions for popular config elements

    boolean isSchemaCheck() { getByName(TransactionSimConfigElement.SCHEMACHECK).getValue() }
    boolean isModelCheck() { getByName(TransactionSimConfigElement.MODELCHECK).getValue() }
    boolean isCodingCheck() { getByName(TransactionSimConfigElement.CODINGCHECK).getValue() }
    boolean isSoapCheck() { getByName(TransactionSimConfigElement.SOAPCHECK).getValue() }
    String getMessageCallback() { getByName(TransactionSimConfigElement.MSGCALLBACK).getValue() }

}