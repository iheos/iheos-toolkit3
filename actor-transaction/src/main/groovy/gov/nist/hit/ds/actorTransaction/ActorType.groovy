package gov.nist.hit.ds.actorTransaction

import com.google.gwt.user.client.rpc.IsSerializable
import gov.nist.hit.ds.actorTransaction.exceptions.InvalidActorTypeDefinitionException

/**
 * Created by bill on 4/16/14.
 */
class ActorType implements IsSerializable, Serializable {
    String name
    String shortName
    String actorSimFactoryClassName
    List<TransactionType> transactionTypes = new ArrayList<TransactionType>()
    Map<String, String> properties = new HashMap<String, String>()

    public String getName() { return name }
    public String getShortName() { return shortName }
    public void setTransactionTypes(List<TransactionType> tt) { transactionTypes = tt }

    boolean hasTransaction(TransactionType transactionType) {
        transactionTypes.find { it == transactionType }
    }

    String getProperty(String key) { return properties.get(key) }
    void putProperty(String key, String value) { properties.put(key, value) }
    boolean containsKey(String key) { return properties.containsKey(key) }

    TransactionType find(String transactionTypeName) {
        transactionTypes.find {
            it.identifiedBy(transactionTypeName)
        }
    }

    String toString() {
        return "ActorType: ${name} (${shortName} with ${properties.keySet()})"
    }

    void check(String typeName) throws InvalidActorTypeDefinitionException {
        String val;

        val = name;
        if (val == null || val.equals(""))
            throw new InvalidActorTypeDefinitionException("${typeName}: name not defined");
        val = shortName;
        if (val == null || val.equals(""))
            throw new InvalidActorTypeDefinitionException("${typeName}: shortName not defined");
        val = actorSimFactoryClassName;
        if (val == null || val.equals(""))
            throw new InvalidActorTypeDefinitionException("${typeName}: actorSimFactoryClass not defined");
        if (transactionTypes.size() == 0)
            throw new InvalidActorTypeDefinitionException("${typeName}: must define at least one transaction");
    }

}
