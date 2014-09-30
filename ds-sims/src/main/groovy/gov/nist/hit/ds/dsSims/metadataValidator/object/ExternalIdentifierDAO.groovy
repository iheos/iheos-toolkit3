package gov.nist.hit.ds.dsSims.metadataValidator.object

import gov.nist.hit.ds.metadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/19/14.
 */

@groovy.transform.TypeChecked
class ExternalIdentifierDAO {
    ExternalIdentifierModel model
    RegistryObjectDAO roDAO

     OMElement toXml(OMElement owner) throws XdsInternalException  {
        model.ro = MetadataSupport.om_factory.createOMElement(MetadataSupport.externalidentifier_qnamens);
        model.ro.addAttribute("id", model.id, null);
        model.ro.addAttribute("identificationScheme", model.identificationScheme, null);
        model.ro.addAttribute("registryObject", owner.getAttributeValue(MetadataSupport.id_qname), null);
        model.ro.addAttribute("value", model.value, null);

         roDAO.addSlotsXml(model.ro);
         roDAO.addNameToXml(model.ro);
         roDAO.addDescriptionXml(model.ro);
         roDAO.addClassificationsXml(model.ro);
         roDAO.addExternalIdentifiersXml(model.ro);

        return model.ro;
    }

    ExternalIdentifierDAO(ExternalIdentifierModel model) {
        this.model = model
        roDAO = new RegistryObjectDAO(model)
    }

}
