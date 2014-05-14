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
    List<TransactionType> transactionTypes = []

    public String getName() { return name }
    public String getShortName() { return shortName }
    public void setTransactionTypes(List<TransactionType> tt) { transactionTypes = tt }

    boolean hasTransaction(TransactionType transactionType) {
        transactionTypes.find { it == transactionType }
    }

    TransactionType find(String transactionTypeName) {
        transactionTypes.find {
            it.identifiedBy(transactionTypeName)
        }
    }

    void check() throws InvalidActorTypeDefinitionException {
        String val;
        ActorType at = this;

        val = at.name;
        if (val == null || val.equals(""))
            throw new InvalidActorTypeDefinitionException("name not defined");
        val = at.shortName;
        if (val == null || val.equals(""))
            throw new InvalidActorTypeDefinitionException("shortName not defined");
        val = at.actorSimFactoryClassName;
        if (val == null || val.equals(""))
            throw new InvalidActorTypeDefinitionException("actorSimFactoryClass not defined");
        if (at.transactionTypes.size() == 0)
            throw new InvalidActorTypeDefinitionException("must define at least one transaction");
    }

}
