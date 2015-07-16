package gov.nist.hit.ds.simServlet.rest

import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simSupport.client.SimId

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by bmajur on 10/26/14.
 */

@Path('/sim/config/{username}/{simid}')
class SimConfig {

    @GET
    @Produces(MediaType.APPLICATION_XML)
    String getConfig(@PathParam('username') String username, @PathParam('simid') String simIdString) {
        try {
            println "GET for SIMConfig ${simIdString}"
            SimId simId = new SimId(simIdString)
            def ret = new SimApi().getConfig(username, simId)
            println "return is \n${ret}"
            return ret
        } catch (Throwable t) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST)
        }

    }

//    @POST
//    @Consumes(MediaType.APPLICATION_XML)
//    def updateConfig(@PathParam('simid') String simIdString, String message) {
//        SimId simId = new SimId(simIdString)
//        new SimApi().updateConfig(simId, message)
//    }

}
