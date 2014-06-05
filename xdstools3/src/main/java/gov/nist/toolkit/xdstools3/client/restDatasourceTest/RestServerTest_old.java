package gov.nist.toolkit.xdstools3.client.restDatasourceTest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/jsonServices")
public class RestServerTest_old {

    @POST
    @Path("/loadItems}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response consumeJSON() {
         Record test1 = new Record("USA", "US");
       String output = test1.toString();
        System.out.println(output);
        return Response.status(200).entity(output).build();
    }


    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response consumeJSON( Record record ) {

        String output = record.toString();

        return Response.status(200).entity(output).build();
    }

}
