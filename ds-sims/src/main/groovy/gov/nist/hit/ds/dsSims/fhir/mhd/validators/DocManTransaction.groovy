package gov.nist.hit.ds.dsSims.fhir.mhd.validators

import gov.nist.hit.ds.dsSims.fhir.schema.FhirSchemaValidator
import gov.nist.hit.ds.dsSims.fhir.utilities.JsonToXmlValidator
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.Transaction
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.hit.ds.toolkit.Toolkit
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException

/**
 * Created by bmajur on 12/7/14.
 */
class DocManTransaction implements Transaction {
    SimHandle simHandle

    DocManTransaction(SimHandle _simHandle) { simHandle = _simHandle }

    @Override
    ValidationStatus validateRequest() {
        assert simHandle
        assert simHandle.event.inOut.reqBody
        String reqString = new String(simHandle.event.inOut.reqBody)
        reqString = reqString.trim()
        if (reqString.startsWith('<')) {
            // XML
            new FhirSchemaValidator(simHandle, reqString, Toolkit.schemaFile(), false).asPeer().run()
            def dr = new XmlSlurper().parseText(reqString)
            def validator = new MhdDocManValidator(simHandle, dr)
            validator.asPeer().run()
        } else if (reqString.startsWith('{')) {
            // JSON
            def jxval = new JsonToXmlValidator(simHandle, reqString).asPeer()
            jxval.run()
            String xmlString = jxval.xml()
            simHandle.event.artifacts.add('XML', xmlString)
            new FhirSchemaValidator(simHandle, xmlString, Toolkit.schemaFile(), false).asPeer().run()
            def dr = new XmlSlurper().parseText(xmlString)
            def validator = new MhdDocManValidator(simHandle, dr)
            validator.asPeer().run()
        } else {
            throw new ToolkitRuntimeException('Parse failed - do not understand format - XML or JSON required')
        }
    }

    @Override
    ValidationStatus validateResponse() {
        return null
    }

    @Override
    ValidationStatus acceptRequest() {
        return null
    }

    @Override
    ValidationStatus sendRequest() {
        return null
    }
}
