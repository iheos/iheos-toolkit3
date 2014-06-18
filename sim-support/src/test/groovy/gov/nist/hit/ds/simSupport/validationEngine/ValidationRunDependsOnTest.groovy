package gov.nist.hit.ds.simSupport.validationEngine
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.eventLog.assertion.Assertion
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.simSupport.engine.SimChain
import gov.nist.hit.ds.simSupport.engine.SimChainFactory
import gov.nist.hit.ds.simSupport.engine.SimEngine
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment
import spock.lang.Specification

public class ValidationRunDependsOnTest extends Specification {

    def 'Depends are in Order'() {
        setup:
        Event event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.validationEngine.ValidatorWithDepends', [:])
        SimChain simChain = simChainFactory.simChain
        simChain.setBase(new SoapEnvironment())

        when:
        new SimEngine(simChain).run()
        AssertionGroup ag = event.assertionGroup
        Assertion a = ag.getFirstFailedAssertion();

        then:
        a == null
        3 == ag.size()
        'VAL3' == ag.getAssertion(0).getId()
        'VAL2' == ag.getAssertion(1).getId()
        'VAL1' == ag.getAssertion(2).getId()
    }
}
