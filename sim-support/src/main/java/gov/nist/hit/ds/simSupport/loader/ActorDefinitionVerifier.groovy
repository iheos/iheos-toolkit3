package gov.nist.hit.ds.simSupport.loader

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.ActorType
import gov.nist.hit.ds.simSupport.simChain.SimChainFactory

/**
 * Created by bmajur on 6/25/14.
 */
class ActorDefinitionVerifier {

    String verify(String actorName) {
        try {
            def factory = new ActorTransactionTypeFactory()
            ActorType actorType = factory.getActorType(actorName)
            actorType.check()
            actorType.transactionTypes.each {
                it.check()
                SimChainFactory.checkFromResource(it.acceptRequestClassName)
            }
        } catch (Exception e) { return "Error verifying Actor ${actorName}: ${e.getMessage()}" }
        return null
    }

    String verifyAll() {
        StringBuffer buf = null
        def factory = new ActorTransactionTypeFactory()
        factory.getActorTypeNames().each {
            def oops = verify(it)
            if (oops) {
                if (buf) buf.append('\n').append(oops)
                else { buf = new StringBuffer(); buf.append(oops) }
            }
        }
        return buf
    }

}
