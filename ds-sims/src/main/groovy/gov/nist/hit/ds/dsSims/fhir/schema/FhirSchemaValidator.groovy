package gov.nist.hit.ds.dsSims.fhir.schema

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
class FhirSchemaValidator extends ValComponentBase {
    SimHandle simHandle
    String xmlString
    File localSchema
    boolean includesFeed

    class Handler implements ErrorHandler {

        def prefix(String str) {
            def size = 25
            if (str.size() < 25) size = str.size()
            str.trim().substring(0, size)
        }

        @Override
        void warning(SAXParseException exception) throws SAXException {
            Assertion a = new Assertion()
            a.msg = "Schema: ${exception.message.trim()}"
            a.status = AssertionStatus.WARNING
            a.expected = 'XML starts with'
            a.found = prefix(xmlString)
            a.location = "Line ${exception.lineNumber} : Col ${exception.columnNumber}"
            simHandle.event.assertionGroup.addAssertion(a, true)
        }

        @Override
        void error(SAXParseException exception) throws SAXException {
            Assertion a = new Assertion()
            a.msg = "Schema: ${exception.message.trim()}"
            a.status = AssertionStatus.ERROR
            a.expected = 'XML starts with'
            a.found = prefix(xmlString)
            a.location = "Line ${exception.lineNumber} : Col ${exception.columnNumber}"
            simHandle.event.assertionGroup.addAssertion(a, true)
        }

        @Override
        void fatalError(SAXParseException exception) throws SAXException {
            Assertion a = new Assertion()
            if (exception.message.trim().startsWith('Content is not allowed in prolog')) {
                a.msg = "Schema: ${exception.message.trim()}"
                a.status = AssertionStatus.ERROR
                a.expected = 'XML starts with'
                a.found = prefix(xmlString)
                a.location = "Line ${exception.lineNumber} : Col ${exception.columnNumber}"
                simHandle.event.assertionGroup.addAssertion(a, true)
            } else {
                a.msg = "Schema: ${exception.message.trim()}"
                a.status = AssertionStatus.ERROR
                a.expected = 'XML starts with'
                a.location = "Line ${exception.lineNumber} : Col ${exception.columnNumber}"
                simHandle.event.assertionGroup.addAssertion(a, true)
            }
            throw new SoapFaultException(simHandle.event, FaultCode.Sender, a.expected + ' ' + a.found)
        }
    }

    FhirSchemaValidator(SimHandle _simHandle, String _xmlString, File _localSchema, boolean _includesFeed) {
        super(_simHandle)
        simHandle = _simHandle
        xmlString = _xmlString
        localSchema = _localSchema
        includesFeed = _includesFeed
    }

    @Fault(code=FaultCode.Sender)
    @Validation(id='schema001', msg='Validate against Schema', ref='')
    def schema001() {
        Handler handler = new Handler()
        FhirSchemaValidation.run(xmlString, localSchema, includesFeed, handler)
    }
}
