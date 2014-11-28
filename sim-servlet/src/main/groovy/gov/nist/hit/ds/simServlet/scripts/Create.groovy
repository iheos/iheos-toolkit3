package gov.nist.hit.ds.simServlet.scripts

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
    <endpoint value='http://localhost:8080/xdstools3/sim/PnrSoapTest/docrec/prb' readOnly='true' />
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
        def http = new HTTPBuilder('http://localhost:9080/xdstools3/rest/sim/create/1')
        http.request(Method.POST, XML) { request ->
            requestContentType = XML
            body = config

            response.success = { resp ->
                println resp.getData()
            }
        }
        http = new HTTPBuilder('http://localhost:9080')
        http.get( path: '/xdstools3/rest/sim/config/1', contentType : XML) { resp, reader ->
            println "response status: ${resp.statusLine}"
            println 'Response data: -----'
            System.out << reader
            println '\n--------------------'
        }
    }
}