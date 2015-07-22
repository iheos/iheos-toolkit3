package gov.nist.hit.ds.simSupport.endpoint

import gov.nist.hit.ds.actorTransaction.ActorType
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimIdentifier
import gov.nist.hit.ds.simSupport.simulator.SimSystemConfig
import gov.nist.hit.ds.soapSupport.core.Endpoint
import groovy.util.logging.Log4j
import org.apache.log4j.Logger

/**
 * Created by bmajur on 6/6/14.
 */

class EndpointBuilder {
    private static Logger log = Logger.getLogger(EndpointBuilder);
    String server
    String port
    String tlsPort
    String base
    String user
    SimId simId = null
    String actorCode
    String transCode


    EndpointBuilder() { log.debug("EndpointBuilder")}

    // TLS endpoints cannot be generated through this constructor
    // Useful for unit tests only
    EndpointBuilder(String server, String port, String base, String user, SimId simId) {
        this.server = server
        this.port = port
        this.tlsPort = tlsPort
        this.base = base
        this.user = user
        this.simId = simId
    }

    EndpointBuilder(SimSystemConfig config, String user, SimId simId) {
        server = config.host
        port = config.port
        tlsPort = config.tlsPort
        base = config.service
        this.user = user
        this.simId = simId
    }

    SimIdentifier getSimIdentifier() { new SimIdentifier(user, simId)}

    public String toString() { [server: server, port: port, base: base, user: user, simId: simId?.id, actorCode: actorCode, transCode: transCode].toString() }

    EndpointValue makeEndpoint(ActorType actor, EndpointType endpointLabel) {
        server = clean(server)
        port = clean(port)
        tlsPort = clean(tlsPort)
        base = clean(base)
        user = clean(user)
        def actorName = clean(actor.name)
        def secure = (endpointLabel.tls) ? 's' : ''
        def theport = (endpointLabel.tls) ? tlsPort : port
        String val;
        val = "http${secure}://${server}:${theport}/${base}/sim/${user}/${simId.getId()}/${actorName}/${endpointLabel.transType.code}"
        return new EndpointValue(val)
    }

    String clean(String val) {
        if (!val) return val
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
        def http
        def blank
        def serverPort
        def basePart
        def simStr
        def simid
        def actor
        def trans
        try {
            (http, blank, serverPort, basePart, simStr, user, simid, actor, trans) = endpoint.split('/')
        } catch (Exception e) {
            throw new Exception("cannot parse endpoint ${endpoint}", e)
        }
        if (simid)
            simId = new SimId(simid)
        actorCode = actor
        transCode = trans
        log.debug("endpoint parser : ${toString()}")
        return this
    }
}
