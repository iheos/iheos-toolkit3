package gov.nist.hit.ds.httpSoapValidator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.http.parser.HttpEnvironment;
import gov.nist.hit.ds.simSupport.LogLoader;
import gov.nist.hit.ds.simSupport.ValidationContext;
import gov.nist.hit.ds.simSupport.engine.SimChain;
import gov.nist.hit.ds.simSupport.engine.SimEngine;
import gov.nist.hit.ds.simSupport.engine.SimEngineSubscriptionException;
import gov.nist.hit.ds.simSupport.engine.SimStep;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;
import gov.nist.hit.ds.xmlValidator.XmlParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class HttpSoapValTest {
	
	@Test
	public void httpSoapTest() {		
		LogLoader loader = new LogLoader(new File("src/test/resources/simple"));
		
		SimChain simChain = new SimChain();
		
		SimEngine engine = setup(loader, simChain);

		run(engine, simChain);
		System.out.println(simChain.getLog());
		assertFalse(simChain.hasErrors());
		assertTrue(6 == engine.getSimsRun());
		assertFalse(simChain.hasErrors());
	}

	@Test
	public void soapFaultTest() {		
		
		SimChain simChain = new SimChain();
		
		SimEngine engine = new SimEngine(simChain);

		List<SimStep> simSteps = new ArrayList<SimStep>();

		simChain.setBase(new SoapEnvironment(new HttpEnvironment().setResponse(new HttpServletResponseMock())));

		simSteps.add(new SimStep().
				setName("SOAPFault test").
				setSimComponent(new SoapFaultThrower()));

		simChain.setSteps(simSteps);

		run(engine, simChain);
		System.out.println(simChain.getLog());
		assertTrue(simChain.hasErrors());
	}
	
	@Test
	public void mustUnderstandFaultTest() {		
		LogLoader loader = new LogLoader(new File("src/test/resources/fault"));
		
		SimChain simChain = new SimChain();
		
		SimEngine engine = setup(loader, simChain);

		run(engine, simChain);
		System.out.println(simChain.getLog());
		assertTrue(simChain.hasErrors());
	}

	@Test
	public void noHeaderFaultTest() {		
		LogLoader loader = new LogLoader(new File("src/test/resources/noHeaderFault"));
		
		SimChain simChain = new SimChain();
		
		SimEngine engine = setup(loader, simChain);

		run(engine, simChain);
		System.out.println(simChain.getLog());
		assertTrue(simChain.hasErrors());
	}


	SimEngine setup(LogLoader loader, SimChain simChain) {
		ValidationContext vc = new ValidationContext();
		vc.hasHttp = true;
		vc.hasSoap = true;
		vc.isR = true;
		vc.isRequest = true;

		List<SimStep> simSteps = new ArrayList<SimStep>();

		simChain.setBase(new SoapEnvironment(new HttpEnvironment().setResponse(new HttpServletResponseMock())));
		SimEngine engine = new SimEngine(simChain);

		simSteps.add(new SimStep().
				setName("HTTP Log Loader").
				setSimComponent(loader));
		simSteps.add(new SimStep().
				setName("HttpMessageValidator").
				setSimComponent(new HttpMessageValidator(vc, engine)));
		simSteps.add(new SimStep().
				setName("XML Parser").
				setSimComponent(new XmlParser()));
		simSteps.add(new SimStep().
				setName("SoapMessageValidator").
				setSimComponent(new SoapMessageValidator(vc, engine)));
		simSteps.add(new SimStep().
				setName("ebRS Root Name Validator").
				setSimComponent(new EbrsRootValidator("SubmitObjectsRequest")));
		simChain.setSteps(simSteps);
		return engine;
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
