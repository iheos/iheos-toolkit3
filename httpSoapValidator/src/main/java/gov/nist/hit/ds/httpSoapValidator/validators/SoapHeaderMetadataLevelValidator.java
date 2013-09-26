package gov.nist.hit.ds.httpSoapValidator.validators;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.httpSoapValidator.datatypes.MetadataLevel;
import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.annotations.Inject;
import gov.nist.hit.ds.simSupport.engine.annotations.ValidatorParameter;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.core.FaultCode;
import gov.nist.hit.ds.soapSupport.core.ValidationFault;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;

import java.util.Iterator;

import org.apache.axiom.om.OMElement;

public class SoapHeaderMetadataLevelValidator extends SimComponentBase {
	String expectedMetadataLevel = null;
	OMElement header;
	MetadataLevel level = MetadataLevel.XDS;  // default

	@ValidatorParameter
	public SoapHeaderMetadataLevelValidator setMetadataLevel(String expectedMetadataLevel) {
		this.expectedMetadataLevel = expectedMetadataLevel;
		return this;
	}

	@Inject
	public void setSoapMessage(SoapMessage soapMessage) {
		this.header = soapMessage.getHeader();
	}

	public MetadataLevel getMetadataLevel() {
		return level;
	}
	
	@ValidationFault(id="MetaLevel001", msg="Validate MetadataLevel flag", faultCode=FaultCode.Sender, ref="MU2")
	public void validateMetadataLevel() {
		
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException,
			RepositoryException {
		Iterator<?> children = header.getChildElements();
		OMElement metadataLevelEle = null;
		while (children.hasNext()) {
			OMElement child = (OMElement) children.next();
			if ("metadata-level".equals(child.getLocalName())) {
				metadataLevelEle = child;
				break;
			}
		}
		if (metadataLevelEle != null) { 
			if (!"urn:direct:addressing".equals(metadataLevelEle.getNamespace().getNamespaceURI()))
				throw new SoapFaultException(
						ag,
						FaultCode.Sender, 
						new ErrorContext(
								"Namespace on metadata-level header element must be " + "urn:direct:addressing", 
								"http://wiki.directproject.org/file/view/2011-03-09%20PDF%20-%20XDR%20and%20XDM%20for%20Direct%20Messaging%20Specification_FINAL.pdf:6.1.1 SOAP Headers"));
			String levelTxt = metadataLevelEle.getText();
			if ("minimal".equals(levelTxt))
				level = MetadataLevel.MINIMAL;
			else if ("XDS".equals(levelTxt))
				level = MetadataLevel.XDS;
			else
				throw new SoapFaultException(
						ag,
						FaultCode.Sender, 
						new ErrorContext(
								"metadata-level must be <minimal> or <XDS>.", 
								"http://wiki.directproject.org/file/view/2011-03-09%20PDF%20-%20XDR%20and%20XDM%20for%20Direct%20Messaging%20Specification_FINAL.pdf:6.1.1 SOAP Headers"));
		}
		ag.challenge("Direct");
		ag.detail("metadata-level set to <" + level + ">");

		validationEngine.run();
}

}
