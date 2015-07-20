package gov.nist.hit.ds.simServlet.rest

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.ActorType
import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import groovy.util.logging.Log4j
import org.apache.log4j.Logger
import org.xml.sax.SAXParseException

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by bmajur on 11/19/14.
 */


@Path('/sim/create/{username}/{simid}')
class   CreateSim {
    private static Logger log = Logger.getLogger(CreateSim);
    @POST
    @Consumes(MediaType.APPLICATION_XML)
//    @Produces(MediaType.APPLICATION_XML)
    String create(@PathParam('username') String username, @PathParam('simid') String simIdString, String message) {
        try {
            def xml
            assert username
            assert simIdString
            println "Message is ${message}"
            try {
                xml = new XmlSlurper().parseText(message)
            } catch (SAXParseException spe) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST)
            }
            println "xml parsed"
            def actorTypeName = xml.@type.text()
            println "actor type is ${actorTypeName}"
            if (!actorTypeName)
                throw new WebApplicationException(Response.Status.BAD_REQUEST)
            def simId = new SimId(simIdString)
            ActorType actorType = new ActorTransactionTypeFactory().getActorType(actorTypeName)

            SimHandle simHandle
            if (actorType?.isClient())
                simHandle = SimApi.createClient(actorTypeName, username, simId, message)  // endpoints updated
            else
                simHandle = SimApi.createServer(actorTypeName, username, simId, message)   // endpoints not updated
            return new String(simHandle.configAsset.content)
        } catch (Throwable t) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST)
        }
    }
}
