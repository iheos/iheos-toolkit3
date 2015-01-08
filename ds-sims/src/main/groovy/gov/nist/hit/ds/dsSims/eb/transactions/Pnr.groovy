package gov.nist.hit.ds.dsSims.eb.transactions

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.validator.MetadataVal
import gov.nist.hit.ds.dsSims.eb.schema.EbSchemaValidator
import gov.nist.hit.ds.dsSims.eb.schema.MetadataTypes
import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataParser
import gov.nist.hit.ds.httpSoap.parsers.HttpSoapParser
import gov.nist.hit.ds.httpSoap.validators.HttpHeaderValidator
import gov.nist.hit.ds.httpSoap.validators.SoapMessageValidator
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.Transaction
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.hit.ds.toolkit.Toolkit
import gov.nist.hit.ds.toolkit.environment.Environment
import gov.nist.hit.ds.utilities.html.HttpMessageContent
import gov.nist.hit.ds.utilities.xml.OMFormatter
import gov.nist.hit.ds.utilities.xml.Util
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 9/24/14.
 */
class Pnr implements Transaction {
    def schema(SimHandle simHandle) { simHandle.actorSimConfig.get('prb').isSchemaCheck() }

    @Override
    ValidationStatus validateRequest(SimHandle simHandle) {
        // Header
        def headerVal = new HttpHeaderValidator(simHandle)
        headerVal.asPeer().run()

        // SOAP
        def soapParser = new HttpSoapParser(new HttpMessageContent(headerVal.header, headerVal.body))
        def soapEnvelopeBytes  = soapParser.getSoapEnvelope()
        def soapVal = new SoapMessageValidator(simHandle, new String(soapEnvelopeBytes))
        soapVal.asPeer().run()

        def soapBody = soapVal.body.toString()
        OMElement soapBodyEle = Util.parse_xml(soapBody)
        OMElement root = (OMElement) soapBodyEle.childElements.next()
        String rootStr = OMFormatter.toString()
        Metadata m = MetadataParser.parse(root)
        ValidationContext vc = new ValidationContext()
        vc.isPnR = true
        vc.isRequest = true
        Environment environment = Environment.defaultEnvironment()

        // Schema
        if (schema(simHandle)) {
            new EbSchemaValidator(simHandle, rootStr, MetadataTypes.METADATA_TYPE_PRb, Toolkit.schemaFile()).asPeer().run()
        }

        // Metadata Validator
        new MetadataVal(simHandle, m, vc, environment, null).asPeer().run()
    }

    @Override
    ValidationStatus validateResponse(SimHandle simHandle) {
        return null
    }

    @Override
    ValidationStatus acceptRequest(SimHandle simHandle) {
        println("Running PnR transaction")
    }

    @Override
    ValidationStatus sendRequest(SimHandle simHandle) {
        return null
    }
}
