package gov.nist.hit.ds.dsSims.eb.transactions

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.validator.MetadataVal
import gov.nist.hit.ds.dsSims.eb.schema.EbSchemaValidator
import gov.nist.hit.ds.dsSims.eb.schema.MetadataTypes
import gov.nist.hit.ds.dsSims.eb.transactionSupport.EbSendRequest
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
import groovy.util.logging.Log4j
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 9/24/14.
 */
@Log4j
class Pnr implements Transaction {
    SimHandle simHandle

    def Pnr(SimHandle _simHamdle) { simHandle = _simHamdle}

    def schema(SimHandle simHandle) { simHandle.actorSimConfig.get('prb')?.isSchemaCheck() }

    @Override
    ValidationStatus validateRequest() {
        // Header
        def headerVal = new HttpHeaderValidator(simHandle)
        headerVal.asPeer().run()

        // SOAP
        def soapParser
        def soapEnvelopeBytes
        def soapVal
        try {
            soapParser = new HttpSoapParser(new HttpMessageContent(headerVal.header, headerVal.body))
            soapEnvelopeBytes = soapParser.getSoapEnvelope()
            soapVal = new SoapMessageValidator(simHandle, new String(soapEnvelopeBytes))
        } catch (Exception e) {
            simHandle.event.assertionGroup.internalError(e)
            return ValidationStatus.ERROR
        }
        soapVal.asPeer().run()

        OMElement soapBodyEle = soapVal.body
        OMElement msgRoot = (OMElement) soapBodyEle.childElements.next()
        Metadata m = MetadataParser.parse(soapBodyEle)
        ValidationContext vc = new ValidationContext()
        vc.isPnR = true
        vc.isRequest = true
        Environment environment = Environment.getDefaultEnvironment()

        // Schema
        if (schema(simHandle)) {
            new EbSchemaValidator(simHandle, new OMFormatter(msgRoot).toString(), MetadataTypes.METADATA_TYPE_PRb_WIRE, Toolkit.schemaFile()).asPeer().run()
        }

        // Metadata Validator
        new MetadataVal(simHandle, m, vc, environment, null).asPeer().run()
    }

    @Override
    ValidationStatus validateResponse() {
        // Header
        def headerVal = new HttpHeaderValidator(simHandle)
        headerVal.asPeer().run()

        // SOAP
        def soapParser
        def soapEnvelopeBytes
        def soapVal
        try {
            soapParser = new HttpSoapParser(new HttpMessageContent(headerVal.header, headerVal.body))
            soapEnvelopeBytes = soapParser.getSoapEnvelope()
            soapVal = new SoapMessageValidator(simHandle, new String(soapEnvelopeBytes))
        } catch (Exception e) {
            simHandle.event.assertionGroup.internalError(e)
            return ValidationStatus.ERROR
        }
        soapVal.asPeer().run()

        OMElement soapBodyEle = soapVal.body
        OMElement msgRoot = (OMElement) soapBodyEle.childElements.next()
        Metadata m = MetadataParser.parse(soapBodyEle)
        ValidationContext vc = new ValidationContext()
        vc.isPnR = true
        vc.isRequest = false
        Environment environment = Environment.getDefaultEnvironment()

        // Schema
        if (schema(simHandle)) {
            new EbSchemaValidator(simHandle, new OMFormatter(msgRoot).toString(), MetadataTypes.METADATA_TYPE_PRb_WIRE, Toolkit.schemaFile()).asPeer().run()
        }
        // Metadata Validator
        new MetadataVal(simHandle, m, vc, environment, null).asPeer().run()
    }

    @Override
    ValidationStatus acceptRequest() {
        println("Running PnR transaction")
    }

    EbSendRequest ebSendRequest

    // must be used for sendRequest
    Pnr(SimHandle _simHandle, EbSendRequest _ebSendRequest) {
        log.debug("send constructor called")
        simHandle = _simHandle
        ebSendRequest = _ebSendRequest
    }

    @Override
    ValidationStatus sendRequest() {
        log.debug("Sending request to ${ebSendRequest.transactionName}")
        return null
    }
}
