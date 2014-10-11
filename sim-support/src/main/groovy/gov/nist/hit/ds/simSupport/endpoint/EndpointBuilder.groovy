package gov.nist.hit.ds.simSupport.endpoint
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.soapSupport.core.Endpoint
import groovy.util.logging.Log4j
/**
 * Created by bmajur on 6/6/14.
 */
@Log4j
class EndpointBuilder {
    String server
    String port
    String base
    SimId simId = null
    String actorCode
    String transCode

    EndpointBuilder() { log.debug("EndpointBuilder")}

    EndpointBuilder(String server, String port, String base, SimId simId) {
        this.server = server
        this.port = port
        this.base = base
        this.simId = simId
    }

    public String toString() { [server: server, port: port, base: base, simId: simId?.id, actorCode: actorCode, transCode: transCode].toString() }

    EndpointValue makeEndpoint(String actor, EndpointType endpointLabel) {
        server = clean(server)
        port = clean(port)
        base = clean(base)
        actor = clean(actor)
        def secure = (endpointLabel.tls) ? 's' : ''
        String val;
        val = "http${secure}://${server}:${port}/${base}/${simId.getId()}/${actor}/${endpointLabel.transType.code}"
        return new EndpointValue(val)
    }

    String clean(String val) {
        def x = val.trim()
        if (x.size() == 0) return x
        x = (x.charAt(0) == '/') ? clean(x.substring(1)) : x
        x = (x.charAt(x.size()-1) == '/') ? clean(x.substring(0, x.size()-1)) : x
        return x
    }

    EndpointBuilder parse(Endpoint endpoint) {
        parse(endpoint.endpoint)
        return this
    }

    EndpointBuilder parse(String endpoint) {
        // http, serverport are actually wrong
        def (http, serverPort, basePart, simStr, simid, actor, trans) = endpoint.tokenize('/')
        log.debug("endpoint parser : ${toString()}")
        if (simid)
            simId = new SimId(simid)
        actorCode = actor
        transCode = trans
        return this
    }
}
