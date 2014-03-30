package gov.nist.hit.ds.registryMetadataValidator.object;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.valSupport.client.ValidationContext;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import org.apache.axiom.om.OMElement;

public class RegistryObject extends AbstractRegistryObject {

	public RegistryObject(Metadata m, OMElement ro) throws ToolkitRuntimeException  {
		super(m, ro);
	}

	@Override
	public String identifyingString() {
		return null;
	}

	@Override
	public OMElement toXml()  {
		return null;
	}

	@Override
	public void validateRequiredSlotsPresent(IAssertionGroup er,
			ValidationContext vc) {

	}

	@Override
	public void validateSlotsCodedCorrectly(IAssertionGroup er,
			ValidationContext vc) {

	}

	@Override
	public void validateSlotsLegal(IAssertionGroup er) {

	}

}
