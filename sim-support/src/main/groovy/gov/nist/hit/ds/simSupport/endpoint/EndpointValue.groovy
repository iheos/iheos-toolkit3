package gov.nist.hit.ds.simSupport.endpoint

import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier

/**
 * Created by bmajur on 6/6/14.
 */
class EndpointValue {
    String value
    def hostport
    def context
    def sim
    def user
    def simid
    def actor
    def trans

    EndpointValue(String value) {
        this.value = value
        def http
        def blank
        if (value.startsWith('http'))
            (http, blank, hostport, context, sim, user, simid, actor, trans) = value.split('/')
        else  // this is the requestURI portion of the endpoint
            (blank, context, sim, user, simid, actor, trans) = value.split('/')
    }
    String getValue() { return value }

    String requestURI() {  // similar to HttpRequest.getRequestURI()
        "/${context}/${sim}/${user}/${simid}/${actor}/${trans}"
    }

    SimIdentifier simIdentifier() { new SimIdentifier(user, simid) }
    SimId simId() { new SimId(simid)}

    String toString() { value }
}
