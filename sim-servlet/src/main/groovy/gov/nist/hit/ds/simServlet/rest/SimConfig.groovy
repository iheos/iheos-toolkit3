package gov.nist.hit.ds.simServlet.rest

import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simSupport.client.SimId

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

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
}
