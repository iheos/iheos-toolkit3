package gov.nist.hit.ds.dsSims.fhir.mhd.validators
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.Transaction
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
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
        String xmlString = new String(simHandle.event.inOut.reqBody)
        def dr = new XmlSlurper().parseText(xmlString)
        def validator = new MhdDocRefValidator(simHandle, dr)
        validator.asPeer().run()
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
