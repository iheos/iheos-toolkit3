package gov.nist.ds.rest.client.restClient;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import gov.nist.ds.rest.client.restService.StringEntity;

import java.util.List;

/**
 * Created by dazais on 9/11/2014.
 */
public class RestApp {

    public static void main(String[] args) {

        // Example: Retrieve a String
        if (!getString().isEmpty()) {
            String str = getString();
            System.out.println("Retrieving String: " + str);
        };


        // Example: Retrieve a List of Strings
        if (!getListString().isEmpty()) {
            List<StringEntity> list = getListString();
            System.out.println("Retrieving List of Strings: " + list.get(0).getString());
        };
    }

    /**
     * Retrieves a String through the REST API. Uses Jersey's ClientResponse class in order to handle the full response
     * and gain access to the error codes returned by the server.
     */
    public static String getString() {

        try {
            // Send out the request
            ClientResponse response = Client.create().resource("http://localhost:8080/rest/rest/apistrings/v1/string/read").get(ClientResponse.class);

            // Check server response
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
                // TODO response error code needs to be handled better
            }

            // Retrieve the entity passed over REST
            return response.getEntity(String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }



    /**
     * Retrieves an empty String through the REST API to check error handling mechanism.
     */
    public static String getEmptyString() {

        try {
            // Send out the request
            ClientResponse response = Client.create().resource("http://localhost:8080/rest/rest/apistrings/v1/string/readempty").get(ClientResponse.class);

            // Check server response
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
                // TODO response error code needs to be handled better
            }

            // Retrieve the entity passed over REST
            return response.getEntity(String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }



    /**
     * Retrieves a List of String objects called "StringEntity" from the REST server. This is a more complex call
     * (and a recommended one). A full response is built on the server, with server response codes, the entity to
     * pass over REST, possible additional headers, etc.
     *
     * @see javax.xml.ws.Response;
     * @see StringEntity
     */
    public static List<StringEntity> getListString() {

        try {
            // Send out the request
            ClientResponse response = Client.create().resource("http://localhost:8080/rest/rest/apistrings/v1/list/read").get(ClientResponse.class);

            // Check server response
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            // Retrieve the entity passed over REST
            List<StringEntity> result = response.getEntity(new GenericType<List<StringEntity>>(){});

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
