package gov.nist.hit.ds.dsSims.eb.transactionSupport

import gov.nist.hit.ds.simSupport.client.SimIdentifier

/**
 * Created by bmajur on 1/24/15.
 */
class EbSendRequest {
    SimIdentifier simIdentifier
    String metadata = null
    String transactionName
    String extraHeaders
    String messageId = null   // use default if null
    Map<String, DocumentHandler> documents = [ : ]
    boolean tls

    public String toString() {
        "SendRequest: to(${simIdentifier} with TLS ${tls})"
    }
}
