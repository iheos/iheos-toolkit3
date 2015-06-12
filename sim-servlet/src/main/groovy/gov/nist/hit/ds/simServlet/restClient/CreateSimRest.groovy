package gov.nist.hit.ds.simServlet.restClient

import gov.nist.hit.ds.utilities.xml.OMFormatter
import groovy.util.logging.Log4j
import groovy.xml.StreamingMarkupBuilder
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import static groovyx.net.http.ContentType.XML

/**
 * Created by bmajur on 2/4/15.
 */
@Log4j
class CreateSimRest {

    static String run(String config, String host, String port, String service, String username, String simid) {
        def returnvalue = null
        def restUri = "http://${host}:${port}/${service}/rest/sim/create/${username}/${simid}"

        def http = new HTTPBuilder(restUri)
        log.debug("CreateSimRest: ${http.getUri()}")
        http.request(Method.POST, XML) { request ->
            requestContentType = XML
            body = config.trim()

            response.success = { resp, xml ->
                log.debug 'Sim Created'
                returnvalue = prettyPrint(xml)
            }
            response.failure = { resp ->
                log.debug 'Failure'
                log.debug resp.statusLine
                resp.getHeaders().each { log.debug it }
            }
            response.'404' = {
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

