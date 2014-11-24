package gov.nist.hit.ds.simServlet.servlet

import gov.nist.hit.ds.utilities.io.Io

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by bmajur on 11/22/14.
 */
class LogServlet extends HttpServlet {

    void doPost(HttpServletRequest request, HttpServletResponse response) {
        byte[] body;
        try {
            body = Io.getBytesFromInputStream(request.getInputStream());
        } catch (Exception e) {
            logger.error("Cannot read input message body: " + e.getMessage());
            return;
        }
        println new String(body)
    }
}