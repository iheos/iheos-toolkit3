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
	List<TransactionSimConfigElement> transactions = new ArrayList<TransactionSimConfigElement>();

	def SimConfig() { }

	def SimConfig(ActorType actorType) { this.actorType = actorType; }
	
	def add(List<TransactionSimConfigElement> elementList) { transactions.addAll(elementList); }

	def add(TransactionSimConfigElement confElement) { transactions.add(confElement); }

	public TransactionSimConfigElement get(String name) {
		if (name == null) return null;
        return transactions.find { it.name == name}
	}
	
	/**
	 * Get endpoint given the type information. 
	 * @param transType
	 * @param tlsType
	 * @param asyncType
	 * @return endpoint or null if no endpoint matches the type information.
	 */
	EndpointValue getEndpoint(TransactionType transType, TlsType tlsType, AsyncType asyncType) {
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
	 * Get config transactions that match all the parameters.
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

        for (TransactionSimConfigElement endp : getTransactions()) {
            EndpointType elabel = endp.getEndpointType();
            if (!tTypes.has(elabel.getTransType())) continue;
            if (!tls.has(elabel.getTlsType())) continue;
            if (!async.has(elabel.getAsyncType())) continue;
            simEles.add(endp);
        }
        return simEles;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();

        buf.append("ActorSimulatorConfig:");
        for (TransactionSimConfigElement asce : transactions) {
            buf.append("\n\t").append(asce);
        }

        return buf.toString();
    }

}