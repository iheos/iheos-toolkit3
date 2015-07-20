package gov.nist.hit.ds.simServlet.rest

import gov.nist.hit.ds.simSupport.simulator.SimHandle
import groovy.xml.MarkupBuilder

/**
 * Created by bmajur on 11/6/14.
 */

class TransactionReportBuilder {

    String build(SimHandle simHandle) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.transactionLog(type: simHandle.actorSimConfig.actorType.shortName, simId:simHandle.simId.id) {
            request() {
                header(simHandle.event.inOut.reqHdr)
                body(new String(simHandle.event.inOut.reqBody))
            }
            response() {
                header(simHandle.event.inOut.respHdr)
                body(new String(simHandle.event.inOut.respBody))
            }
        }

        return writer.toString()
    }
}
