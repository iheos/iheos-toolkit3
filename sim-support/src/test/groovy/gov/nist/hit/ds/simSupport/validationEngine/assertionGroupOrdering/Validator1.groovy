package gov.nist.hit.ds.simSupport.validationEngine.assertionGroupOrdering
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
/**
 * Created by bmajur on 12/22/14.
 */
class Validator1 extends ValComponentBase {
    SimHandle simHandle

    Validator1(SimHandle _simHandle) {
        super(_simHandle.event)
        simHandle = _simHandle
    }

    @Validation(id="val1", msg="test", ref='')
    public void val1()  {
        infoFound(true)
    }

}
