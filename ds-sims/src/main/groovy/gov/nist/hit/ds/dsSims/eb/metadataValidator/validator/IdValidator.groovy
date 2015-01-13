package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.UuidFormatValidator
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

class IdValidator extends ValComponentBase {
    SimHandle simHandle
    ValidationContext vc
    String attName
    String attValue

    IdValidator(SimHandle _simHandle, ValidationContext _vc, String _attName, String _attValue) {
        super(_simHandle.event)
        simHandle = _simHandle
        vc = _vc
        attName = _attName
        attValue = _attValue
    }

    @Validation(id = 'roid010', msg = '', ref = 'ITI TF-3: 4.1.12.3')
    def roid010() {
        infoFound("Id ${attName}")
        assertHasValue(attValue)
        if (!attValue) return
        if (vc.isSQ && vc.isResponse) new UuidFormatValidator(simHandle, attValue).asSelf(this).run()
        else if(attValue.startsWith("urn:uuid:")) new UuidFormatValidator(simHandle, attValue).asSelf(this).run()
    }
}