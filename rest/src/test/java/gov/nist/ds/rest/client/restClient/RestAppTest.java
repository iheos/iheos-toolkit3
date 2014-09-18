package gov.nist.ds.rest.client.restClient;

import gov.nist.ds.rest.client.restService.RestServer;
import gov.nist.ds.rest.client.restService.StringEntity;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

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
        RestServer server = new RestServer();
        String str = server.getStr();

        // Get String from client side
        String str2 = RestApp.getString();

        assertEquals(str, str2);
    }



    /**
     *
     * Method: getListString()
     *
     */
    @Test
    public void testGetListString() throws Exception {
        // String on server side
        RestServer server = new RestServer();
        List<StringEntity> list1 = server.getStringEntity();

        // Get String from client side
        List<StringEntity> list2 = RestApp.getListString();

        assertEquals(list1.get(0).getString(), list2.get(0).getString());
    }


} 
