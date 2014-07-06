package gov.nist.hit.ds.simSupport.simEngine

import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.simSupport.simChain.SimChain
import gov.nist.hit.ds.simSupport.simChain.SimChainFactory
import spock.lang.Specification

/**
 * Created by bmajur on 5/1/14.
 */
class SimScannerTest extends Specification {

    def 'SimChain Scanner'() {
        setup:
        Event event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)

        when:
        simChainFactory.loadFromPropertyBasedResource('simChainTwoSteps.properties')
        SimChain simChain = simChainFactory.simChain
        SimScanner simScanner = new SimScanner(simChain)
        simScanner.run()

        then:
        simChain.steps.size() == 2
    }

    def 'SimChain Scanner Missing input'() {
        setup:
        Event event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)

        when:
        simChainFactory.loadFromPropertyBasedResource('simChainTwoStepsMissingInput.properties')
        SimChain simChain = simChainFactory.simChain
        SimScanner simScanner = new SimScanner(simChain)
        simScanner.run()

        then:
        simChain.steps.size() == 2
        !simScanner.isRunable()
    }

}
