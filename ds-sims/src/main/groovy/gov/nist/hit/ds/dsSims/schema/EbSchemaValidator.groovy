package gov.nist.hit.ds.dsSims.schema

import gov.nist.hit.ds.eventLog.assertion.Assertion
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Fault
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXException
import org.xml.sax.SAXParseException

/**
 * Created by bmajur on 11/18/14.
 */
class EbSchemaValidator extends ValComponentBase {
    SimHandle simHandle
    String xmlString
    File localSchema
    int metadataType

    class Handler implements ErrorHandler {

        @Override
        void warning(SAXParseException exception) throws SAXException {
            Assertion a = new Assertion()
            a.found = exception.message
            a.status = AssertionStatus.WARNING
            a.expected = ''
            a.location = "Line ${exception.lineNumber} : Col ${exception.columnNumber}"
            simHandle.event.assertionGroup.addAssertion(a, true)
        }

        @Override
        void error(SAXParseException exception) throws SAXException {
            Assertion a = new Assertion()
            a.found = exception.message
            a.status = AssertionStatus.ERROR
            a.expected = ''
            a.location = "Line ${exception.lineNumber} : Col ${exception.columnNumber}"
            simHandle.event.assertionGroup.addAssertion(a, true)
        }

        @Override
        void fatalError(SAXParseException exception) throws SAXException {
            Assertion a = new Assertion()
            if (exception.message.trim().startsWith('Content is not allowed in prolog')) {
                a.msg = exception.message.trim()
                a.found = xmlString.substring(0, 25).trim()
                a.status = AssertionStatus.ERROR
                a.expected = 'XML starts with'
                a.location = "Line ${exception.lineNumber} : Col ${exception.columnNumber}"
                simHandle.event.assertionGroup.addAssertion(a, true)
            } else {
                a.msg = exception.message
                a.status = AssertionStatus.ERROR
                a.expected = ''
                a.location = "Line ${exception.lineNumber} : Col ${exception.columnNumber}"
                simHandle.event.assertionGroup.addAssertion(a, true)
            }
            throw new SoapFaultException(simHandle.event, FaultCode.Sender, a.expected + ' ' + a.found)
        }
    }

    EbSchemaValidator(SimHandle _simHandle, String _xmlString, int _metadataType, File _localSchema) {
        super(_simHandle)
        simHandle = _simHandle
        xmlString = _xmlString
        metadataType = _metadataType
        localSchema = _localSchema
    }

    @Fault(code=FaultCode.Sender)
    @Validation(id='schema001', msg='Validate against Schema', ref='')
    def schema001() {
        Handler handler = new Handler()
        EbSchemaValidation.run(xmlString, metadataType, localSchema, handler)
    }
}
