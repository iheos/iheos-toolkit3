package gov.nist.hit.ds.dsSims.metadataValidator.object

import gov.nist.hit.ds.metadata.MetadataSupport
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/18/14.
 */
@groovy.transform.TypeChecked
class SubmissionSetDAO {
    SubmissionSetModel model
    RegistryObjectDAO roDAO

    public OMElement toXml() throws XdsInternalException  {
        OMElement ro = MetadataSupport.om_factory.createOMElement(MetadataSupport.registrypackage_qnamens);
        ro.addAttribute("id", model.id, null);
        if (model.status != null)
            ro.addAttribute("status", model.status, null);
        if (model.home != null)
            ro.addAttribute("home", model.home, null);

        roDAO.addSlotsXml(ro);
        roDAO.addNameToXml(ro);
        roDAO.addDescriptionXml(ro);
        roDAO.addClassificationsXml(ro);
        roDAO.addAuthorsXml(ro);
        roDAO.addExternalIdentifiersXml(ro);

        return ro;
    }

    SubmissionSetDAO(SubmissionSetModel model) {
        this.model = model
        roDAO = new RegistryObjectDAO(model)
    }
}
