package gov.nist.hit.ds.httpSoap;

import gov.nist.hit.ds.eventLog.Event;

/*
 * TODO: Rethink these tests
 */
public class HttpSoapValTest {
	Event event = null;
	
//	@Before
//	public void build() throws InitializationFailedException, IOException, RepositoryException {
//		Installation.reset();
//		Installation.installation().initialize();
//		Configuration.configuration();
//		event = new EventBuilder().buildEvent(new SimId("1123"), "Foo", "FOO");
//	}
//
//	@Test
//	public void httpSoapTest() throws RepositoryException {
//		ByParamLogLoader loader = new ByParamLogLoader().setSource(new File("src/test/resources/simple"));
//
//		SimChain simChain = new SimChain();
//
//		engine = setup(loader, simChain);
//
//		run(engine, simChain);
//		System.out.println(simChain.getLog());
//		assertFalse(simChain.hasErrors());
//	}
//
//	@Test
//	public void soapFaultTest() throws RepositoryException {
//
//		SimChain simChain = new SimChain();
//
//		engine = new SimEngine(simChain, event);
//
//		List<SimStep> simSteps = new ArrayList<SimStep>();
//
//		simChain.setBase(new SoapEnvironment(new HttpEnvironment().setResponse(new HttpServletResponseMock())));
//
//		simSteps.add(new SimStep().
//				setName("SOAPFault test").
//				setSimComponent(new SoapFaultThrower()));
//
//		simChain.setSteps(simSteps);
//
//		run(engine, simChain);
//		System.out.println(simChain.getLog());
//		assertTrue(simChain.hasErrors());
//	}
//
//	@Test
//	public void mustUnderstandFaultTest() throws RepositoryException {
//		ByParamLogLoader loader = new ByParamLogLoader().setSource(new File("src/test/resources/fault"));
//
//		SimChain simChain = new SimChain();
//
//		engine = setup(loader, simChain);
//
//		run(engine, simChain);
//		System.out.println(simChain.getLog());
//		assertTrue(simChain.hasErrors());
//	}
//
//	/*
//	 * TODO: Fix and nable this test
//	 */
////	@Test
////	public void noHeaderFaultTest() throws RepositoryException {
////		ByParamLogLoader loader = new ByParamLogLoader().setSource(new File("src/test/resources/noHeaderFault"));
////
////		SimChain simChain = new SimChain();
////
////		engine = setup(loader, simChain);
////
////		run(engine, simChain);
////		System.out.println(simChain.getLog());
////		assertTrue(simChain.hasErrors());
////	}
//
//
//	ValidationContext vc = new ValidationContext();
//	SimEngine engine;
//
//	SimEngine setup(ByParamLogLoader loader, SimChain simChain) throws RepositoryException {
//		vc.hasHttp = true;
//		vc.hasSoap = true;
//		vc.isR = true;
//		vc.isRequest = true;
//
//		List<SimStep> simSteps = new ArrayList<SimStep>();
//
//		simChain.setBase(new SoapEnvironment(new HttpEnvironment().setResponse(new HttpServletResponseMock())));
//		engine = new SimEngine(simChain, event);
//
//		// Supplies ValidatorContext and MessageValidatorEngine
//		simSteps.add(new SimStep().
//				setName("Validation Context Source").
//				setSimComponent(new MyWrapper()));
//
//		simSteps.add(new SimStep().
//				setName("HTTP Log Loader").
//				setSimComponent(loader));
//		simSteps.add(new SimStep().
//				setName("HttpMessageValidator").
//				setSimComponent(new HttpMessageValidator()));
//		simSteps.add(new SimStep().
//				setName("XML Parser").
//				setSimComponent(new XmlParser()));
//		simSteps.add(new SimStep().
//				setName("SOAP Parser").
//				setSimComponent(new SoapMessageParser()));
//		simSteps.add(new SimStep().
//				setName("SoapMessageValidator").
//				setSimComponent(new SoapHeaderValidator().setExpectedWsAction("urn:ihe:iti:2007:RegisterDocumentSet-b")));
////		simSteps.add(new SimStep().
////				setName("ebRS Root Name Validator").
////				setSimComponent(new SubmitObjectsRequestRootValidator()));
//		simChain.setSteps(simSteps);
//		return engine;
//	}
//
//	void run(SimEngine engine, SimChain simChain) {
//		System.out.println(engine.getDescription(simChain));
//		try {
//			engine.run();
//		} catch (SimEngineException e) {
//			System.out.flushAll();
//			e.printStackTrace();
//			fail();
//		} catch (RepositoryException e) {
//			e.printStackTrace();
//			fail();
//		}
//	}
//
//	public class MyWrapper implements SimComponent {
//		IAssertionGroup er;
//		Event event;
//
//		public MessageValidatorEngine getMessageValidationEngine() {
//			return engine;
//		}
//
//		public ValidationContext getValidationContext() {
//			return vc;
//		}
//
//		@Override
//		public void setAssertionGroup(AssertionGroup er) {
//			this.er = er;
//		}
//
//		@Override
//		public String getName() {
//			return getClass().getSimpleName();
//		}
//
//		@Override
//		public String getDescription() {
//			return null;
//		}
//
//		@Override
//		public void run(MessageValidatorEngine mve) throws SoapFaultException {
//
//		}
//
//		@Override
//		public void setName(String name) {
//
//		}
//
//		@Override
//		public void setDescription(String description) {
//
//		}
//
//		@Override
//		public void setEvent(Event event) {
//			this.event = event;
//		}
//
//		@Override
//		public boolean showOutputInLogs() {
//			return false;
//		}
//	}


}
