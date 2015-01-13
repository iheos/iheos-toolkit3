package gov.nist.hit.ds.dsSims.eb.metadataValidator.model

import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement

public class RegistryObjectModel extends AbstractRegistryObjectModel {

	public RegistryObjectModel(Metadata m, OMElement ro) throws XdsInternalException {
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


}
