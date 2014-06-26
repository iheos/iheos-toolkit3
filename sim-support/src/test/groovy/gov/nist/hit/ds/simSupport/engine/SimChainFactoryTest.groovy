package gov.nist.hit.ds.simSupport.engine

import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.simSupport.simChain.SimChainFactory
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment
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
        simChain.base = new SoapEnvironment()
        def engine = new SimEngine(simChain);
        engine.run()
        println simChain.getStepStatusString()

        then:
        engine.isComplete()
        !engine.hasError
    }
}
