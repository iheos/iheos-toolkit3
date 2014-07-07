package gov.nist.hit.ds.actorTransaction

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.util.logging.Log4j

/**
 * Created by bmajur on 5/7/14.
 */
@Log4j
class ActorTransactionTypeFactory {
    private static Map<String, TransactionType> transactionTypeMap
    private static Map<String, ActorType> actorTypeMap

    ActorTransactionTypeFactory() { init() }

    def clear() {
        transactionTypeMap.clear()
        actorTypeMap.clear()
    }

    static def init() {
        if (actorTypeMap) return
        transactionTypeMap = new HashMap<String, TransactionType>()
        actorTypeMap = new HashMap<String, ActorType>()
    }

    ActorType getActorTypeIfAvailable(String type) { return actorTypeMap.get(type) }

    TransactionType getTransactionTypeIfAvailable(String type) { return transactionTypeMap.get(type) }

    ActorType getActorType(String type) {
        ActorType actorType = actorTypeMap.get(type)
        if (actorType) return actorType
        throw new ToolkitRuntimeException("ActorType ${type} does not exist.")
    }

    TransactionType getTransactionType(String type) {
        TransactionType transactionType = transactionTypeMap.get(type)
        if (transactionType) return transactionType
        throw new ToolkitRuntimeException("TransactionType ${type} does not exist.")
    }

    Collection<TransactionType> getTransactionTypes() { return transactionTypeMap.values() }

    List<String> getActorTypeNames() { return actorTypeMap.keySet() }

    void loadFromResource(String resourceName) {
        loadFromString(this.class.getClassLoader().getResourceAsStream(resourceName).text)
    }

    void loadFromString(String config) {
        def records = new XmlSlurper().parseText(config)
        def transactions = records.transaction
        def actors = records.actor
        transactions.each { parseTransactionType(it) }
        actors.each { parseActorType(it) }
    }

    void parseTransactionType(tt) {
        TransactionType ttype = new TransactionType()
        ttype.id = tt.@id
        ttype.shortName = tt.@id
        ttype.name = tt.@id
        ttype.code = tt.@code
        ttype.asyncCode = tt.@asyncCode
        ttype.requestAction = tt.request.@action
        ttype.responseAction = tt.response.@action
        ttype.implementationClassName = tt.@class
        transactionTypeMap.put(ttype.id, ttype)
        transactionTypeMap.put(ttype.code, ttype)
        transactionTypeMap.put(ttype.asyncCode, ttype)
        tt.property.each {
            String name = it.@name.text()
            String value = it.@value.text()
            ttype.putTransactionProperty(name, value)
        }
        log.debug("Loading ${ttype}")
    }

    void parseActorType(at) {
        ActorType atype = new ActorType()
        atype.name = at.@displayName
        atype.shortName = at.@id
        atype.actorSimFactoryClassName = at.simFactoryClass.@class
        at.transaction.each { trans ->
            def ttid = trans.@id
            def tt = transactionTypeMap.get(ttid.text())
            if (!tt)
                throw new ToolkitRuntimeException("Transaction ${ttid} not defined - ${transactionTypeMap.keySet()}")
            atype.getTransactionTypes().add(tt)
        }
        at.property.each {
            String name = it.@name.text()
            String value = it.@value.text()
            atype.putActorProperty(name, value)
        }
        actorTypeMap.put(atype.getShortName(), atype)
        log.debug("Loading ${atype}")
    }

}
