package gov.nist.hit.ds.dsSims.metadataValidator.engine
import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.dsSims.metadataValidator.RegistryValidationInterface
import gov.nist.hit.ds.dsSims.metadataValidator.object.SubmissionSetModel
import gov.nist.hit.ds.dsSims.metadataValidator.object.SubmissionSetValidator
import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.metadata.MetadataSupport
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/4/14.
 */
@groovy.transform.TypeChecked
class ObjectStructureValidator extends ValComponentBase {
    Metadata m
    SimHandle simHandle
    ValidationContext vc
    RegistryValidationInterface rvi;

    Set<String> knownIds = new HashSet<String>();


    public ObjectStructureValidator(SimHandle simHandle, Metadata m, ValidationContext vc, RegistryValidationInterface rvi) {
        super(simHandle.event)
        this.simHandle = simHandle
        this.m = m
        this.vc = vc
        this.rvi = rvi
    }

    @Override
    void run() {
        if (vc.skipInternalStructure)
            return;
        runValidationEngine()

        for (OMElement ssEle : m.getSubmissionSets()) {
            SubmissionSetModel ssModel = new SubmissionSetModel(m, ssEle)
            simHandle.event.addChildResults('SubmissionSet(' + ssEle.getAttributeValue(MetadataSupport.id_qname) + ")")
            new SubmissionSetValidator(simHandle, ssModel, vc, knownIds).run()
        }
    }
}
