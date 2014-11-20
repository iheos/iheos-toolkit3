package gov.nist.hit.ds.simServlet.rest

import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simSupport.client.SimId
import org.xml.sax.SAXParseException

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by bmajur on 10/26/14.
 */

@Path('/simconfig/{simid}')
class SimConfig {

    @GET
    @Produces(MediaType.APPLICATION_XML)
    String getConfig(@PathParam('simid') String simIdString) {
        println "GET for SIMConfig ${simIdString}"
        assert false
        SimId simId = new SimId(simIdString)
        return new SimApi().getConfig(simId)
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    def updateConfig(@PathParam('simid') String simIdString, String message) {
        SimId simId = new SimId(simIdString)
        new SimApi().updateConfig(simId, message)
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    def create(@PathParam('simid') String simIdString, String message) {
        def xml
        try {
            xml = new XmlSlurper().parse(message)
        } catch (SAXParseException spe) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST)
        }
        def actorTypeName = xml.@type.text()
        if (!actorTypeName)
            throw new WebApplicationException(Response.Status.BAD_REQUEST)
        def simId = new SimId(simIdString)
        SimApi.create(actorTypeName, simId)
        SimApi.updateConfig(simId, message)
    }
}
