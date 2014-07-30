package gov.nist.hit.ds.simSupport.endpoint

import gov.nist.hit.ds.actorTransaction.EndpointLabel
import gov.nist.hit.ds.simSupport.client.SimId

/**
 * Created by bmajur on 6/6/14.
 */
class EndpointBuilder {
    String server
    String port
    String base
    SimId simId
    String actorCode
    String transCode

    EndpointBuilder() {}

    EndpointBuilder(String server, String port, String base, SimId simId) {
        this.server = server
        this.port = port
        this.base = base
        this.simId = simId
    }

    Endpoint makeEndpoint(String actor, EndpointLabel endpointLabel) {
        server = clean(server)
        port = clean(port)
        base = clean(base)
        actor = clean(actor)
        def secure = (endpointLabel.tls) ? 's' : ''
        String val;
        val = "http${secure}://${server}:${port}/${base}/${simId.getId()}/${actor}/${endpointLabel.transType.code}"
        return new Endpoint(val)
    }

    String clean(String val) {
        def x = val.trim()
        if (x.size() == 0) return x
        x = (x.charAt(0) == '/') ? clean(x.substring(1)) : x
        x = (x.charAt(x.size()-1) == '/') ? clean(x.substring(0, x.size()-1)) : x
        return x
    }

    def parse(String endpoint) {
        def (http, serverPort, basePart, simStr, simid, actor, trans) = endpoint.tokenize('/')
        simId = new SimId(simid)
        actorCode = actor
        transCode = trans
    }
}
