package gov.nist.hit.ds.httpSoapValidator;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.simSupport.engine.SimElement;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;

public class EbrsRootValidator implements SimElement {
	String expectedRootName;
	ErrorRecorder er;
	SoapBody soapBody;
	
	public EbrsRootValidator(String expectedRootName) {
		this.expectedRootName = expectedRootName;
	}

	public EbrsRootValidator setSoapBody(SoapBody soapBody) {
		this.soapBody = soapBody;
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
