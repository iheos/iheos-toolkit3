package gov.nist.hit.ds.actorTransaction

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.util.logging.Log4j

/**
 * Created by bmajur on 5/7/14.
 */
@Log4j
class ActorTransactionTypeFactory {
    static Map<String, TransactionType> transactionTypeMap = new HashMap<String, TransactionType>()
    static Map<String, ActorType> actorTypeMap = new HashMap<String, ActorType>()

    static def clear() {
        transactionTypeMap.clear()
        actorTypeMap.clear()
    }

    ActorType getActorType(String type) { return actorTypeMap.get(type) }
    TransactionType getTransactionType(String type) { return transactionTypeMap.get(type)}
    Collection<TransactionType> getTransactionTypes() { return transactionTypeMap.values() }

    List<String> getActorTypeNames() { return actorTypeMap.keySet() }

    void load(String config) {
        def records = new XmlSlurper().parseText(config)
        def transactions = records.transaction
        def actors = records.actor
        log.debug("${transactions.size()} transactions")
        log.debug("${actors.size()} actors")
        transactions.each { parseTransactionType(it) }
        actors.each { parseActorType(it) }
    }

    void parseTransactionType(tt) {
        TransactionType ttype = new TransactionType()
        ttype.id = tt.@id
        ttype.shortName = tt.@id
        ttype.name = tt.@displayName
        ttype.code = tt.@code
        ttype.asyncCode = tt.@asyncCode
        ttype.requestAction = tt.request.@action
        ttype.responseAction = tt.response.@action
        log.debug("Loading Transaction Type ${ttype.id}")
        transactionTypeMap.put(ttype.id, ttype)
        transactionTypeMap.put(ttype.code, ttype)
        transactionTypeMap.put(ttype.asyncCode, ttype)
    }

    void parseActorType(at) {
        ActorType atype = new ActorType()
        atype.name = at.@displayName
        atype.shortName = at.@id
        atype.actorSimFactoryClassName = at.simFactoryClass.@class
        log.debug("Loading Actor Type ${atype.getShortName()}")
        at.transaction.each { trans ->
            log.debug("Transaction included")
            def ttid = trans.@id
            log.debug("... transaction ${ttid}")
            def tt = transactionTypeMap.get(ttid.text())
            if (!tt)
                throw new ToolkitRuntimeException("Transaction ${ttid} not defined - ${transactionTypeMap.keySet()}")
            atype.getTransactionTypes().add(tt)
        }
        def properties = at.property
        properties.each {
            log.debug("... property ${it.@name} => ${it.@value}")
            atype.putProperty(it.@name.text(), it.@value.text())
        }
        actorTypeMap.put(atype.getShortName(), atype)
    }

//    void load2() {
//        URL url = this.class.getClassLoader().getResource('actorsAndTransactions.txt')
//        assert url != null
//        url.eachLine {
//            log.debug("Line is ${it}")
//            boolean isTransaction = TransactionTypeFactory.loadTransactionType(it.trim())
//            if (isTransaction) return
//            boolean isActor = ActorTypeFactory.loadActorType(it.trim())
//            if (isActor) return
//            log.error("Reading <${url}> - cannot load <${it}> as either a transaction or actor definition.")
//        }
//    }
}
