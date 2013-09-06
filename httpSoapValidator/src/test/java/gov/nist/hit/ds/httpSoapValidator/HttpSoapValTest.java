package gov.nist.hit.ds.httpSoapValidator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.http.environment.HttpEnvironment;
import gov.nist.hit.ds.httpSoapValidator.testSupport.HttpServletResponseMock;
import gov.nist.hit.ds.httpSoapValidator.validators.HttpMessageValidator;
import gov.nist.hit.ds.httpSoapValidator.validators.SoapHeaderValidator;
import gov.nist.hit.ds.httpSoapValidator.validators.SoapParser;
import gov.nist.hit.ds.simSupport.engine.SimChain;
import gov.nist.hit.ds.simSupport.engine.SimComponent;
import gov.nist.hit.ds.simSupport.engine.SimEngine;
import gov.nist.hit.ds.simSupport.engine.SimEngineException;
import gov.nist.hit.ds.simSupport.engine.SimStep;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.loader.ByParamLogLoader;
import gov.nist.hit.ds.simSupport.loader.ValidationContext;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.xmlValidator.XmlParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class HttpSoapValTest {

	@Test
	public void httpSoapTest() {		
		ByParamLogLoader loader = new ByParamLogLoader().setSource(new File("src/test/resources/simple"));

		SimChain simChain = new SimChain();

		engine = setup(loader, simChain);

		run(engine, simChain);
		System.out.println(simChain.getLog());
		assertFalse(simChain.hasErrors());
	}

	@Test
	public void soapFaultTest() {		

		SimChain simChain = new SimChain();

		engine = new SimEngine(simChain);

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
		ByParamLogLoader loader = new ByParamLogLoader().setSource(new File("src/test/resources/fault"));

		SimChain simChain = new SimChain();

		engine = setup(loader, simChain);

		run(engine, simChain);
		System.out.println(simChain.getLog());
		assertTrue(simChain.hasErrors());
	}

	@Test
	public void noHeaderFaultTest() {		
		ByParamLogLoader loader = new ByParamLogLoader().setSource(new File("src/test/resources/noHeaderFault"));

		SimChain simChain = new SimChain();

		engine = setup(loader, simChain);

		run(engine, simChain);
		System.out.println(simChain.getLog());
		assertTrue(simChain.hasErrors());
	}
	

	ValidationContext vc = new ValidationContext();
	SimEngine engine;
	
	SimEngine setup(ByParamLogLoader loader, SimChain simChain) {
		vc.hasHttp = true;
		vc.hasSoap = true;
		vc.isR = true;
		vc.isRequest = true;

		List<SimStep> simSteps = new ArrayList<SimStep>();

		simChain.setBase(new SoapEnvironment(new HttpEnvironment().setResponse(new HttpServletResponseMock())));
		engine = new SimEngine(simChain);

		// Supplies ValidatorContext and MessageValidatorEngine
		simSteps.add(new SimStep().
				setName("Validation Context Source").
				setSimComponent(new MyWrapper()));
		
		simSteps.add(new SimStep().
				setName("HTTP Log Loader").
				setSimComponent(loader));
		simSteps.add(new SimStep().
				setName("HttpMessageValidator").
				setSimComponent(new HttpMessageValidator()));
		simSteps.add(new SimStep().
				setName("XML Parser").
				setSimComponent(new XmlParser()));
		simSteps.add(new SimStep().
				setName("SOAP Parser").
				setSimComponent(new SoapParser()));
		simSteps.add(new SimStep().
				setName("SoapMessageValidator").
				setSimComponent(new SoapHeaderValidator().setExpectedWsAction("urn:ihe:iti:2007:RegisterDocumentSet-b")));
//		simSteps.add(new SimStep().
//				setName("ebRS Root Name Validator").
//				setSimComponent(new SubmitObjectsRequestRootValidator()));
		simChain.setSteps(simSteps);
		return engine;
	}

	void run(SimEngine engine, SimChain simChain) {
		System.out.println(engine.getDescription(simChain));
		try {
			engine.run();
		} catch (SimEngineException e) {
			System.out.flush();
			e.printStackTrace();
			fail();
		}
	}
	
	public class MyWrapper implements SimComponent {
		ErrorRecorder er;
		
		public MessageValidatorEngine getMessageValidationEngine() {
			return engine;
		}

		public ValidationContext getValidationContext() {
			return vc;
		}

		@Override
		public void setErrorRecorder(ErrorRecorder er) {
			this.er = er;
		}

		@Override
		public String getName() {
			return getClass().getSimpleName();
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public void run(MessageValidatorEngine mve) throws SoapFaultException {

		}

		@Override
		public void setName(String name) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setDescription(String description) {
			// TODO Auto-generated method stub
			
		}
	}


}
