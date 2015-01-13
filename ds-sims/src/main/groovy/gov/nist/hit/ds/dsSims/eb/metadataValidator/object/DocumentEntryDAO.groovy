package gov.nist.hit.ds.dsSims.eb.metadataValidator.object

import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.xdsExceptions.XdsInternalException
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/18/14.
 */

@groovy.transform.TypeChecked
class DocumentEntryDAO {
    DocumentEntryModel model
    RegistryObjectDAO roDAO

    public OMElement toXml() throws XdsInternalException  {
        model.ro = MetadataSupport.om_factory.createOMElement(MetadataSupport.extrinsicobject_qnamens);
        model.ro.addAttribute("id", model.id, null);
        model.ro.addAttribute("mimeType", model.mimeType, null);
        if (model.status != null)
            model.ro.addAttribute("status", model.status, null);
        if (model.home != null)
            model.ro.addAttribute("home", model.home, null);

        roDAO.addSlotsXml(model.ro);
        roDAO.addNameToXml(model.ro);
        roDAO.addDescriptionXml(model.ro);
        roDAO.addClassificationsXml(model.ro);
        roDAO.addAuthorsXml(model.ro);
        roDAO.addExternalIdentifiersXml(model.ro);

        return model.ro;
    }

    DocumentEntryDAO(DocumentEntryModel model) {
        this.model = model
        roDAO = new RegistryObjectDAO(model)
    }
}
