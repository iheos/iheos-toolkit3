package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

class IdUniqueValidator extends ValComponentBase {
    SimHandle simHandle
    String id
    Set<String> knownIds

    IdUniqueValidator(SimHandle _simHandle, String _id, Set<String> _knownIds) {
        super(_simHandle.event)
        simHandle = _simHandle
        id = _id
        knownIds = _knownIds
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roidu010', msg = 'ID must be unique', ref = 'ITI TF-3: 4.1.12.3 and ebRS 5.1.2')
    def roidu010() {
        if (id == null) return
        infoFound("Id is ${id}")
        assertFalse(knownIds.contains(id))
        knownIds.add(id)
    }
}
