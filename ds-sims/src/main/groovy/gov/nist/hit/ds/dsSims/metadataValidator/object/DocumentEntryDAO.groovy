package gov.nist.hit.ds.dsSims.metadataValidator.object

import gov.nist.hit.ds.metadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/18/14.
 */
class DocumentEntryDAO {
    DocumentEntryModel model
    RegistryObjectDAO roDAO

    public OMElement toXml() throws XdsInternalException  {
        ro = MetadataSupport.om_factory.createOMElement(MetadataSupport.extrinsicobject_qnamens);
        ro.addAttribute("id", id, null);
        ro.addAttribute("mimeType", mimeType, null);
        if (status != null)
            ro.addAttribute("status", status, null);
        if (home != null)
            ro.addAttribute("home", home, null);

        addSlotsXml(ro);
        addNameToXml(ro);
        addDescriptionXml(ro);
        addClassificationsXml(ro);
        addAuthorsXml(ro);
        addExternalIdentifiersXml(ro);

        return ro;
    }

    DocumentEntryDAO(DocumentEntryModel model) {
        this.model = model
        roDAO = new RegistryObjectDAO(model)
    }
}
