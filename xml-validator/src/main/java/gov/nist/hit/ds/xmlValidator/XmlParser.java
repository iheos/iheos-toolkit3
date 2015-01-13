package gov.nist.hit.ds.xmlValidator;

import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.engine.SimComponent;
import gov.nist.hit.ds.simSupport.engine.annotations.SimComponentInject;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.utilities.string.StringUtil;
import gov.nist.hit.ds.utilities.xml.Parse;
import gov.nist.hit.ds.utilities.xml.XmlText;
import gov.nist.hit.ds.xdsExceptions.XMLParserException;

import org.apache.axiom.om.OMElement;

public class XmlParser implements SimComponent, XmlMessage {
	OMElement xml;
	AssertionGroup ag;
	XmlText xmlText;
	Event event;
		
	public XmlMessage getXmlMessage() {
		return this;
	}

	public XmlText getXmlText() {
		return xmlText;
	}

	@SimComponentInject
	public XmlParser setXmlText(XmlText xmlText) {
		this.xmlText = xmlText;
		return this;
	}

	@Override
	public void setAssertionGroup(AssertionGroup er) {
		this.ag = er;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public void run(MessageValidatorEngine mve) throws RepositoryException {
		event.addArtifact("XML Starts", StringUtil.firstNChars(xmlText.getXml(), 200));
		try {
			parse();
		} catch (Exception e) {
			ag.err(Code.NoCode, e);
		}
	}

	public OMElement parse() throws XMLParserException {
		xml = Parse.parse_xml_string(xmlText.getXml());
		return xml;
	}

	@Override
	public OMElement getXml() {
		return xml;
	}

	@Override
	public String getDescription() {
		return "Run the XML Parser";
	}

	@Override
	public void setName(String name) {
		
	}

	@Override
	public void setDescription(String description) {
		
	}

	@Override
	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public boolean showOutputInLogs() {
		return true;
	}

}
