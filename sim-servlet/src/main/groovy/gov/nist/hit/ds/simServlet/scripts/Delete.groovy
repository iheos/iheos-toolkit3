package gov.nist.hit.ds.simServlet.scripts
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import static groovyx.net.http.ContentType.XML
/**
 * Created by bmajur on 11/22/14.
 */
class Delete {
    static main(String[] args) {
        println "Delete"
        def http = new HTTPBuilder('http://localhost:9080/xdstools3/rest/sim/delete/1')
        http.request(Method.POST, XML) {
            requestContentType = XML
            body = ''
        }
    }
}
