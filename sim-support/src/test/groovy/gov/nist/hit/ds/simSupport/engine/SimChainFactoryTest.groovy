package gov.nist.hit.ds.simSupport.engine

import gov.nist.hit.ds.eventLog.EventFactory
import spock.lang.Specification

class SimChainFactoryTest extends Specification {

    def 'Test Startup'() {
        when:
        def event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.FooMaker', [:])
        def simChain = simChainFactory.simChain
        simChain.init(event)

        def engine = new SimEngine(simChain);

        then:
        engine.isRunable()
    }

    def 'Test Complete'() {
        when:
        def event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)
        def parms = [:]
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.FooMaker', parms)
        def simChain = simChainFactory.simChain
        simChain.init(event)
        def engine = new SimEngine(simChain);
        engine.run()

        then:
        !engine.isRunable()
    }
}
