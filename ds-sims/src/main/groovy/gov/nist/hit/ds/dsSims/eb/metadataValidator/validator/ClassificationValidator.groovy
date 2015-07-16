package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.AbstractRegistryObjectModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.AuthorModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.ClassAndIdDescription
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.ClassificationModel
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.toolkit.valsupport.client.ValidationContext

class ClassificationValidator extends ValComponentBase {
    SimHandle simHandle
    AbstractRegistryObjectModel model
    ValidationContext vc
    ClassAndIdDescription desc

    ClassificationValidator(SimHandle _simHandle, AbstractRegistryObjectModel _model, ValidationContext _vc, ClassAndIdDescription _desc) {
        super(_simHandle.event)
        simHandle = _simHandle
        model = _model
        vc = _vc
        desc = _desc
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rocla010', msg = 'Validating Classifications present are legal', ref = '')
    def rocla010() {
        infoFound("Scanning ${model.getClassifications().size()} ${model.identifyingString()}(s)")
        Set<String> cSchemes = new HashSet<String>();

        for (ClassificationModel c : model.getClassifications()) {
            String cScheme = c.getClassificationScheme();
            if (cScheme == null || cScheme.equals("") || !desc.definedSchemes.contains(cScheme)) {
                fail(model.identifyingString() + ": " + c.identifyingString() + " has an unknown classificationScheme attribute value: " + cScheme);
            } else {
                cSchemes.add(cScheme);
            }
        }

        for (String cScheme : cSchemes) {
            if (model.count(cSchemes as List<String>, cScheme) > 1 && !desc.multipleSchemes.contains(cScheme))
                fail(model.identifyingString() + ": " + model.classificationDescription(desc, cScheme) + " is specified multiple times, only one allowed");
        }
    }

    boolean notXdmOrXdrLimited() { !(vc.isXDM || vc.isXDRLimited)  }

    @Guard(methodNames=['notXdmOrXdrLimited'])
    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rocla020', msg = 'Validating Required Classifications present', ref = '')
    def rocla020() {
        infoFound("Scanning ${model.getClassifications().size()} ${model.identifyingString()}(s)")
        for (String cScheme : desc.requiredSchemes) {
            List<ClassificationModel> cs = model.getClassificationsByClassificationScheme(cScheme);
            if (cs.size() == 0)
                fail(model.identifyingString() + ": " + model.classificationDescription(desc, cScheme) + " is required but missing");
        }
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'rocla030', msg = 'Validating Classifications coded correctly', ref = '')
    def rocla030() {
        infoFound("Scanning ${model.getClassifications().size()} ${model.identifyingString()}(s)")
        for (ClassificationModel c : model.getClassifications())
            new ClassificationStructureValidator(simHandle, vc, c).asSelf(this).run()

        for (AuthorModel a : model.getAuthors())
            new AuthorStructureValidator(simHandle, vc, a)
    }

}
