package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SlotModel
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

class SlotsUniqueValidator extends ValComponentBase {
    SimHandle simHandle
    List<SlotModel> slots

    SlotsUniqueValidator(SimHandle _simHandle, List<SlotModel> _slots) {
        super(_simHandle.event)
        simHandle = _simHandle
        slots = _slots
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'roslotu010', msg = 'Verify slot names unique', ref = 'ebRIM 3.0 section 2.8.2')
    def roslotu010() {
        List<String> names = new ArrayList<String>();
        boolean legal = true
        int count = 0
        for (SlotModel slot : slots) {
            if (names.contains(slot.getName())) {
                fail("Slot ${slot.getName()} is multiply defined");
                names.add(slot.getName());
                legal = false
            }
            count++
        }
        found("${count}")
        if (legal) { success() }
    }
}
