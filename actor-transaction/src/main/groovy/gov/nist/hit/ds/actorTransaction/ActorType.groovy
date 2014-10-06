package gov.nist.hit.ds.actorTransaction

import gov.nist.hit.ds.actorTransaction.exceptions.InvalidActorTypeDefinitionException
import groovy.transform.ToString
import groovy.util.logging.Log4j
/**
 * Created by bill on 4/16/14.
 */
@Log4j
@ToString(includeFields=true, includes="shortName, transactionTypes, props")
class ActorType /* implements IsSerializable, Serializable */{
    String name
    String shortName
    String actorSimFactoryClassName
    List<TransactionType> transactionTypes = new ArrayList<TransactionType>()
    Map<String, String> props = new HashMap<String, String>()

    public String getName() { return name }
    public String getShortName() { return shortName }
    public void setTransactionTypes(List<TransactionType> tt) { transactionTypes = tt }

    boolean hasTransaction(TransactionType transactionType) {
        transactionTypes.find { it == transactionType }
    }

    String getActorProperty(String key) { return props.get(key) }
    void putActorProperty(String key, String value) { props.put(key, value) }
    boolean containsKey(String key) { return props.containsKey(key) }

    TransactionType find(String transactionTypeName) {
        transactionTypes.find {
            it.identifiedBy(transactionTypeName)
        }
    }

//    String toString() {
//        return "ActorType: ${name} (${shortName} with ${properties.keySet()})"
//    }

    void check() throws InvalidActorTypeDefinitionException {
        String val;
        String typeName = shortName

        val = name;
        if (val == null || val.equals(""))
            throw new InvalidActorTypeDefinitionException("${typeName}: name not defined");
        val = shortName;
        if (val == null || val.equals(""))
            throw new InvalidActorTypeDefinitionException("${typeName}: shortName not defined");
//        val = actorSimFactoryClassName;
//        if (val == null || val.equals(""))
//            throw new InvalidActorTypeDefinitionException("${typeName}: actorSimFactoryClass not defined");
        if (transactionTypes.size() == 0)
            throw new InvalidActorTypeDefinitionException("${typeName}: must define at least one transaction");
    }

    List<EndpointType> endpointTypes() {
        return transactionTypes.collect { ttype ->
           [ new EndpointType(ttype, TlsType.NOTLS, AsyncType.SYNC) ,
             new EndpointType(ttype, TlsType.TLS, AsyncType.SYNC) ]
        }.flatten()
    }


}
