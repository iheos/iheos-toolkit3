package gov.nist.hit.ds.simSupport.endpoint

import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimIdentifier
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException

/**
 * Created by bmajur on 6/6/14.
 */
class EndpointValue {
    String value
    private boolean unParseable = false
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
        try {
            if (value.startsWith('http'))
                (http, blank, hostport, context, sim, user, simid, actor, trans) = value.split('/')
            else  // this is the requestURI portion of the endpoint
                (blank, context, sim, user, simid, actor, trans) = value.split('/')
        } catch (Exception e) {
            unParseable = true
        }
    }
    String getValue() { return value }

    String requestURI() {  // similar to HttpRequest.getRequestURI()
        if (unParseable) throw new ToolkitRuntimeException("EndpointValue#requestURI: Cannot act on un-parse-able endpoint")
        "/${context}/${sim}/${user}/${simid}/${actor}/${trans}"
    }

    SimIdentifier simIdentifier() {
        if (unParseable) throw new ToolkitRuntimeException("EndpointValue#simIdentifier: Cannot act on un-parse-able endpoint")
        new SimIdentifier(user, simid)
    }
    SimId simId() {
        if (unParseable) throw new ToolkitRuntimeException("EndpointValue#simId: Cannot act on un-parse-able endpoint")
        new SimId(simid)
    }

    String toString() { value }
}
