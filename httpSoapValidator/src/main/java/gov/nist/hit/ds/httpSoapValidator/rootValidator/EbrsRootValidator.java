package gov.nist.hit.ds.httpSoapValidator.rootValidator;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapBody;
import gov.nist.hit.ds.simSupport.engine.SimComponent;
import gov.nist.hit.ds.simSupport.engine.annotations.Inject;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;

abstract public class EbrsRootValidator implements SimComponent {
	String expectedRootName;
	IAssertionGroup er;
	SoapBody soapBody;
	
	abstract String getExpectedRootName();
	
	public EbrsRootValidator() {
		this.expectedRootName = getExpectedRootName();
	}

	@Inject
	public EbrsRootValidator setSoapBody(SoapBody soapBody) {
		this.soapBody = soapBody;
		return this;
	}

	@Override
	public void setAssertionGroup(AssertionGroup er) {
		this.er = er;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public String getDescription() {
		return "Verify root element name";
	}

	@Override
	public void run(MessageValidatorEngine mve) {
		if (expectedRootName != null && expectedRootName.equals(soapBody.getBody().getLocalName())) 
			er.challenge("Root element is <" + expectedRootName + ">");
		else
			er.err(Code.XDSRegistryMetadataError, new ErrorContext("Expected root element <" + expectedRootName + "> found <" + soapBody.getBody().getLocalName() + ">", null), this);
	}

}
