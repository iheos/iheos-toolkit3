package gov.nist.hit.ds.dsSims.msgs

import gov.nist.hit.ds.dsSims.msgModels.RegistryResponse
import gov.nist.hit.ds.metadata.MetadataSupport
import gov.nist.hit.ds.utilities.xml.XmlUtil
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 10/7/14.
 */
class RegistryResponseGenerator {
    RegistryResponse registryResponse

    RegistryResponseGenerator(RegistryResponse _registryResponse) {
        registryResponse = _registryResponse
    }

    OMElement toXml() {
        OMElement response = XmlUtil.om_factory.createOMElement("RegistryResponse", MetadataSupport.ebRSns3);
        String status = getStatus()
        response.addAttribute('status', status, null)
        return response
    }

    // TODO: This does not consider cross-community or Retrieve yet
    String getStatus() {
        if (registryResponse.hasErrors()) return MetadataSupport.status_failure
        return MetadataSupport.status_success
    }
}
