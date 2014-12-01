package gov.nist.hit.ds.dsSims.eb.metadataValidator.object

import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/18/14.
 */
class RegistryObjectDAO {
    RegistryObjectModel model

    RegistryObjectDAO(RegistryObjectModel model) { this.model = model }
    public void addNameToXml(OMElement parent) {

        if (model.name != null && !model.name.equals("")) {
            OMElement n = MetadataSupport.om_factory.createOMElement(MetadataSupport.name_qnamens);
            OMElement locstr = MetadataSupport.om_factory.createOMElement(MetadataSupport.localizedstring_qnamens);
            n.addChild(locstr);
            locstr.addAttribute("value", model.name, null);
            parent.addChild(n);
        }
    }

    public void addDescriptionXml(OMElement parent) {

        if (model.description != null && !model.description.equals("")) {
            OMElement n = MetadataSupport.om_factory.createOMElement(MetadataSupport.description_qnamens);
            OMElement locstr = MetadataSupport.om_factory.createOMElement(MetadataSupport.localizedstring_qnamens);
            n.addChild(locstr);
            locstr.addAttribute("value", model.description, null);
            parent.addChild(n);
        }
    }

    public void addVersionXml(OMElement parent) {
        OMElement n = MetadataSupport.om_factory.createOMElement(MetadataSupport.versioninfo_qnamens);
        n.addAttribute("versionName", model.version, null);
        parent.addChild(n);
    }

    public void addSlotsXml(OMElement parent) {
        for (SlotValidator s : model.slots) {
            parent.addChild(s.toXML());
        }
    }

    public void addClassificationsXml(OMElement parent) throws XdsInternalException  {
        for (ClassificationValidator c : model.classifications) {
            OMElement cl = c.toXml(parent);
            parent.addChild(cl);

        }
        if (model.internalClassifications != null) {
            for (InternalClassificationValidator ic : model.internalClassifications)
                parent.addChild(ic.toXml());
        }
    }

    public void addAuthorsXml(OMElement parent) throws XdsInternalException  {
        for (AuthorValidator a : model.authors) {
            OMElement ele = a.toXml(parent);
            parent.addChild(ele);
        }
    }

    public void addExternalIdentifiersXml(OMElement parent) throws XdsInternalException  {
        for (ExternalIdentifierValidator ei : model.externalIdentifiers) {
            OMElement ele = ei.toXml(parent);
            parent.addChild(ele);
        }
    }
}
