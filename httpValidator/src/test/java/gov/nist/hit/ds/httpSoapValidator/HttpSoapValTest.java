package gov.nist.hit.ds.httpSoapValidator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.simSupport.LogLoader;
import gov.nist.hit.ds.simSupport.ValidationContext;
import gov.nist.hit.ds.simSupport.engine.SimChain;
import gov.nist.hit.ds.simSupport.engine.SimEngine;
import gov.nist.hit.ds.simSupport.engine.SimEngineSubscriptionException;
import gov.nist.hit.ds.simSupport.engine.SimStep;
import gov.nist.hit.ds.xmlValidator.XmlParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class HttpSoapValTest {
	@Test
	public void httpSoapTest() {		
		LogLoader loader = new LogLoader(new File("src/test/resources/simple"));
		ValidationContext vc = new ValidationContext();
		vc.hasHttp = true;
		vc.hasSoap = true;
		vc.isR = true;
		vc.isRequest = true;

		List<SimStep> simSteps = new ArrayList<SimStep>();

		SimChain simChain = new SimChain();
		SimEngine engine = new SimEngine(simChain);

		simSteps.add(new SimStep().
				setName("HTTP Log Loader").
				setValSim(loader));
		simSteps.add(new SimStep().
				setName("HttpMessageValidator").
				setValSim(new HttpMessageValidator(vc, engine)));
		simSteps.add(new SimStep().
				setName("XML Parser").
				setValSim(new XmlParser(vc, engine)));
		simSteps.add(new SimStep().
				setName("SoapMessageValidator").
				setValSim(new SoapMessageValidator(vc, engine)));
		simSteps.add(new SimStep().
				setName("ebRS Root Name Validator").
				setValSim(new EbrsRootValidator("SubmitObjectsRequest")));
		simChain.setSteps(simSteps);

		run(engine, simChain);
		System.out.println(simChain.getLog());
		assertFalse(simChain.hasErrors());
		assertTrue(6 == engine.getSimsRun());
		assertFalse(simChain.hasErrors());
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
