package gov.nist.hit.ds.simulatorIntegrationTest.soapParser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.ActorTypeFactory;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.actorTransaction.TransactionTypeFactory;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.Assertion;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.httpSoapValidator.components.parsers.SoapMessageParser;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.event.EventBuilder;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.xml.Util;
import gov.nist.hit.ds.xmlValidator.XmlMessage;

import java.io.File;
import java.io.IOException;

import org.apache.axiom.om.OMElement;
import org.junit.Before;
import org.junit.Test;

public class SoapParserIT implements XmlMessage {
	File xmlInputFile;
	OMElement ele = null;
	Event event = null;
	
	@Before
	public void before() throws InitializationFailedException, IOException, RepositoryException {
		Installation.reset();
		Installation.installation().initialize();
		Configuration.configuration();
	}
	
	public void loadXml()   {
		try {
			ele = Util.parse_xml(xmlInputFile);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			event = new EventBuilder().buildEvent(new SimId("ST-reg-1"), ActorTypeFactory.find("registry").getShortName(), TransactionTypeFactory.find("register").getShortName());
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail();
		}

	}

	/*************************
	 * 
	 * XmlMessage
	 * 
	 *************************/
	
	@Override
	public OMElement getXml()  {
		if (ele == null) {
				loadXml();
		}
		return ele;
	}
	
	
	
	@Test
	public void goodTest() {
		try {
			AssertionGroup ag = run(new File("src/test/resources/soapParser_testdata/good.xml"));
			Assertion a = ag.getFirstFailedAssertion();
			assertNull(a);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void missingHeaderTest() {
		try {
			run(new File("src/test/resources/soapParser_testdata/missingHeader.xml"));
			fail();
		} catch (SoapFaultException e) {
			assertTrue(e.getFaultString().startsWith("Envelope must have 2 childern"));
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void badBodyNamespaceTest() {
		try {
			run(new File("src/test/resources/soapParser_testdata/badBodyNamespace.xml"));
			fail();
		} catch (SoapFaultException e) {
			assertTrue(e.getFaultString().startsWith("Body Namespace"));
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void extraPartTest() {
		try {
			run(new File("src/test/resources/soapParser_testdata/extraPart.xml"));
			fail();
		} catch (SoapFaultException e) {
			assertTrue(e.getFaultString().startsWith("Envelope must have 2 childern"));
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	AssertionGroup run(File xmlInputFile) throws RepositoryException, SoapFaultException {
		this.xmlInputFile = xmlInputFile;
		loadXml();
		SoapMessageParser soapParser = new SoapMessageParser();
		soapParser.setEvent(event);
		AssertionGroup ag = new AssertionGroup();
		event.addAssertionGroup(ag);
		soapParser.setAssertionGroup(ag);
		soapParser.setEvent(event);
		soapParser.setXML(this);
		soapParser.run(null);
		return ag;
	}
	
}
