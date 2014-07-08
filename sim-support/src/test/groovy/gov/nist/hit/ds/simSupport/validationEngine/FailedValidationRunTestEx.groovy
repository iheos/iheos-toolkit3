package gov.nist.hit.ds.simSupport.validationEngine

import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.eventLog.assertion.Assertion
import gov.nist.hit.ds.simSupport.simChain.SimChain
import gov.nist.hit.ds.simSupport.simChain.SimChainFactory
import gov.nist.hit.ds.simSupport.simEngine.SimEngine
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment
import spock.lang.Specification

public class FailedValidationRunTestEx extends Specification {

    def 'Assertion Fails'() {
        setup:
        Event event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.validationEngine.ValidationFailed', [:])
        SimChain simChain = simChainFactory.simChain
        simChain.setBase(new SoapEnvironment())

        when:
        new SimEngine(simChain).run()
        Assertion a = event.assertionGroup.getFirstFailedAssertion()

        then:
        a != null
    }

    def 'Assertion Fails with Fault'() {
        setup:
        Event event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.validationEngine.ValidationFailedWithFault', [:])
        SimChain simChain = simChainFactory.simChain
        simChain.setBase(new SoapEnvironment())

        when:
        new SimEngine(simChain).run()
        Assertion a = event.assertionGroup.getFirstFailedAssertion()

        then:
        a != null
        a.code == 'ActionNotSupported'
        event.hasFault()
    }

    def 'No SimComponentBase object'() {
        setup:
        Event event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.validationEngine.ValidationFailedWithFault', [:])
        SimChain simChain = simChainFactory.simChain
        //simChain.setBase(new SoapEnvironment())

        when:
        new SimEngine(simChain).run()

        then:
        event.hasFault()
    }

    def 'Bad Component Class'() {
        setup:
        Event event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)

        when:
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.validationEngine.Xxxx', [:])
        SimChain simChain = simChainFactory.simChain

        then:
        thrown SoapFaultException
    }

    def 'Component does not implement SimComponent'() {
        setup:
        Event event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)

        when:
        simChainFactory.addComponent('gov.nist.hit.ds.eventLog.Fault', [:])
        SimChain simChain = simChainFactory.simChain

        then:
        thrown SoapFaultException
    }
}
