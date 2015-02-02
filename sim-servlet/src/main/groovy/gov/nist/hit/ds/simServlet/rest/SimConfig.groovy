package gov.nist.hit.ds.simServlet.rest

import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simSupport.client.SimId

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * Created by bmajur on 10/26/14.
 */

@Path('/sim/config/{simid}')
class SimConfig {

    @GET
    @Produces(MediaType.APPLICATION_XML)
    String getConfig(@PathParam('simid') String simIdString) {
        println "GET for SIMConfig ${simIdString}"
        SimId simId = new SimId(simIdString)
        def ret = new SimApi().getConfig(simId)
        println "return is \n${ret}"
        return ret
    }

//    @POST
//    @Consumes(MediaType.APPLICATION_XML)
//    def updateConfig(@PathParam('simid') String simIdString, String message) {
//        SimId simId = new SimId(simIdString)
//        new SimApi().updateConfig(simId, message)
//    }

}
