package gov.nist.hit.ds.actorTransaction

import gov.nist.hit.ds.actorTransaction.exceptions.InvalidActorTypeDefinitionException
import groovy.transform.ToString
import groovy.util.logging.Log4j
/**
 * Created by bill on 4/16/14.
 */
@Log4j
@ToString(includeFields=true, includes="shortName, directionalTransactionTypes, props")
class ActorType /* implements IsSerializable, Serializable */{
    String name
    String shortName
    String actorSimFactoryClassName
    List<DirectionalTransactionType> directionalTransactionTypes = new ArrayList<>()
    Map<String, String> props = new HashMap<String, String>()

    public String getName() { return name }
    public String getShortName() { return shortName }
//    public void setTransactionTypes(List<DirectionalTransactionType> tt) { directionalTransactionTypes = tt }

    boolean hasTransaction(TransactionType transactionType) {
        directionalTransactionTypes.find { it.transactionType == transactionType }
    }

//    String getActorProperty(String key) { return props.get(key) }
    void putActorProperty(String key, String value) { props.put(key, value) }
    boolean containsKey(String key) { return props.containsKey(key) }

    TransactionType findSimple(String transactionTypeName, boolean send) { return findTransactionType(transactionTypeName, send)}
    TransactionType findTransactionType(String transactionTypeName, boolean send) { findDirectional(transactionTypeName, send)?.transactionType }

    DirectionalTransactionType findDirectional(String transactionTypeName, boolean send) {
        directionalTransactionTypes.find {
            it.transactionType.identifiedBy(transactionTypeName) && it.client == send
        }
    }

    TransactionType getTransactionTypeFromRequestAction(String action, boolean client) {
        directionalTransactionTypes.find { dtt ->
            action == dtt.transactionType.requestAction && client == dtt.client
        }?.transactionType
    }

    void check() throws InvalidActorTypeDefinitionException {
        String val;
        String typeName = shortName

        val = name;
        if (val == null || val.equals(""))
            throw new InvalidActorTypeDefinitionException("${typeName}: displayName not defined");
        val = shortName;
        if (val == null || val.equals(""))
            throw new InvalidActorTypeDefinitionException("${typeName}: shortName not defined");
//        val = actorSimFactoryClassName;
//        if (val == null || val.equals(""))
//            throw new InvalidActorTypeDefinitionException("${typeName}: actorSimFactoryClass not defined");
        if (directionalTransactionTypes.size() == 0)
            throw new InvalidActorTypeDefinitionException("${typeName}: must define at least one transaction");
    }

    List<EndpointType> endpointTypes() {
        return directionalTransactionTypes.collect { ttype ->
           [ new EndpointType(ttype.transactionType, TlsType.NOTLS, AsyncType.SYNC) ,
             new EndpointType(ttype.transactionType, TlsType.TLS, AsyncType.SYNC) ]
        }.flatten()
    }


}
