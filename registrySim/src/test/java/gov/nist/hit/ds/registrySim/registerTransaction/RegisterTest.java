package gov.nist.hit.ds.registrySim.registerTransaction;

import gov.nist.hit.ds.actorTransaction.ActorTypeFactory;
import gov.nist.hit.ds.actorTransaction.TransactionTypeFactory;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.httpSoap.components.parsers.SoapMessageParser;
import gov.nist.hit.ds.httpSoap.datatypes.SoapMessage;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.registrySim.transactions.RegisterTransactionSim;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.event.EventBuilder;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.xml.Parse;
import gov.nist.hit.ds.xdsException.XMLParserException;
import gov.nist.hit.ds.xmlValidator.XmlMessage;
import org.apache.axiom.om.OMElement;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.parsers.FactoryConfigurationError;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.fail;

public class RegisterTest {
	RegisterTransactionSim registerTransactionSim;
	
	@BeforeClass
	public static void init() throws XMLParserException, FactoryConfigurationError, SoapFaultException, RepositoryException, InitializationFailedException, IOException {
		LogManager.getRootLogger().setLevel(Level.DEBUG);
		Installation.reset();
		Installation.installation().initialize();
		Installation.installation().setExternalCache(new File("src/test/resources/registerTest/external_cache"));
		Configuration.configuration();		
	}

	private Event setup(String submissionFile) throws FactoryConfigurationError,
			XMLParserException, SoapFaultException, RepositoryException {
		OMElement envelope = Parse.parse_xml_file(submissionFile);
		XmlHolder xml = new XmlHolder();
		xml.xml = envelope;
		SoapMessageParser soapMessageParser = new SoapMessageParser();
		soapMessageParser.setXML(xml);
		soapMessageParser.run(null);
		SoapMessage soapMessage = soapMessageParser.getSoapMessage();
		SimEndpoint simEndPoint = new SimEndpoint();
		simEndPoint.setTransaction("rb");
		registerTransactionSim = new RegisterTransactionSim();
		registerTransactionSim.setSoapMessage(soapMessage);
//		RegIndex regIndex = new RegIndex("src/test/resources/registerTest/external_cache/regindex.dat");
//		registerTransactionSim.setRegIndex(regIndex);
		Event event = new EventBuilder().buildEvent(new SimId("ST-reg-1"), ActorTypeFactory.find("registry").getShortName(), TransactionTypeFactory.find("register").getShortName());
		AssertionGroup ag = new AssertionGroup();
		event.addAssertionGroup(ag);
		registerTransactionSim.setAssertionGroup(ag);
		registerTransactionSim.setEvent(event);
		return event;
	}
	
	@Test
	public void forceErrorIsDetectedTest() throws SoapFaultException, XMLParserException, FactoryConfigurationError, RepositoryException {
		Event event = setup("src/test/resources/registerTest/goodSubmission.xml");
//		registerTransactionSim.setRegIndex(new RegIndex("ThisFileDoesNotExist"));
		registerTransactionSim.run(null);
		if (!event.hasErrors())
			fail("Has Errors");
		event.flush();
	}
	
	@Test
	public void goodTest() throws SoapFaultException, XMLParserException, FactoryConfigurationError, RepositoryException {
		Event event = setup("src/test/resources/registerTest/goodSubmission.xml");
		registerTransactionSim.run(null);
		if (event.hasErrors())
			fail("Has Errors");
		event.flush();
	}
	
	@Test
	public void badAssocTest() throws SoapFaultException, XMLParserException, FactoryConfigurationError, RepositoryException {
		Event event = setup("src/test/resources/registerTest/badAssocLinkSubmission.xml");
		registerTransactionSim.run(null);
		if (!event.hasErrors())
			fail("Has no Errors");
		event.flush();
	}
	
	class XmlHolder implements XmlMessage {
		OMElement xml;
		
		@Override
		public OMElement getXml() {
			return xml;
		}
		
	}
}
