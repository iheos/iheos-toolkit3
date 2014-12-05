package gov.nist.hit.ds.dsSims.fhir.mhd.validators

import gov.nist.hit.ds.dsSims.fhir.utilities.JsonToXml
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.Transaction
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException

/**
 * Created by bmajur on 12/4/14.
 */
class DocRefXml implements Transaction {
    SimHandle simHandle

    DocRefXml(SimHandle _simHandle) { simHandle = _simHandle }

    @Override
    ValidationStatus validateRequest() {
        assert simHandle
        assert simHandle.event.inOut.reqBody
        String reqString = new String(simHandle.event.inOut.reqBody)
        reqString = reqString.trim()
        if (reqString.startsWith('<')) {
            // XML
            def dr = new XmlSlurper().parseText(reqString)
            def validator = new MhdDocRefValidator(simHandle, dr)
            validator.asPeer().run()
        } else if (reqString.startsWith('{')) {
            // JSON
            String xmlString = new JsonToXml(reqString).xml()
            simHandle.event.artifacts.add('XML', xmlString)
            def dr = new XmlSlurper().parseText(xmlString)
            def validator = new MhdDocRefValidator(simHandle, dr)
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
