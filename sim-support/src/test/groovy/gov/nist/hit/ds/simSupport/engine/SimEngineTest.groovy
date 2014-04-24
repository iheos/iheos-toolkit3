package gov.nist.hit.ds.simSupport.engine

import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.simSupport.components.Base
import spock.lang.Specification

public class SimEngineTest extends Specification {

    def 'Initial Chain Is Not Complete'() {
        when:
        def event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.FooMaker', [:])

        SimChain simChain = simChainFactory.simChain

        then:
        !simChain.steps[0].completed
        !simChain.isDone()
    }

    def 'Base is Publisher'() {
        setup:
        def base = new Base()  // pubs Foo and Bar
        def event = new EventFactory().buildEvent(null)
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
        def event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.FooMaker', [:])
        // this one fails
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.BarUser', [:])
        SimChain simChain = simChainFactory.simChain
        SimEngine engine = new SimEngine(simChain)

        when:
        engine.run()
        def errs = simChain.getErrorMessages()
        print "Found ${errs.size()} errors."
        print errs

        then:
        simChain.isDone()
        simChain.hasErrors()
        errs.size() == 2
    }

    def 'No Matching Publishers on not last component'() {
        setup:
        def event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.FooMaker', [:])
        // this one fails
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.BarUser', [:])
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.FooMaker', [:])
        SimChain simChain = simChainFactory.simChain
        SimEngine engine = new SimEngine(simChain)

        when:
        engine.run()

        then:
        simChain.isDone()
        simChain.hasErrors()
    }

    def 'No Previous Publishers'() {
        setup:
        def event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.addComponent('gov.nist.hit.ds.simSupport.components.BarUser', [:])
        SimChain simChain = simChainFactory.simChain
        SimEngine engine = new SimEngine(simChain)

        when:
        engine.run()

        then:
        simChain.isDone()
        simChain.hasErrors()
    }

//    public void errorCatchTest() throws RepositoryException {
//        List<SimStep> simSteps = new ArrayList<SimStep>();
//        SimStep step = new SimStep();
//        step.setName("FooMakerError");
//        step.setSimComponent(new FooMakerError());
//        simSteps.add(step);
//        step = new SimStep();
//        step.setName("FooUser");
//        step.setSimComponent(new FooUser());
//        simSteps.add(step);
//
//        SimChain simChain = new SimChain().addSteps(simSteps);
//
//        assertFalse(simChain.hasErrors());
//        run(simChain);
//        assertTrue(simChain.hasErrors());
//    }
//
//    void run(SimChain simChain) throws RepositoryException {
//        Event event = new EventFactory().buildEvent(null);
//        simChain.init(event);
//        SimEngine engine = new SimEngine(simChain);
//        try {
//            engine.run();
//            System.out.println(engine.getDescription(simChain));
//        } catch (SimEngineException e) {
//            System.out.flush();
//            e.printStackTrace();
//            fail();
//        }
//    }

}
