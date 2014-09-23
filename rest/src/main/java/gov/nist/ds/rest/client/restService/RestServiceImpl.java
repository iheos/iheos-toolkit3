package gov.nist.ds.rest.client.restService;


import gov.nist.ds.rest.client.exceptionHandling.AppConstants;
import gov.nist.ds.rest.client.exceptionHandling.AppException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Jersey resource for a standard RESTful API.
 * <p>
 *
 * @see //http://docs.sun.com/app/docs/doc/820-4867/ggnxo?l=en&a=view
 * @see //http://blogs.sun.com/enterprisetechtips/entry/jersey_and_spring
 */

// the API path must indicate the functionality accessed, and the version of the API to avoid later confusion
@Path("/apistrings/v1")
public class RestServiceImpl implements RestService {
    StringEntity stringEntity;
    List<StringEntity> list;
    String str;
    String emptyStr;

    public RestServiceImpl(){
        str = "This is the test string.";
        emptyStr = "";
        createListOfstring();
    }



    /**
     * Reads a list of Strings.
     * @see StringEntity
     * @return A list of StringEntities build on the REST server.
     */
    @Produces( { MediaType.APPLICATION_XML })
    @Consumes( { MediaType.TEXT_XML })
    @GET
    @Path("/list/read")
    public List<StringEntity> getListOfStrings() throws AppException {

        if(list.isEmpty()){
            throw new AppException(Response.Status.NO_CONTENT.getStatusCode(), "The entity you are trying to access " +
                    "is empty.", AppConstants.SERVICE_URL);
        }

        return list;
    }



    /**
     * Reads a String located on the server.
     * @return a test string
     */
    @Produces( { MediaType.APPLICATION_XML })
    @Consumes( { MediaType.TEXT_XML })
    @GET
    @Path("/string/read")
    public String getString() throws AppException {

        if(str.equals("")){
            throw new AppException(Response.Status.NO_CONTENT.getStatusCode(), "The entity you are trying to access " +
                    "is empty.", AppConstants.SERVICE_URL);
        }

        return str;
    }



    /**
     * Reads an empty String to check Exception mechanism.
     * @return a test string
     */
    @Produces( { MediaType.APPLICATION_XML })
    @Consumes( { MediaType.TEXT_XML })
    @GET
    @Path("/string/readempty")
    public String getEmptyString() throws AppException {

        if(emptyStr.equals("")){
            throw new AppException(Response.Status.NO_CONTENT.getStatusCode(), "The entity you are trying to access " +
                    "is empty.", AppConstants.SERVICE_URL);
        }

        return emptyStr;
    }




/*

    @Produces( { MediaType.APPLICATION_XML })
    @Consumes( { MediaType.TEXT_XML })
    @POST
    @Path("/add")
    public StringResponse create(StringRequest request) {
        StringResponse response = new StringResponse();

        if (request.getOperationType() != OperationType.ADD || request.getMessages().size() != 1) {
            response.setStatus(RestResponse.STATUS_FAILURE);
        } else {
            try {
                // create List of Strings
                stringEntity = new StringEntity();
                response.setStatus(RestResponse.STATUS_SUCCESS);
            } catch (Exception e) {
                response.setStatus(RestResponse.STATUS_FAILURE);
                e.printStackTrace();
            }
        }

        return response;
    }
*/




    // -------------- Utilities ------------------

    private void createListOfstring() {
        list = new ArrayList<>();
        StringEntity entity1 = new StringEntity();
        StringEntity entity2 = new StringEntity();
        list.add(entity1);list.add(entity2);
    }



    // -------------- Getters and setters ------------------

    public String getStr(){
        return str;
    }

    public String getEmptyStr(){
        return emptyStr;
    }


    public List<StringEntity> getStringEntity(){
        return list;
    }


}
