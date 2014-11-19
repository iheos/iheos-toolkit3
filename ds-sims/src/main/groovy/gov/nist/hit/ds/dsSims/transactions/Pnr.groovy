package gov.nist.hit.ds.dsSims.transactions
import gov.nist.hit.ds.dsSims.schema.EbSchemaValidator
import gov.nist.hit.ds.dsSims.schema.MetadataTypes
import gov.nist.hit.ds.httpSoap.validators.HttpHeaderValidator
import gov.nist.hit.ds.httpSoap.validators.SoapMessageValidator
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.Transaction
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.hit.ds.toolkit.Toolkit
/**
 * Created by bmajur on 9/24/14.
 */
class Pnr implements Transaction {
    SimHandle simHandle

    def Pnr(SimHandle _simHandle) { simHandle = _simHandle }

    @Override
    ValidationStatus validateRequest() {
        new EbSchemaValidator(simHandle, simHandle.event.inOut.reqBody as String, MetadataTypes.METADATA_TYPE_PRb, Toolkit.schemaFile()).asSelf().run()
        new HttpHeaderValidator(simHandle).asPeer().run()
        new SoapMessageValidator(simHandle, simHandle.event.inOut.reqBody as String).asPeer().run()
    }

    @Override
    ValidationStatus validateResponse() {
        return null
    }

    @Override
    ValidationStatus acceptRequest() {
        println("Running PnR transaction")
    }

    @Override
    ValidationStatus sendRequest() {
        return null
    }
}
