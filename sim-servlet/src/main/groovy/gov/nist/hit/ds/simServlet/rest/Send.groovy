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
 * Created by bmajur on 2/3/15.
 */
@Path('/sim/client/{username}/{simid}')
class   Send {
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
            SimApi.create(actorTypeName, username, simId)
            return SimApi.updateConfig(username, simId, message)
//        return '<foo/>'
        } catch (Throwable t) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST)
        }
    }
}
