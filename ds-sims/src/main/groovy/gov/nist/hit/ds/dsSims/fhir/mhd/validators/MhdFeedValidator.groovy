package gov.nist.hit.ds.dsSims.fhir.mhd.validators

import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase

/**
 * Created by bmajur on 12/7/14.
 */
class MhdFeedValidator extends ValComponentBase {
    SimHandle simHandle
    def dr

    // dr is XmlSlurper representation of a DocumentReference
    MhdFeedValidator(SimHandle _simHandle, def _dr) {
        super(_simHandle.event)
        simHandle = _simHandle
        dr = _dr
    }
}
