package gov.nist.hit.ds.httpSoap.rootValidator;

import gov.nist.hit.ds.httpSoap.datatypes.SoapBody;
import gov.nist.hit.ds.simSupport.validationEngine.ValComponent;

abstract public class EbrsRootValidator implements ValComponent {
	String expectedRootName;
//	IAssertionGroup er;
	SoapBody soapBody;
	
	abstract String getExpectedRootName();
	
//	public EbrsRootValidator() {
//		this.expectedRootName = getExpectedRootName();
//	}
//
//	@SimComponentInject
//	public EbrsRootValidator setSoapBody(SoapBody soapBody) {
//		this.soapBody = soapBody;
//		return this;
//	}
//
//	@Override
//	public void setAssertionGroup(AssertionGroup er) {
//		this.er = er;
//	}
//
//	@Override
//	public String getName() {
//		return getClass().getSimpleName();
//	}
//
//	@Override
//	public String getDescription() {
//		return "Verify root element name";
//	}
//
//	@Override
//	public void run(MessageValidatorEngine mve) {
//		if (expectedRootName != null && expectedRootName.equals(soapBody.getBody().getLocalName()))
//			er.challenge("Root element is <" + expectedRootName + ">");
//		else
//			er.err(Code.XDSRegistryMetadataError, new ErrorContext("Expected root element <" + expectedRootName + "> found <" + soapBody.getBody().getLocalName() + ">", null), this);
//	}

}
