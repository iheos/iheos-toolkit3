package gov.nist.ds.rest.client.restClient;

import gov.nist.ds.rest.client.restService.RestServiceImpl;
import gov.nist.ds.rest.client.restService.StringEntity;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * RestApp Tester.
 *
 * @author <Authors name>
 * @since <pre>Sep 18, 2014</pre>
 * @version 1.0
 */
public class RestAppTest {


    /**
     *
     * Method: getString()
     *
     */
    @Test
    public void testGetString() throws Exception {
        // String on server side
        RestServiceImpl server = new RestServiceImpl();
        String str = server.getStr();

        // Get String from client side
        String str2 = RestApp.getString();

        assertEquals(str, str2);
    }



    /**
     *
     * Method: getString()
     * Tests Exception handling through class AppException when entity is null on server.
     *
     * @see gov.nist.ds.rest.client.exceptionHandling.AppException
     */
    @Test
    public void testGetNullString() throws Exception {
        // String on server side
        RestServiceImpl server = new RestServiceImpl();

        // Get String from client side
        String emptstr2 = RestApp.getEmptyString();

        assertNull(emptstr2);
    }


    /**
     *
     * Method: getListString()
     *
     */
    @Test
    public void testGetListString() throws Exception {
        // String on server side
        RestServiceImpl server = new RestServiceImpl();
        List<StringEntity> list1 = server.getStringEntity();

        // Get String from client side
        List<StringEntity> list2 = RestApp.getListString();

        assertEquals(list1.get(0).getString(), list2.get(0).getString());
    }


} 
