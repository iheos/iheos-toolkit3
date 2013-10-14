package gov.nist.hit.ds.simulatorIntegrationTest.soapHeaderValidator;

import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.Assertion;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage;
import gov.nist.hit.ds.httpSoapValidator.validators.SoapHeaderValidator;
import gov.nist.hit.ds.httpSoapValidator.validators.SoapMessageParser;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.event.EventBuilder;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.xml.Util;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.axiom.om.OMElement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO: relatesTo
 * TODO: from
 * @author bill
 *
 */

public class SoapHeaderValidatorIT  {
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
		event = new EventBuilder().buildEvent(new SimId("ST-reg-1"), ActorType.REGISTRY.getShortName(), TransactionType.REGISTER.getShortName());
	}

	@Test
	public void goodTest() {
		try {
			AssertionGroup ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/good.xml"));
			run();
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNull(a);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void messageIdNamespaceTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/messageidnamespace.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA011", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void toNamespaceTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/tonamespace.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA030", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void actionValueTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/good.xml"));
			val.setExpectedWsAction("urn:ihe:iti:2007:RegisterDocumentSet");
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA290", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void actionNamespaceTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/actionnamespace.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA040", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void replyToNamespaceTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/replytonamespace.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA060", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void faultToNamespaceTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/faulttonamespace.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA070", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void multipleToTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/multipleto.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA080", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void multipleFromTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/multiplefrom.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA090", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void multipleReplyToTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/multiplereplyto.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA100", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void multipleFaultToTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/multiplefaultto.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA110", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void multipleActionTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/multipleaction.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA120", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void multipleMessageIdTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/multiplemessageid.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA130", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void noMustUnderstandTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/nomustunderstand.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA140", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void replyToHttpTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/replytohttp.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA150", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void fromHttpTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/fromhttp.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA160", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void faultToHttpTest() {
		AssertionGroup ag = null;
		try {
			ag = setup(new File("src/test/resources/soapHeaderValidator_testdata/faulttohttp.xml"));
			run();
			Assert.fail();
		} 
		catch (SoapFaultException e) {
			Assertion a = ag.getFirstFailedAssertion();
			Assert.assertNotNull(a);
			Assert.assertEquals("WSA170", a.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}



	SoapHeaderValidator val = null;
	
	AssertionGroup setup(File xmlInputFile) throws RepositoryException, SoapFaultException, XdsInternalException, FactoryConfigurationError {
		init();
		this.xmlInputFile = xmlInputFile;

		ele = Util.parse_xml(xmlInputFile);
		SoapMessageParser smp = new SoapMessageParser();
		smp.parse(ele);
		SoapMessage soapMessage = smp.getSoapMessage();
		val = new SoapHeaderValidator();
		val.setSoapMessage(soapMessage);
		val.setExpectedWsAction("urn:ihe:iti:2007:RegisterDocumentSet-b");
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
