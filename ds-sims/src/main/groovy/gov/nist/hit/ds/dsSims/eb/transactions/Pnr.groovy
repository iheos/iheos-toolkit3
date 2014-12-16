package gov.nist.hit.ds.dsSims.eb.transactions
import gov.nist.hit.ds.dsSims.eb.schema.EbSchemaValidator
import gov.nist.hit.ds.dsSims.eb.schema.MetadataTypes
import gov.nist.hit.ds.httpSoap.parsers.HttpSoapParser
import gov.nist.hit.ds.httpSoap.validators.HttpHeaderValidator
import gov.nist.hit.ds.httpSoap.validators.SoapMessageValidator
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.Transaction
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.hit.ds.toolkit.Toolkit
import gov.nist.hit.ds.utilities.html.HttpMessageContent

/**
 * Created by bmajur on 9/24/14.
 */
class Pnr implements Transaction {
    SimHandle simHandle

    def Pnr(SimHandle _simHandle) { simHandle = _simHandle }

    def schema() { simHandle.actorSimConfig.get('prb').isSchemaCheck() }

    @Override
    ValidationStatus validateRequest() {
        def headerVal = new HttpHeaderValidator(simHandle)
        headerVal.asPeer().run()
        def soapParser = new HttpSoapParser(new HttpMessageContent(headerVal.header, headerVal.body))
        def soapEnvelopeBytes  = soapParser.getSoapEnvelope()
        def soapVal = new SoapMessageValidator(simHandle, new String(soapEnvelopeBytes))
        soapVal.asPeer().run()
        if (schema()) {
            new EbSchemaValidator(simHandle, soapVal.body.toString(), MetadataTypes.METADATA_TYPE_PRb, Toolkit.schemaFile()).asPeer().run()
        }
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
