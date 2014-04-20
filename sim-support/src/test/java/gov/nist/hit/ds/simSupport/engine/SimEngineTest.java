package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.event.EventBuilder;
import gov.nist.hit.ds.simSupport.exception.SimEngineException;
import gov.nist.hit.ds.simSupport.sims.*;
import gov.nist.hit.ds.toolkit.installation.InitializationFailedException;
import gov.nist.hit.ds.toolkit.installation.Installation;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SimEngineTest {
	Event event = null;

	@Before
	public void init() throws InitializationFailedException, IOException,
			RepositoryException {
		Installation.reset();
		Installation.installation().initialize();
		Configuration.configuration();
		event = new EventBuilder().buildEvent(new SimId("1123"), "Foo", "FOO");
	}

	@Test
	public void isCompleteTest() throws RepositoryException {
		List<SimStep> simSteps = new ArrayList<SimStep>();
		SimStep fooMakerStep = new SimStep().setName("FooMaker")
				.setSimComponent(new FooMaker());
		simSteps.add(fooMakerStep);

		SimChain simChain = new SimChain().addSteps(simSteps);

		SimEngine engine = new SimEngine(simChain, event);
		assertFalse(engine.isStepCompleted(fooMakerStep));
		assertFalse(!engine.isRunable());
		engine.simStepCompleted(fooMakerStep);
		assertTrue(!engine.isRunable());
	}

	/**
	 * Find match where publisher is previous ValSim. Includes match where
	 * publisher is base
	 * 
	 * @throws RepositoryException
	 */
	@Test
	public void prevMatchTest() throws RepositoryException {
		Base base = new Base(); // a FooPublisher and a BarPublisher

		List<SimStep> simSteps = new ArrayList<SimStep>();
		simSteps.add(new SimStep().setName("FooUserBarMaker Name")
				.setSimComponent(new FooUserBarMaker()));
		simSteps.add(new SimStep().setName("BarUser Name").setSimComponent(
				new BarUser()));

		SimChain simChain = new SimChain().setBase(base).addSteps(simSteps);

		run(simChain);
	}

	@Test
	public void errorCatchTest() throws RepositoryException {
		List<SimStep> simSteps = new ArrayList<SimStep>();
		simSteps.add(new SimStep().setName("FooMakerError").setSimComponent(
				new FooMakerError()));
		simSteps.add(new SimStep().setName("FooUser").setSimComponent(
				new FooUser()));

		SimChain simChain = new SimChain().addSteps(simSteps);

		assertFalse(simChain.hasErrors());
		run(simChain);
		assertTrue(simChain.hasErrors());
	}

	@Test
	public void fromFileTest() {

	}

	void run(SimChain simChain) throws RepositoryException {
		Event event = new EventBuilder().buildEvent(new SimId("1123"), "Foo",
				"FOO");
		SimEngine engine = new SimEngine(simChain, event);
		try {
			engine.run();
			System.out.println(engine.getDescription(simChain));
		} catch (SimEngineException e) {
			System.out.flush();
			e.printStackTrace();
			fail();
		}
	}

}
