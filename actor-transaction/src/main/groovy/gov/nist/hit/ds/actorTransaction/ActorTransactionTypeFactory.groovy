package gov.nist.hit.ds.actorTransaction

import groovy.util.logging.Log4j

/**
 * Created by bmajur on 5/7/14.
 */
@Log4j
class ActorTransactionTypeFactory {

    void load() {
        URL url = this.class.getClassLoader().getResource('actorsAndTransactions.txt')
        assert url != null
        url.eachLine {
            boolean isTransaction = TransactionTypeFactory.loadTransactionType(it.trim())
            if (isTransaction) return
            boolean isActor = ActorTypeFactory.loadActorType(it.trim())
            if (isActor) return
            log.error("Reading <${url}> - cannot load <${it}> as either a transaction or actor definition.")
        }
    }
}
