package gov.nist.hit.ds.simSupport.test.engine;

import gov.nist.hit.ds.simSupport.engine.SimEngine;
import gov.nist.hit.ds.simSupport.engine.ValSim;
import gov.nist.hit.ds.simSupport.test.sims.BarUser;
import gov.nist.hit.ds.simSupport.test.sims.Base;
import gov.nist.hit.ds.simSupport.test.sims.FooUserBarMaker;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FooSimEngineTest {

	/**
	 * Find match where publisher is previous ValSim.
	 * Includes match where publisher is base
	 */
	@Test
	public void prevMatchTest() {
		Base base = new Base(); // a FooPublisher and a BarPublisher
		List<ValSim> chain = new ArrayList<ValSim>();
		chain.add(new FooUserBarMaker());
		chain.add(new BarUser());
		SimEngine engine = new SimEngine(base, chain);
		engine.run();
	}
}
