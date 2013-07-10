package gov.nist.hit.ds.simSupport.engine;

import static org.junit.Assert.*;
import gov.nist.hit.ds.simSupport.engine.SimChain;
import gov.nist.hit.ds.simSupport.engine.SimStep;

import java.util.Iterator;

import org.junit.Test;

public class SimChainTest {

	@Test
	public void iterator1Test() {
		SimChain simChain = new SimChain();
		simChain.add(new SimStep().setName("Step 1"));
		
		Iterator<SimStep> it = simChain.iterator();
		assertTrue(it.hasNext());
		it.next();
		assertFalse(it.hasNext());
	}

	@Test
	public void iterator2Test() {
		SimChain simChain = new SimChain();
		simChain.add(new SimStep().setName("Step 1"));
		simChain.add(new SimStep().setName("Step 2"));
		
		Iterator<SimStep> it = simChain.iterator();
		assertTrue(it.hasNext());
		it.next();
		assertTrue(it.hasNext());
		it.next();
		assertFalse(it.hasNext());
	}
}
