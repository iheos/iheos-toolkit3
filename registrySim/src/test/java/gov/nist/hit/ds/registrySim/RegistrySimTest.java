package gov.nist.hit.ds.registrySim;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.actorSimFactory.ActorSimFactory;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.http.environment.HttpEnvironment;
import gov.nist.hit.ds.http.parser.HttpParseException;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.httpSoapValidator.testSupport.HttpServletResponseMock;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.simServlet.SimServlet;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.components.parsers.SimEndpointParser;
import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.factory.SimulatorFactory;
import gov.nist.hit.ds.simSupport.loader.ByConstructorLogLoader;
import gov.nist.hit.ds.simSupport.simrepo.SimDb;
import gov.nist.hit.ds.soapSupport.core.Endpoint;
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;


public class RegistrySimTest {
	SimServlet servlet;
	
	/**
	 * Perform initializations that would normally be done the the toolkit during startup.
	 * In production, the src/java versions would be used instead of the src/test versions.
	 * @throws InitializationFailedException 
	 * @throws IOException 
	 * @throws RepositoryException 
	 */
	@Before
	public void init() throws InitializationFailedException, IOException, RepositoryException {
		// Reuse the basic system initialization
		new FactoryTest().init();
		
		new ActorSimFactory().setConfiguredSimsFile(new File("configuredActorSims.properties"));
		
		// Initialize servlet - includes loading simulator definitions
		servlet = new SimServlet();
		try {
			servlet.initSimEnvironment(); 
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} 
	}

	/**
	 * TODO: Re-enable this test.
	 */
	public void registerTest() {
		
		SimDb simDb = null;
		try {
			
			// Create a new Registry simulator with non-TLS and sync inputs only
			String simIdString = "123";
			SimId simId = new SimId(simIdString);
			Simulator sim = null;
			try {
				SimulatorFactory simFactory = new SimulatorFactory().initializeSimulator(simId);
				simFactory.addActorSim(ActorType.REGISTRY);
				simDb = simFactory.save();
				sim = simFactory.getSimulator();
			} catch (Exception e) {
				e.printStackTrace();
				if (simDb != null) {
					simDb.delete();
					simDb = null;
				}
				fail();
			} 
			
			// verify endpoint was created
			String endpointString = sim.getEndpoint(TransactionType.REGISTER, TlsType.NOTLS, AsyncType.SYNC);
			assertFalse(endpointString == null);
			
			// Build mock servlet environment that allows test inputs to be injected
			SimEndpoint simEndpoint = new SimEndpointParser().parse(endpointString);
			Endpoint endpoint = new Endpoint().setEndpoint(endpointString);
			
			HttpEnvironment httpEnv = new HttpEnvironment().setResponse(new HttpServletResponseMock());
			SoapEnvironment soapEnv = new SoapEnvironment(httpEnv);
			soapEnv.setEndpoint(endpoint);

			
			// load the Register transaction inputs
			ByConstructorLogLoader logLoader = new ByConstructorLogLoader(new File("src/test/resources/register"));
			logLoader.run(null);

			// build HTTP request
			HttpServletRequest request = null;
			request = logLoader.getServletRequest();
			
			// initiate Register transaction through SimServlet
			Event event = new EventBuilder().buildEvent(soapEnv, simEndpoint);
			servlet.handleSimulatorInputTransaction(request, soapEnv, simEndpoint, endpoint, event);
			
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
		} catch (ToolkitRuntimeException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			if (simDb != null) {
				simDb.delete();
				simDb = null;
			}
		}
	}
	
	class EventBuilder {

		public Event buildEvent(SoapEnvironment soapEnv, SimEndpoint simEndpoint) throws SoapFaultException {
			Event event = null;
			try {
				SimDb db = new SimDb(simEndpoint.getSimId());
				SimId simId = simEndpoint.getSimId();
				RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
				Repository repos = fact.createNamedRepository(
						"Event_Repository", 
						"Event Repository", 
						new SimpleType("simEventRepository"),               // repository type
						ActorType.findActor(simEndpoint.getActor()).getShortName() + "-" + simId    // repository name
						);
				Asset eventAsset = repos.createAsset(
						db.nowAsFilenameBase(), 
						simEndpoint.getTransaction() + " Event", 
						new SimpleType("simEvent"));
				event = new Event(eventAsset);
			} catch (Exception e) {
				throw new SoapFaultException(
						null,
						FaultCode.Receiver, 
						"Internal error initializing simulator environment: " + e.getMessage());
			}
			return event;
		}

	}
}
