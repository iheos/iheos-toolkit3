package gov.nist.hit.ds.httpSoap;

import gov.nist.hit.ds.eventLog.Event;


public class ConfigFileTest {
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
//	public void configFileTest()  {
//		try {
//
//			String chainDef = "httpSoap.properties";
//
//			SimChain simChain = new SimChainLoader(chainDef).load();
//			simChain.setBase(new SoapEnvironment(new HttpEnvironment().setResponse(new HttpServletResponseMock())));
//
//			assertTrue(simChain.getSteps().size() > 0);
//
//			SimEngine engine = new SimEngine(simChain, event);
//
//			System.out.println(engine.getDescription(simChain));
//
//			engine.testRun();
//
//			System.out.println(simChain.getLog());
//
//			for (SimStep step : simChain.getSteps())
//				assertTrue(engine.isStepCompleted(step));
//			assertTrue(engine.isComplete());
//
//
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//			fail();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//			fail();
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//			fail();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//			fail();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//			fail();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//			fail();
//		} catch (SimEngineException e) {
//			e.printStackTrace();
//			fail();
//		} catch (SimChainLoaderException e) {
//			e.printStackTrace();
//			fail();
//		} catch (RepositoryException e) {
//			e.printStackTrace();
//			fail();
//		}
//	}

}
