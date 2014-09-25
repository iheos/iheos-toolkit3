package gov.nist.hit.ds.actorTransaction

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.util.logging.Log4j

/**
 * Created by bmajur on 8/27/14.
 */
@Log4j
class ActorTransactionTypeDAO {
    ActorTransactionTypeFactory fact

    ActorTransactionTypeDAO(ActorTransactionTypeFactory _fact) { fact = _fact }

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
        ttype.implementationClassName = tt.implClass.@value
        if (tt.params) {
            ttype.multiPart = tt.params.@multiPart == 'true'
            ttype.soap = tt.params.@soap == 'true'
        }
        fact.transactionByName.put(ttype.id, ttype)
        fact.transactionByName.put(ttype.code, ttype)
        fact.transactionByName.put(ttype.asyncCode, ttype)
        fact.transactionByRequestAction.put(ttype.requestAction, ttype)
        fact.transactionByResponseAction.put(ttype.responseAction, ttype)
        tt.property.each {
            String name = it.@name.text()
            String value = it.@value.text()
            ttype.putTransactionProperty(name, value)
        }
        log.debug("Loading ${ttype}")
    }

    void parseActorType(at) {
        ActorType actorType = new ActorType()
        actorType.name = at.@displayName
        actorType.shortName = at.@id
        actorType.actorSimFactoryClassName = at.simFactoryClass.@class
        at.transaction.each { trans ->
            def transId = trans.@id
            def tt = fact.transactionByName.get(transId.text())
            if (!tt)
                throw new ToolkitRuntimeException("Transaction ${transId} not defined - ${transactionTypeMap.keySet()}")
            actorType.getTransactionTypes().add(tt)
        }
        at.property.each {
            String name = it.@name.text()
            String value = it.@value.text()
            actorType.putActorProperty(name, value)
        }
        fact.actorByName.put(actorType.getShortName(), actorType)
        log.debug("Loading ${actorType}")
    }
}
