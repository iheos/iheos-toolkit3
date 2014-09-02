package gov.nist.hit.ds.dsSims.metadataValidator.object

import gov.nist.hit.ds.metadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/19/14.
 */

@groovy.transform.TypeChecked
class AuthorDAO {
    AuthorModel model
    RegistryObjectDAO roDAO

     OMElement toXml(OMElement parent) throws XdsInternalException  {
        model.ro = MetadataSupport.om_factory.createOMElement(MetadataSupport.classification_qnamens);
         model.ro.addAttribute(MetadataSupport.classificationscheme_qname.getLocalPart(), model.getClassificationScheme(parent), null);
         model.ro.addAttribute(MetadataSupport.classified_object_qname.getLocalPart(), parent.getAttributeValue(MetadataSupport.id_qname), null);
         model.ro.addAttribute(MetadataSupport.noderepresentation_qname.getLocalPart(), "", null);

         roDAO.addSlotsXml(model.ro);
         roDAO.addNameToXml(model.ro);
         roDAO.addDescriptionXml(model.ro);
         roDAO.addClassificationsXml(model.ro);
         roDAO.addExternalIdentifiersXml(model.ro);

        return model.ro;
    }

    AuthorDAO(AuthorModel model) {
        this.model = model
        roDAO = new RegistryObjectDAO(model)
    }
}
