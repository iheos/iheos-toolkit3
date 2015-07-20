package gov.nist.hit.ds.dsSims.eb.bad
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.soapSupport.SoapFaultException
import groovy.util.logging.Log4j
import org.apache.log4j.Logger

/**
 * Created by bmajur on 7/12/14.
 */

class FailsWithErrorValidator extends ValComponentBase {
    private static Logger log = Logger.getLogger(FailsWithErrorValidator);
    SimHandle handle

    public FailsWithErrorValidator(SimHandle handle) {
        super(handle.event)
        this.handle = handle
        setName(getClass().getSimpleName());
    }

//    @Fault(code=FaultCode.Sender)
    @Validation(id="Failing001", msg="This shall fail", ref="??")
    public void validationStep() throws SoapFaultException {
        assertEquals('a', 'b')
    }

    @Override
    public boolean showOutputInLogs() {
        return true;
    }
}
