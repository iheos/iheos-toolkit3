package gov.nist.hit.ds.simSupport.engine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.simSupport.test.sims.BarUser;
import gov.nist.hit.ds.simSupport.test.sims.Base;
import gov.nist.hit.ds.simSupport.test.sims.FooMaker;
import gov.nist.hit.ds.simSupport.test.sims.FooMakerError;
import gov.nist.hit.ds.simSupport.test.sims.FooUser;
import gov.nist.hit.ds.simSupport.test.sims.FooUserBarMaker;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SimEngineTest {

	@Test 
	public void isCompleteTest() {		
		List<SimStep> simSteps = new ArrayList<SimStep>();
		SimStep fooMakerStep = new SimStep().
				setName("FooMaker").
				setSimComponent(new FooMaker()); 
		simSteps.add(fooMakerStep);
		
		SimChain simChain = new SimChain().
				setSteps(simSteps);

		SimEngine engine = new SimEngine(simChain);
		assertFalse(fooMakerStep.hasRan());
		assertFalse(engine.isComplete());
		fooMakerStep.hasRan(true);
		assertTrue(engine.isComplete());
	}
	
	/**
	 * Find match where publisher is previous ValSim.
	 * Includes match where publisher is base
	 */
	@Test
	public void prevMatchTest() {
		Base base = new Base(); // a FooPublisher and a BarPublisher
		
		List<SimStep> simSteps = new ArrayList<SimStep>();
		simSteps.add(new SimStep().
				setName("FooUserBarMaker Name").
				setSimComponent(new FooUserBarMaker()));
		simSteps.add(new SimStep().
				setName("BarUser Name").
				setSimComponent(new BarUser()));
		
		SimChain simChain = new SimChain().
				setBase(base).
				setSteps(simSteps);

		run(simChain);
	}

	@Test
	public void errorCatchTest() {		
		List<SimStep> simSteps = new ArrayList<SimStep>();
		simSteps.add(new SimStep().
				setName("FooMakerError").
				setSimComponent(new FooMakerError()));
		simSteps.add(new SimStep().
				setName("FooUser").
				setSimComponent(new FooUser()));
		
		SimChain simChain = new SimChain().
				setSteps(simSteps);

		assertFalse(simChain.hasErrors());
		run(simChain);
		assertTrue(simChain.hasErrors());
	}

	void run(SimChain simChain) {
		SimEngine engine = new SimEngine(simChain);
		System.out.println(engine.getDescription(simChain));
		try {
			engine.run();
		} catch (SimEngineSubscriptionException e) {
			System.out.flush();
			e.printStackTrace();
			fail();
		}
	}
	
}
