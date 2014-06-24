package gov.nist.hit.ds.simSupport.engine

import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.simSupport.components.FooBarBase
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment
import spock.lang.Specification

public class SimEngineTest extends Specification {
    def base = new FooBarBase()  // pubs Foo and Bar

    def 'Initial Chain Is Not Complete'() {
        when:
        def event = new EventFactory().buildEvent(EventFactory.IN_MEMORY)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.FooMaker', [:])

        SimChain simChain = simChainFactory.simChain

        then:
        !simChain.steps[0].completed
        !simChain.isDone()
    }

    def 'Base is Publisher'() {
        setup:
        def event = new EventFactory().buildEvent(EventFactory.IN_MEMORY)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.BarUser', [:])
        SimChain simChain = simChainFactory.simChain
        simChain.base = base
        SimEngine engine = new SimEngine(simChain)

        when:
        engine.run()

        then:
        simChain.isDone()
    }

    def 'Empty componentsPriorTo' () {
        setup:
        def targetComponent = 'a'
        def priorComponents = []

        when:
        def priors = new SimEngine(null).componentsPriorTo(targetComponent, priorComponents)

        then:
        priors == []
    }

    def 'First is target componentsPriorTo' () {
        setup:
        def targetComponent = 'a'
        def priorComponents = ['a', 'b', 'c'].reverse()

        when:
        def priors = new SimEngine(null).componentsPriorTo(targetComponent, priorComponents)

        then:
        priors == []
    }

    def 'Middle is target componentsPriorTo' () {
        setup:
        def targetComponent = 'b'
        def priorComponents = ['a', 'b', 'c'].reverse()

        when:
        def priors = new SimEngine(null).componentsPriorTo(targetComponent, priorComponents)

        then:
        priors == ['a']
    }

    def 'Last is target componentsPriorTo' () {
        setup:
        def targetComponent = 'c'
        def priorComponents = ['a', 'b', 'c'].reverse()

        when:
        def priors = new SimEngine(null).componentsPriorTo(targetComponent, priorComponents)

        then:
        priors == ['a', 'b']
    }

    def 'No Matching Publishers on last component'() {
        setup:
        def event = new EventFactory().buildEvent(EventFactory.IN_MEMORY)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.FooMaker', [:])
        // this one fails
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.BarUser', [:])
        SimChain simChain = simChainFactory.simChain
        simChain.base = new SoapEnvironment()
        SimEngine engine = new SimEngine(simChain)

        when:
        engine.run()
        def errs = simChain.getErrorMessages()

        then:
        simChain.isDone()
        simChain.hasErrors()
        errs.size() > 0
    }

    def 'No Matching Publishers on not last component'() {
        setup:
        def event = new EventFactory().buildEvent(EventFactory.IN_MEMORY)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.FooMaker', [:])
        // this one fails
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.BarUser', [:])
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.FooMaker', [:])
        SimChain simChain = simChainFactory.simChain
        simChain.base = new SoapEnvironment()
        SimEngine engine = new SimEngine(simChain)

        when:
        engine.run()

        then:
        simChain.isDone()
        simChain.hasErrors()
    }

    def 'No Previous Publishers'() {
        setup:
        def event = new EventFactory().buildEvent(EventFactory.IN_MEMORY)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.BarUser', [:])
        SimChain simChain = simChainFactory.simChain
        simChain.base = new SoapEnvironment()
        SimEngine engine = new SimEngine(simChain)

        when:
        engine.run()

        then:
        simChain.isDone()
        simChain.hasErrors()
    }

    def 'Trace'() {
        setup:
        def base = new FooBarBase()  // pubs Foo and Bar
        def event = new EventFactory().buildEvent(EventFactory.IN_MEMORY)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.BarUser', [:])
        SimChain simChain = simChainFactory.simChain
        simChain.base = base
        SimEngine engine = new SimEngine(simChain)
        engine.trace = true

        when:
        engine.run()
        println engine.getExecutionTrace()

        then:
        simChain.isDone()
    }

    def 'Trace internal error'() {
        setup:
        def event = new EventFactory().buildEvent(EventFactory.IN_MEMORY)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.BarUser', [:])
        SimChain simChain = simChainFactory.simChain
        simChain.base = new SoapEnvironment()
        SimEngine engine = new SimEngine(simChain)
        engine.trace = true

        when:
        engine.run()
        println engine.getExecutionTrace()

        then:
        simChain.isDone()
        simChain.hasErrors()
        simChain.hasInternalErrors()
    }


}
