package gov.nist.hit.ds.actorTransaction

import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
import groovy.util.logging.Log4j
import org.apache.log4j.Logger

/**
 * Created by bmajur on 8/27/14.
 */

class ActorTransactionTypeDAO {
    static Logger log = Logger.getLogger(ActorTransactionTypeDAO.class);
    ActorTransactionTypeFactory fact

    ActorTransactionTypeDAO(ActorTransactionTypeFactory _fact) { fact = _fact }

    void loadFromResource(String resourceName) {
        log.info("Loading ActorTransaction types from resource ${resourceName}")
        log.info("... ${this.getClass().getClassLoader().getResource(resourceName).toURI()}")
        loadFromString(this.class.getClassLoader().getResourceAsStream(resourceName).text)
    }

    void loadFromString(String config) {
        log.info("Loading ActorTransaction types from String")
        def records = new XmlSlurper().parseText(config)
        def transactions = records.transaction
        def actors = records.actor
        transactions.each { parseTransaction(it) }
        actors.each { parseActor(it) }
    }

    void parseTransaction(tt) {
        TransactionType ttype = new TransactionType()
        def id = tt.@id.text()
        ttype.name = tt.@name.text()
        ttype.code = tt.@code.text()
        ttype.id = tt.@id.text()
        ttype.isRetrieve = tt.@isRetrieve.text() == 'true'
        ttype.asyncCode = tt.@asyncCode.text()
        ttype.requestAction = tt.request.@action.text()
        ttype.responseAction = tt.response.@action.text()
        ttype.acceptRequestClassName = tt.implClass.@value.text()
        tt.params.each  { params ->
            ttype.multiPart = params.@multipart.text() == 'true'
            ttype.soap = params.@soap.text() == 'true'
        }

        // transactions can be looked up by name, code, asyncCode, or id
        fact.transactionByName.put(ttype.name, ttype)
        fact.transactionByName.put(ttype.code, ttype)
        fact.transactionByName.put(ttype.asyncCode, ttype)
        fact.transactionById.put(id, ttype)

        fact.transactionByRequestAction.put(ttype.requestAction, ttype)
        fact.transactionByResponseAction.put(ttype.responseAction, ttype)
        tt.property.each {
            String name = it.@name.text()
            String value = it.@value.text()
            ttype.putTransactionProperty(name, value)
        }
        log.debug("Loading ${ttype}")
        ttype.check()
    }

    void parseActor(at) {
        ActorType actorType = new ActorType()
        log.debug("Parsing actor type ${at.@id}")
        actorType.name = at.@id
        actorType.shortName = at.@id
        actorType.actorSimFactoryClassName = at.simFactoryClass.@class
        def directions = [] as Set
        at.transaction.each { trans ->
            String transId = trans.@id.text()
            log.debug("...${transId}")
            TransactionType tt = fact.transactionById.get(transId)
            if (!tt)
                throw new ToolkitRuntimeException("Actor type ${actorType.name} references Transaction type by id [${transId}] which is not defined - ${fact.transactionByName.keySet()}")
            boolean client = trans.@type.text() == 'client'
            directions.add(client)
            actorType.getDirectionalTransactionTypes().add(new DirectionalTransactionType(tt, client))
            tt.actorType = actorType
        }
        if (directions.size() == 2)
            throw new ToolkitRuntimeException("Actor type ${actorType.name} is inconsistent - all transactions must be server or all transactions must be client")
        at.property.each {
            String name = it.@name.text()
            String value = it.@value.text()
            actorType.putActorProperty(name, value)
        }
        fact.actorByName.put(actorType.getShortName(), actorType)
        log.debug("Loading ${actorType}")
    }
}
