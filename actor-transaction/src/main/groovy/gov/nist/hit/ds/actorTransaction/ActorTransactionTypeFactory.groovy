package gov.nist.hit.ds.actorTransaction

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.util.logging.Log4j

/**
 * Created by bmajur on 5/7/14.
 */
@Log4j
class ActorTransactionTypeFactory {
    static Map<String, TransactionType> transactionByName
    static Map<String, ActorType> actorByName
    static Map<String, TransactionType> transactionByRequestAction
    static Map<String, TransactionType> transactionByResponseAction

    ActorTransactionTypeFactory() { init() }

    def clear() {
        transactionByName.clear()
        actorByName.clear()
        transactionByRequestAction.clear()
        transactionByResponseAction.clear()
    }

    static def init() {
        if (actorByName) return
        transactionByName = new HashMap<>()
        actorByName = new HashMap<String, ActorType>()
        transactionByRequestAction = new HashMap<>()
        transactionByResponseAction = new HashMap<>()
    }

    ActorType getActorTypeIfAvailable(String type) { return actorByName.get(type) }

    TransactionType getTransactionTypeIfAvailable(String type) { return transactionByName.get(type) }

    ActorType getActorType(String type) {
        ActorType actorType = actorByName.get(type)
        if (actorType) return actorType
        throw new ToolkitRuntimeException("ActorType ${type} does not exist.")
    }

    TransactionType getTransactionType(String type) {
        TransactionType transactionType = transactionByName.get(type)
        if (transactionType) return transactionType
        throw new ToolkitRuntimeException("TransactionType ${type} does not exist. Types ${transactionByName.keySet()} are defined")
    }

    List<TransactionType> getTransactionTypes() { return transactionByName.values() as List}

    List<String> getActorTypeNames() { return actorByName.keySet() }

    TransactionType getTransactionTypeFromRequestAction(String action) { return transactionByRequestAction.get(action)}
    TransactionType getTransactionTypeFromResponseAction(String action) { return transactionByResponseAction.get(action)}

    static List<String> getKnownRequestActions() {
        transactionByRequestAction.collect { action, trans -> action }
    }

    void loadFromResource(String resourceName) { new ActorTransactionTypeDAO(this).loadFromResource(resourceName) }
    void loadFromString(String config) { new ActorTransactionTypeDAO(this).loadFromString(config)}
}
