package gov.nist.hit.ds.simServlet.restClient

import gov.nist.hit.ds.utilities.xml.OMFormatter
import groovy.util.logging.Log4j
import groovy.xml.StreamingMarkupBuilder
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.log4j.Logger

import static groovyx.net.http.ContentType.XML

/**
 * Created by bill on 6/3/15.
 */

class RestSend {
    private static Logger log = Logger.getLogger(RestSend);

    static String run(uri, message) {
        def returnvalue = null
        def http = new HTTPBuilder(uri)
        http.request(Method.POST, XML) { request ->
            requestContentType = XML
            body = message

            response.success = { resp, xml ->
                log.debug 'REST request sent'
                returnvalue = prettyPrint(xml)
            }
            response.failure = { resp ->
                log.debug 'REST request failed'
                log.debug resp.statusLine
                resp.getHeaders().each { log.debug it }
            }
            response.'404' = {
                log.debug "404 - Service does not exist"
                returnvalue = null
            }
        }
        return returnvalue
    }

    static prettyPrint(xml) {
        def smb = new StreamingMarkupBuilder();

        String o = smb.bind {
            xml1 -> xml1.mkp.yield xml
        }
        new OMFormatter(o).toString()
    }

}
