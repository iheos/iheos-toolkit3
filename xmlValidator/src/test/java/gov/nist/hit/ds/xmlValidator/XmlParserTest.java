package gov.nist.hit.ds.xmlValidator;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.simSupport.engine.SimChain;
import gov.nist.hit.ds.simSupport.engine.SimEngine;
import gov.nist.hit.ds.simSupport.engine.SimEngineSubscriptionException;
import gov.nist.hit.ds.simSupport.engine.SimStep;
import gov.nist.hit.ds.simSupport.transaction.LogLoader;
import gov.nist.hit.ds.simSupport.transaction.ValidationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class XmlParserTest {

	@Test
	public void goodXml() {
		LogLoader loader = new LogLoader(new File("src/test/resources/good"));
		ValidationContext vc = new ValidationContext();
		vc.hasHttp = true;
		vc.hasSoap = true;
		vc.isR = true;

		List<SimStep> simSteps = new ArrayList<SimStep>();

		SimChain simChain = new SimChain();
		SimEngine engine = new SimEngine(simChain);

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
	public void badXml() {
		LogLoader loader = new LogLoader(new File("src/test/resources/bad"));
		ValidationContext vc = new ValidationContext();
		vc.hasHttp = true;
		vc.hasSoap = true;
		vc.isR = true;

		List<SimStep> simSteps = new ArrayList<SimStep>();

		SimChain simChain = new SimChain();
		SimEngine engine = new SimEngine(simChain);

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
		} catch (SimEngineSubscriptionException e) {
			System.out.flush();
			e.printStackTrace();
			fail();
		}
	}
}
