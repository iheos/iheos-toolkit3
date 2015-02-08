package gov.nist.hit.ds.simServlet.scripts

import gov.nist.hit.ds.utilities.xml.OMFormatter
import groovy.xml.StreamingMarkupBuilder
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import static groovyx.net.http.ContentType.*

/**
 * Created by bmajur on 11/22/14.
 */

class Create {
    static main(String[] args) {
        def config = '''
<actor type='docrec'>
  <transaction name='prb'>
    <endpoint value='http://localhost:9080/xdstools3/sim/PnrSoapTest/docrec/prb' readOnly='true' />
    <settings>
      <boolean name='schemaCheck' value='true' />
      <boolean name='modelCheck' value='false' />
      <boolean name='codingCheck' value='false' />
      <boolean name='soapCheck' value='true' />
      <text name='msgCallback' value='' />
      <webService value='prb' readOnly='true' />
    </settings>
  </transaction>
</actor>
'''
        def http = new HTTPBuilder('http://localhost:9080/xdstools3/rest/sim/create/bill/1')
        http.request(Method.POST, XML) { request ->
            requestContentType = XML
            body = config

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
                println 'Not found'
            }
            response.failure = { resp ->
                print resp
            }
        }

        http = new HTTPBuilder('http://localhost:9080')

        http.get( path : '/xdstools3/rest/sim/config/bill/1',
                contentType : XML ) { resp, xml ->

            println "response status: ${resp.statusLine}"
            println 'Headers: -----------'
            resp.headers.each { h ->
                println " ${h.name} : ${h.value}"
            }
            println ''
            println prettyPrint(xml)
//            def smb = new StreamingMarkupBuilder();
//
//            String o = smb.bind {
//                xml1 -> xml1.mkp.yield xml
//            }
//            println new OMFormatter(o).toString()
        }
    }

    static prettyPrint(xml) {
        def smb = new StreamingMarkupBuilder();

        String o = smb.bind {
            xml1 -> xml1.mkp.yield xml
        }
        new OMFormatter(o).toString()
    }
}
