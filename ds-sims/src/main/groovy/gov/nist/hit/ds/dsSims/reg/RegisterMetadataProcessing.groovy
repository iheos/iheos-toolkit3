package gov.nist.hit.ds.dsSims.reg
import gov.nist.hit.ds.eventLog.assertion.annotations.Validation
import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.metadata.MetadataParser
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 7/13/14.
 */
class RegisterMetadataProcessing extends ValComponentBase {
    OMElement soapBody
    Metadata m

    RegisterMetadataProcessing(SimHandle handle, OMElement soapBody) {
        super(handle.event)
        this.soapBody = soapBody
    }

    @Validation(id="RegisterMetadataProcessing001", required=RequiredOptional.R, msg="Parse XML", ref="??")
    public void parseMetadata() throws SoapFaultException {
        m = MetadataParser.parse(soapBody)
    }

    @Override
    public void run() throws SoapFaultException, RepositoryException {
        runValidationEngine();
    }

    @Override
    public boolean showOutputInLogs() {
        return true
    }

}
