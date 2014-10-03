package gov.nist.ds.rest.client.restService;

import gov.nist.ds.rest.client.exceptionHandling.AppException;

import java.util.List;

/**
 * Provides the interface for the REST Service example "apistrings" version 1.
 *
 */
public interface RestService {



    /**
     * Reads a list of Strings.
     * @see StringEntity
     * @return A list of StringEntities build on the REST server.
     */
    public List<StringEntity> getListOfStrings() throws AppException;

    /**
     * Reads a String located on the server.
     * @return a test string
     */
    public String getString() throws AppException;



}
