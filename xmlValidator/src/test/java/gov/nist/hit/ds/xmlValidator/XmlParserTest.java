package gov.nist.hit.ds.xmlValidator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.engine.SimChain;
import gov.nist.hit.ds.simSupport.engine.SimEngine;
import gov.nist.hit.ds.simSupport.engine.SimEngineException;
import gov.nist.hit.ds.simSupport.engine.SimStep;
import gov.nist.hit.ds.simSupport.event.EventBuilder;
import gov.nist.hit.ds.simSupport.loader.ByParamLogLoader;
import gov.nist.hit.ds.simSupport.loader.ValidationContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class XmlParserTest {
	Event event = null;
	
	@Before
	public void init() throws InitializationFailedException, IOException, RepositoryException {
		Installation.reset();
		Installation.installation().initialize();
		Configuration.configuration();
		event = new EventBuilder().buildEvent(new SimId("1123"), "Foo", "FOO");
	}

	@Test
	public void goodXml() throws RepositoryException {
		ByParamLogLoader loader = new ByParamLogLoader().setSource("src/test/resources/good");
		ValidationContext vc = new ValidationContext();
		vc.hasHttp = true;
		vc.hasSoap = true;
		vc.isR = true;

		List<SimStep> simSteps = new ArrayList<SimStep>();

		SimChain simChain = new SimChain();
		SimEngine engine = new SimEngine(simChain, event);

		simSteps.add(new SimStep().
				setName("HTTP Log Loader").
				setSimComponent(loader));
		simSteps.add(new SimStep().
				setName("XML Parser").
				setSimComponent(new XmlParser()));
		simChain.setSteps(simSteps);

		run(engine, simChain);
		System.out.println(simChain.getLog());
		assertFalse(simChain.hasErrors());
	}

	@Test
	public void badXml() throws RepositoryException {
		ByParamLogLoader loader = new ByParamLogLoader().setSource(new File("src/test/resources/bad"));
		ValidationContext vc = new ValidationContext();
		vc.hasHttp = true;
		vc.hasSoap = true;
		vc.isR = true;

		List<SimStep> simSteps = new ArrayList<SimStep>();

		SimChain simChain = new SimChain();
		SimEngine engine = new SimEngine(simChain, event);

		simSteps.add(new SimStep().
				setName("HTTP Log Loader").
				setSimComponent(loader));
		simSteps.add(new SimStep().
				setName("XML Parser").
				setSimComponent(new XmlParser()));
		simChain.setSteps(simSteps);

		run(engine, simChain);
		System.out.println(simChain.getLog());
		assertTrue(simChain.hasErrors());
	}

	void run(SimEngine engine, SimChain simChain) {
		System.out.println(engine.getDescription(simChain));
		try {
			engine.run();
		} catch (SimEngineException e) {
			System.out.flush();
			e.printStackTrace();
			fail();
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail();
		}
	}
}
