package gov.nist.hit.ds.simServlet.rest

import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simSupport.client.SimId
import org.xml.sax.SAXParseException

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by bmajur on 11/19/14.
 */

@Path('/sim/create/{simid}')
class CreateSim {
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    def create(@PathParam('simid') String simIdString, String message) {
        def xml
        println "simid is ${simIdString.trim()}"
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
        SimApi.create(actorTypeName, simId)
        SimApi.updateConfig(simId, message)
    }
}
