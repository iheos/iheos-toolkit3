package gov.nist.hit.ds.registrySim;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.actorSim.factory.ActorSimFactory;
import gov.nist.hit.ds.actorSim.servlet.SimServlet;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.http.environment.HttpEnvironment;
import gov.nist.hit.ds.http.parser.HttpParseException;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.httpSoapValidator.testSupport.HttpServletResponseMock;
import gov.nist.hit.ds.initialization.ExtendedPropertyManager;
import gov.nist.hit.ds.initialization.Installation;
import gov.nist.hit.ds.registrySim.factory.DocumentRegistryActorFactory;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.engine.SimChainLoaderException;
import gov.nist.hit.ds.simSupport.engine.SimEngineSubscriptionException;
import gov.nist.hit.ds.simSupport.loader.ByConstructorLogLoader;
import gov.nist.hit.ds.simSupport.validators.SimEndpointParser;
import gov.nist.hit.ds.soapSupport.core.Endpoint;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;


public class RegistrySimTest {
	SimServlet servlet;
	
	@Before
	public void init() {
		Installation.installation().setExternalCache(new File("src/test/resources/external_cache"));

		File warHome = new File("src/test/resources/registry");
		Installation.installation().setWarHome(warHome);
		ExtendedPropertyManager.load(warHome);
		new ActorSimFactory().setConfiguredSimsFile(new File("src/test/resources/configuredActorSims.properties"));
		servlet = new SimServlet();
		try {
			servlet.initSimEnvironment(); 
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (InstantiationException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail();
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			fail();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			fail();
		} catch (SimEngineSubscriptionException e) {
			e.printStackTrace();
			fail();
		} catch (SimChainLoaderException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void registerTest() {
		ByConstructorLogLoader logLoader = new ByConstructorLogLoader(new File("src/test/resources/register"));
		logLoader.run(null);
		HttpServletRequest request = null;
		try {
			request = logLoader.getServletRequest();
			String simId = "123";
			DocumentRegistryActorFactory fact = new DocumentRegistryActorFactory();
			Simulator sim =
					fact.buildNewSimulator(new SimId(simId), 
							new TlsType[]  { TlsType.NOTLS }, 
							new AsyncType[] { AsyncType.SYNC });
			String endpointString = sim.getEndpoint(TransactionType.REGISTER, TlsType.NOTLS, AsyncType.SYNC);
			assertFalse(endpointString == null);
			SimEndpoint simEndpoint = new SimEndpointParser().parse(endpointString);
			Endpoint endpoint = new Endpoint().setEndpoint(endpointString);
			HttpEnvironment httpEnv = new HttpEnvironment().setResponse(new HttpServletResponseMock());
			SoapEnvironment soapEnv = new SoapEnvironment(httpEnv);
			soapEnv.setEndpoint(endpoint);

			servlet.handleSimulatorInputTransaction(request, soapEnv, simEndpoint, endpoint);
			assertTrue(servlet.getFaultSent() == null);
		} catch (HttpParseException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (XdsInternalException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} 
	}
}
