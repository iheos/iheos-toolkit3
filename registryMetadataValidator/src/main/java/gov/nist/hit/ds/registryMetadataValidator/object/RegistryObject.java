package gov.nist.hit.ds.registryMetadataValidator.object;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.valSupport.client.ValidationContext;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import org.apache.axiom.om.OMElement;

public class RegistryObject extends AbstractRegistryObject {

	public RegistryObject(Metadata m, OMElement ro) throws XdsInternalException  {
		super(m, ro);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String identifyingString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OMElement toXml()  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateRequiredSlotsPresent(IAssertionGroup er,
			ValidationContext vc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateSlotsCodedCorrectly(IAssertionGroup er,
			ValidationContext vc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateSlotsLegal(IAssertionGroup er) {
		// TODO Auto-generated method stub

	}

}
