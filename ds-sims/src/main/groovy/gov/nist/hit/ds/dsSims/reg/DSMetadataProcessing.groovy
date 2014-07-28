package gov.nist.hit.ds.dsSims.reg
import gov.nist.hit.ds.eventLog.assertion.annotations.Validation
import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.metadata.MetadataParser
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional
import gov.nist.hit.ds.xdsException.MetadataException
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 7/13/14.
 */
class DSMetadataProcessing extends ValComponentBase {
    OMElement soapBody
    Metadata m

    DSMetadataProcessing(SimHandle handle, OMElement soapBody) {
        super(handle.event)
        this.soapBody = soapBody
    }

    @Validation(id="MetadataParser001", required=RequiredOptional.R, msg="Parse Metadata", ref="??")
    public void parseMetadata() throws SoapFaultException {
        try {
            m = MetadataParser.parse(soapBody)
        } catch (MetadataException e) {
            fail(e.message)
        }
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
