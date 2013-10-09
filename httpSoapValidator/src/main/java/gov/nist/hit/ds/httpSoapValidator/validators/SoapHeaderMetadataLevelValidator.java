package gov.nist.hit.ds.httpSoapValidator.validators;

import gov.nist.hit.ds.httpSoapValidator.datatypes.MetadataLevel;
import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.annotations.Inject;
import gov.nist.hit.ds.simSupport.engine.annotations.ParserOutput;
import gov.nist.hit.ds.simSupport.engine.annotations.ValidatorParameter;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.core.ValidationFault;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;

import java.util.Iterator;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

public class SoapHeaderMetadataLevelValidator extends SimComponentBase {
	String expectedMetadataLevel = null;
	OMElement header;
	MetadataLevel level = MetadataLevel.XDS;  // default
	OMElement metadataLevelEle = null;
	OMNamespace namespace = null;

	@ValidatorParameter
	public SoapHeaderMetadataLevelValidator setMetadataLevel(String expectedMetadataLevel) {
		this.expectedMetadataLevel = expectedMetadataLevel;
		return this;
	}

	@Inject
	public void setSoapMessage(SoapMessage soapMessage) {
		this.header = soapMessage.getHeader();
	}

	@ParserOutput
	public MetadataLevel getMetadataLevel() {
		return level;
	}

	@ValidationFault(id="MetaLevel001", msg="Find metadata-level header in SOAP Header", faultCode=FaultCode.Sender, ref="MU2")
	public void findMetadataLevel() throws SoapFaultException {
		Iterator<?> children = header.getChildElements();
		while (children != null && children.hasNext()) {
			OMElement child = (OMElement) children.next();
			if ("metadata-level".equals(child.getLocalName())) {
				metadataLevelEle = child;
				break;
			}
		}
		infoFound(metadataLevelEle != null);
	}

	@ValidationFault(id="MetaLevel002", dependsOn="MetaLevel001", msg="Metadata-level namespace must be present", faultCode=FaultCode.Sender, ref="http://wiki.directproject.org/file/view/2011-03-09%20PDF%20-%20XDR%20and%20XDM%20for%20Direct%20Messaging%20Specification_FINAL.pdf:6.1.1 SOAP Headers")
	public void parseMetadataLevelNamespace() throws SoapFaultException {
		if (metadataLevelEle == null)
			return;
		namespace = metadataLevelEle.getNamespace();
		assertNotNull(namespace);
	}


	@ValidationFault(id="MetaLevel003", dependsOn="MetaLevel002", msg="Validate metadata-level namespace", faultCode=FaultCode.Sender, ref="http://wiki.directproject.org/file/view/2011-03-09%20PDF%20-%20XDR%20and%20XDM%20for%20Direct%20Messaging%20Specification_FINAL.pdf:6.1.1 SOAP Headers")
	public void validateMetadataLevelNamespace() throws SoapFaultException {
		if (metadataLevelEle == null)
			return;
		assertEquals("urn:direct:addressing", namespace.getNamespaceURI());
	}	

	@ValidationFault(id="MetaLevel004", dependsOn="MetaLevel001", msg="metadata-level must be <minimal> or <XDS>.", faultCode=FaultCode.Sender, ref="http://wiki.directproject.org/file/view/2011-03-09%20PDF%20-%20XDR%20and%20XDM%20for%20Direct%20Messaging%20Specification_FINAL.pdf:6.1.1 SOAP Headers")
	public void validateMetadataLevelValue() throws SoapFaultException {
		if (metadataLevelEle == null)
			return;
		String levelTxt = metadataLevelEle.getText();
		String[] expected = {"minimal", "XDS" };
		assertIn(expected, levelTxt);
		if ("minimal".equals(levelTxt)) 
			level = MetadataLevel.MINIMAL;
		else if ("XDS".equals(levelTxt)) 
			level = MetadataLevel.XDS;
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException,
	RepositoryException {
		runValidationEngine();
	}

}
