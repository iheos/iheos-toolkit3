package gov.nist.hit.ds.simulatorIntegrationTest.soapMessageValidator;

import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.ActorTypeFactory;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.actorTransaction.TransactionTypeFactory;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.Assertion;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.httpSoapValidator.components.parsers.SoapMessageParser;
import gov.nist.hit.ds.httpSoapValidator.components.validators.SoapMessageValidator;
import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.event.EventBuilder;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.xml.Util;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.axiom.om.OMElement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SoapMessageValidatorIT {
	File xmlInputFile;
	OMElement ele = null;
	Event event = null;

	@Before
	public void before() throws InitializationFailedException, IOException, RepositoryException {
		Installation.reset();
		Installation.installation().initialize();
		Configuration.configuration();
	}

	void init() throws RepositoryException   {		
		event = new EventBuilder().buildEvent(new SimId("ST-reg-1"), ActorTypeFactory.find("registry").getShortName(), TransactionTypeFactory.find("register").getShortName());
	}

	/**************************/

	@Test
	public void goodTest() {
		try {
			AssertionGroup ag = setup(new File("src/test/resources/soapMessageValidator_testdata/good.xml"));
			run();
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNull(a);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void noHeaderTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapMessageValidator_testdata/singlechild.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("SOAP030", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void badOrderTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapMessageValidator_testdata/badorder.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("SOAP040", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void headerNamespaceTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapMessageValidator_testdata/headernamespace.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("SOAP050", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void notEnvelopeTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapMessageValidator_testdata/notenvelope.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("SOAP011", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void badBodyTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapMessageValidator_testdata/badbody.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("SOAP060", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void bodyNamespaceTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapMessageValidator_testdata/bodynamespace.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("SOAP070", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	SoapMessageValidator val = null;
	
	AssertionGroup setup(File xmlInputFile) throws RepositoryException, SoapFaultException, ToolkitRuntimeException, FactoryConfigurationError {
		init();
		this.xmlInputFile = xmlInputFile;

		ele = Util.parse_xml(xmlInputFile);
		SoapMessageParser smp = new SoapMessageParser();
		smp.parse(ele);
		SoapMessage soapMessage = smp.getSoapMessage();
		val = new SoapMessageValidator();
		val.setSoapMessage(soapMessage);
		val.setEvent(event);
		AssertionGroup ag = new AssertionGroup();
		event.addAssertionGroup(ag);
		val.setAssertionGroup(ag);
		val.setEvent(event);
		return ag;
	}
	
	void run() throws SoapFaultException, RepositoryException {
		val.runValidationEngine();
	}

}
