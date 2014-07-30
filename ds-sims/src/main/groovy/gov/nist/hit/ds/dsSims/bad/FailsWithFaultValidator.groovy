package gov.nist.hit.ds.dsSims.bad
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional
import groovy.util.logging.Log4j

@Log4j
public class FailsWithFaultValidator extends ValComponentBase {
    SimHandle handle

    public FailsWithFaultValidator(SimHandle handle) {
        super(handle.event)
        this.handle = handle
        setName(getClass().getSimpleName());
    }

    @ValidationFault(id="Failing001", required=RequiredOptional.R, msg="This shall fail", faultCode=FaultCode.Sender, ref="??")
    public void validationStep() throws SoapFaultException {
        assertEquals('a', 'b')
    }

    void run() {
        runValidationEngine()
    }

    @Override
    public boolean showOutputInLogs() {
        return true;
    }
}
