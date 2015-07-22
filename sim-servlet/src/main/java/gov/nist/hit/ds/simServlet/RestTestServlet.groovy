package gov.nist.hit.ds.simServlet

import gov.nist.hit.ds.utilities.io.Io
import groovy.util.logging.Log4j
import org.apache.log4j.Logger

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by bmajur on 11/6/14.
 */

class RestTestServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(RestTestServlet);

    void doPost(HttpServletRequest request, HttpServletResponse response) {
        byte[] body;
        try {
            body = Io.getBytesFromInputStream(request.getInputStream());
        } catch (Exception e) {
            logger.error("Cannot read input message body: " + e.getMessage());
            return;
        }

        println "\n\nRESTTEST: ${body}\n"
    }
}
