package gov.nist.hit.ds.registrySim.registerTransaction;

import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.httpSoapValidator.components.parsers.SoapMessageParser;
import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.registrySim.store.RegIndex;
import gov.nist.hit.ds.registrySim.transactions.RegisterTransactionSim;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleAsset;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.event.EventBuilder;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.xml.Parse;
import gov.nist.hit.ds.xdsException.XMLParserException;
import gov.nist.hit.ds.xmlValidator.XmlMessage;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.axiom.om.OMElement;
import org.junit.Before;
import org.junit.Test;

public class RegisterTest {
	RegisterTransactionSim registerTransactionSim;
	
	@Before
	public void init() throws XMLParserException, FactoryConfigurationError, SoapFaultException, RepositoryException, InitializationFailedException, IOException {
		Installation.reset();
		Installation.installation().initialize();
		Installation.installation().setExternalCache(new File("src/test/resources/registerTest/external_cache"));
		Configuration.configuration();
		
		OMElement envelope = Parse.parse_xml_file("src/test/resources/registerTest/goodSubmission.xml");
		XmlHolder xml = new XmlHolder();
		xml.xml = envelope;
		SoapMessageParser soapMessageParser = new SoapMessageParser();
		soapMessageParser.setXML(xml);
		soapMessageParser.run(null);
		SoapMessage soapMessage = soapMessageParser.getSoapMessage();
		SimEndpoint simEndPoint = new SimEndpoint();
		simEndPoint.setTransaction("rb");
		registerTransactionSim = new RegisterTransactionSim();
		registerTransactionSim.setSimEndPoint(simEndPoint);
		registerTransactionSim.setSoapMessage(soapMessage);
		registerTransactionSim.setRegIndex(new RegIndex());
		Event event = new EventBuilder().buildEvent(new SimId("ST-reg-1"), ActorType.REGISTRY.getShortName(), TransactionType.REGISTER.getShortName());

		registerTransactionSim.setEvent(event);
	}
	
	@Test
	public void runTheTest() throws SoapFaultException {
		registerTransactionSim.run(null);
	}
	
	class XmlHolder implements XmlMessage {
		OMElement xml;
		
		@Override
		public OMElement getXml() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
