package gov.nist.hit.ds.simServlet.rest
import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simSupport.client.SimId

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.MediaType
/**
 * Created by bmajur on 11/22/14.
 */
@Path('/sim/delete/{simid}')
class DeleteSim {
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    def delete(@PathParam('simid') String simIdString) {
        def xml
        println "Deleting sim ${simIdString.trim()}"
        def simId = new SimId(simIdString)
        SimApi.delete(simId)
    }
}
