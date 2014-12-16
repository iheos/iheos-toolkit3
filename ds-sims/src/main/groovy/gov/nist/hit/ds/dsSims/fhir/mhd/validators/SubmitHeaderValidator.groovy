package gov.nist.hit.ds.dsSims.fhir.mhd.validators

import gov.nist.hit.ds.dsSims.fhir.mhd.SubmitModel
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

/**
 * Created by bmajur on 12/14/14.
 */
class SubmitHeaderValidator  extends ValComponentBase {
    SimHandle simHandle
    SubmitModel submitModel

    SubmitHeaderValidator(SimHandle _simHandle, SubmitModel _submitModel) {
        super(_simHandle.event)
        simHandle = _simHandle
        submitModel = _submitModel
    }

    SubmitHeaderValidator() {}

    boolean isXml() { submitModel.isXml }
    boolean isJson() { !submitModel.isXml }

    @Guard(methodNames=['isXml'])
    @Validation(id='mhdsm080', msg='Mime type is application/atom+xml', ref=['3.65.4.1.2 Message Semantics'])
    def mhdsm080() { assertEquals('application/atom+xml', contentType()) }

    @Guard(methodNames=['isJson'])
    @Validation(id='mhdsm090', msg='Mime type is application/json+fhir', ref=['3.65.4.1.2 Message Semantics'])
    def mhdsm090() { assertEquals('application/json+fhir', contentType()) }

    String contentType() {
        return contentType(simHandle.event.inOut.reqHdr)
    }

    String contentType(headers) {
        headers = headers.toLowerCase().readLines()
        def contentTypeHeader = headers.find { it.startsWith('content-type')}
        if (!contentTypeHeader) return ''
        String[] parts = contentTypeHeader.split(':')
        if (parts.size() < 2) return ''
        if (parts[1].indexOf(';') != -1) {
            String[] parts2 = parts[1].split(';')
            def type = parts2[0]
            return type.trim()
        }
        return parts[1].trim()
    }
}
