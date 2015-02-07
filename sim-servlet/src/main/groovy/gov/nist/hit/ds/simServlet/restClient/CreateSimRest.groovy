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

    static String run(String config, host, port, service, username, simid) {
        def returnvalue

        def http = new HTTPBuilder("http://${host}:${port}/${service}/rest/sim/create/${username}/${simid}")
        log.debug("CreateSimRest: ${http.getUri()}")
        http.request(Method.POST, XML) { request ->
            requestContentType = XML
            body = config.trim()

            response.success = { resp, xml ->
                println 'Sim Created'
                println prettyPrint(xml)
            }
            response.failure = { resp ->
                println 'Failure'
                println resp.statusLine
                resp.getHeaders().each { println it }
            }
            response.'404' = {
                returnvalue = 'Create Sim failed: Service Not found'
            }
        }

        http = new HTTPBuilder("http://${host}:${port}")

        http.get( path : "/${service}/rest/sim/config/${username}/${simid}",
                contentType : XML ) { resp, xml ->

            println "response status: ${resp.statusLine}"
            println 'Headers: -----------'
            resp.headers.each { h ->
                println " ${h.name} : ${h.value}"
            }
            returnvalue = prettyPrint(xml)

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

