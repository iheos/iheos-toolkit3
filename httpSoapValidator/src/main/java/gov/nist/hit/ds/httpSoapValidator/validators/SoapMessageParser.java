package gov.nist.hit.ds.httpSoapValidator.validators;

import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.annotations.Inject;
import gov.nist.hit.ds.simSupport.engine.annotations.ParserOutput;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.xmlValidator.XmlMessage;

import java.util.Iterator;

import org.apache.axiom.om.OMElement;

public class SoapMessageParser extends SimComponentBase {
	OMElement xml;
	XmlMessage xmlMessage;
	OMElement header = null;
	OMElement body = null;
	OMElement root;
	int partCount;

	@Inject
	public SoapMessageParser setXML(XmlMessage xmlMessage) {
		this.xmlMessage = xmlMessage;
		return this;
	}

	@ParserOutput
	public SoapMessage getSoapMessage() {
		return new SoapMessage().
				setHeader(header).
				setBody(body).
				setRoot(root).
				setPartCount(partCount);
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException, RepositoryException {
		root = xmlMessage.getXml();
		if (root != null) {
			Iterator<?> partsIterator = root.getChildElements();

			partCount = 0;
			if (partsIterator.hasNext()) { 
				partCount++;
				header = (OMElement) partsIterator.next();
				if (partsIterator.hasNext()) {
					partCount++;
					body = (OMElement) partsIterator.next();
					partCount += countParts(partsIterator);
				}
			}
		}

		runValidationEngine();
	}

	int countParts(Iterator<?> it) {
		int cnt = 0;
		while (it.hasNext()) {
			it.next();
			cnt++;
		}
		return cnt;
	}

	@Override
	public boolean showOutputInLogs() {
		return true;
	}

}
