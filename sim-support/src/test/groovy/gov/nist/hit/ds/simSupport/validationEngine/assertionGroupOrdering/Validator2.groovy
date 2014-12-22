package gov.nist.hit.ds.simSupport.validationEngine.assertionGroupOrdering

import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

/**
 * Created by bmajur on 12/22/14.
 */
class Validator2 extends ValComponentBase {
    SimHandle simHandle

    Validator2(SimHandle _simHandle) {
        super(_simHandle.event)
        simHandle = _simHandle
    }

    @Validation(id="val2", msg="test", ref='')
    public void val2()  {
        infoFound(true)
    }
}
