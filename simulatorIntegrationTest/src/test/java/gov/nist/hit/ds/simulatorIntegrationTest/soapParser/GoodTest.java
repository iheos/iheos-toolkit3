package gov.nist.hit.ds.simulatorIntegrationTest.soapParser;

import static org.junit.Assert.fail;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.httpSoapValidator.validators.SoapParser;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.event.EventBuilder;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.xml.Util;
import gov.nist.hit.ds.xmlValidator.XmlMessage;

import java.io.File;

import org.apache.axiom.om.OMElement;
import org.junit.Before;
import org.junit.Test;

public class GoodTest implements XmlMessage {
	File xmlInputFile = new File("src/test/resources/testdata/good.xml");
	OMElement ele = null;
	Event event = null;
	
	@Before
	public void loadXml()   {
		try {
			Installation.reset();
			Installation.installation().initialize();
			Configuration.configuration();
			ele = Util.parse_xml(xmlInputFile);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			event = new EventBuilder().buildEvent(new SimId("ST-reg-1"), ActorType.REGISTRY.getShortName(), TransactionType.REGISTER.getShortName());
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
	public void runTest() {
		try {
			SoapParser soapParser = new SoapParser();
			soapParser.setEvent(event);
			AssertionGroup ag = new AssertionGroup();
			event.addAssertionGroup(ag);
			soapParser.setXML(this);
			soapParser.run(null);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
