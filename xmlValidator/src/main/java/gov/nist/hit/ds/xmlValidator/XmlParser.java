package gov.nist.hit.ds.xmlValidator;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.simSupport.engine.Inject;
import gov.nist.hit.ds.simSupport.engine.SimComponent;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.utilities.string.StringUtil;
import gov.nist.hit.ds.utilities.xml.Parse;
import gov.nist.hit.ds.utilities.xml.XmlText;

import org.apache.axiom.om.OMElement;

public class XmlParser implements SimComponent, XmlMessage {
	OMElement xml;
	ErrorRecorder er;
	XmlText xmlText;
		
	public XmlMessage getXmlMessage() {
		return this;
	}

	public XmlText getXmlText() {
		return xmlText;
	}

	@Inject
	public XmlParser setXmlText(XmlText xmlText) {
		this.xmlText = xmlText;
		return this;
	}

	@Override
	public void setErrorRecorder(ErrorRecorder er) {
		this.er = er;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public void run(MessageValidatorEngine mve) {
		er.challenge("XML starts with...");
		er.detail("<" + StringUtil.firstNChars(xmlText.getXml(), 200) + ">");
		try {
			xml = Parse.parse_xml_string(xmlText.getXml());
		} catch (Exception e) {
			er.err(Code.NoCode, e);
		}
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}

}
