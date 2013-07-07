package gov.nist.hit.ds.simSupport.test.engine;

import static org.junit.Assert.fail;
import gov.nist.hit.ds.simSupport.engine.SimChain;
import gov.nist.hit.ds.simSupport.engine.SimEngine;
import gov.nist.hit.ds.simSupport.engine.SimEngineSubscriptionException;
import gov.nist.hit.ds.simSupport.engine.SimStep;
import gov.nist.hit.ds.simSupport.test.sims.BarUser;
import gov.nist.hit.ds.simSupport.test.sims.Base;
import gov.nist.hit.ds.simSupport.test.sims.FooUserBarMaker;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SimEngineTest {

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
				setValSim(new FooUserBarMaker()));
		simSteps.add(new SimStep().
				setName("BarUser Name").
				setValSim(new BarUser()));
		
		SimChain simChain = new SimChain().
				setBase(base).
				setSteps(simSteps);

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
