package gov.nist.hit.ds.ebDocsrcSim.transaction

import gov.nist.hit.ds.dsSims.eb.transactionSupport.EbSendRequest
import gov.nist.hit.ds.ebDocsrcSim.engine.PnrSend
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.Transaction
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.hit.ds.utilities.xml.OMFormatter
import groovy.util.logging.Log4j

/**
 * Created by bmajur on 2/3/15.
 */
@Log4j
class Pnr implements Transaction {
    SimHandle simHandle

    @Override
    ValidationStatus validateRequest() {
        return null
    }

    @Override
    ValidationStatus validateResponse() {
        return null
    }

    @Override
    ValidationStatus acceptRequest() {
        return null
    }

    EbSendRequest ebSendRequest

    // must be used for sendRequest
    Pnr(SimHandle _simHandle, EbSendRequest _ebSendRequest) {
        log.debug("client constructor called")
        simHandle = _simHandle
        ebSendRequest = _ebSendRequest
    }

    @Override
    ValidationStatus sendRequest() {
        log.debug("Sending request to ${ebSendRequest.transactionName}")
        def results = new PnrSend(simHandle, ebSendRequest).run()
        def result = results[0]
        result = new OMFormatter(result).toString()
        simHandle.event.inOut.respBody = result.bytes
        log.debug("\nresults: ${result}")
        return null
    }
}
