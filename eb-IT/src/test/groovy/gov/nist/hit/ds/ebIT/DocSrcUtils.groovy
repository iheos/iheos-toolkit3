package gov.nist.hit.ds.ebIT

import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimHandle

/**
 * Created by bill on 6/3/15.
 */
class DocSrcUtils {

    static createClientSimConfig(noTlsEndpoint, tlsEndpoint) {
        """
<actor type='docsrc'>
    <environment name="NA2015"/>
    <transaction name='prb'>
        <!-- example endpoint value -->
        <endpoint value='${noTlsEndpoint}' />
        <settings>
            <!-- Checks applied to response only -->
            <boolean name='schemaCheck' value='false' />
            <boolean name='modelCheck' value='false' />
            <boolean name='codingCheck' value='false' />
            <boolean name='soapCheck' value='false' />
            <!-- on response -->
            <text name='msgCallback' value='' />
        </settings>
        <webService value='prb' />
    </transaction>
    <transaction name='prb'>
        <endpoint value='${tlsEndpoint}' />
        <settings>
            <boolean name='schemaCheck' value='false' />
            <boolean name='modelCheck' value='false' />
            <boolean name='codingCheck' value='false' />
            <boolean name='soapCheck' value='false' />
            <text name='msgCallback' value='' />
        </settings>
        <webService value='prb_TLS' />
    </transaction>
</actor>"""
    }

    static createServerSimConfig(noTlsEndpoint) {
        """
<actor type='docrec'>
    <environment name="NA2015"/>
    <transaction name='prb'>
        <!-- example endpoint value -->
        <endpoint value='${noTlsEndpoint}' />
        <settings>
            <!-- Checks applied to response only -->
            <boolean name='schemaCheck' value='false' />
            <boolean name='modelCheck' value='false' />
            <boolean name='codingCheck' value='false' />
            <boolean name='soapCheck' value='false' />
            <!-- on response -->
            <text name='msgCallback' value='http://localhost:8080/resttest/1' />
        </settings>
        <webService value='prb' />
    </transaction>
    <transaction name='prb'>
        <endpoint value='https://localhost:8080/xdstools3/sim/unknown/target23/docrec/prb' />
        <settings>
            <boolean name='schemaCheck' value='false' />
            <boolean name='modelCheck' value='false' />
            <boolean name='codingCheck' value='false' />
            <boolean name='soapCheck' value='false' />
            <text name='msgCallback' value='' />
        </settings>
        <webService value='prb_TLS' />
    </transaction>
</actor>"""
    }

    static createClientRequest(clientUserName, clientSimName, metadata, tls) {
        """
<sendRequest>
    <simReference>${clientUserName}/${clientSimName}</simReference>
    <transactionName>prb</transactionName>
    <tls value="${(tls) ? 'true' : 'false'}"/>
    <metadata>${metadata}</metadata>
    <extraHeaders><foo/><bar/></extraHeaders>
    <document id="Document01" mimeType="text/plain">doc content</document>
</sendRequest>
"""
    }

    static SimHandle createDocSrcSim(userName, SimId clientSimId, serverEndpoint, serverTlsEndpoint) {
        SimApi.createClient('docsrc', userName, clientSimId, clientSimConfig(serverEndpoint, serverTlsEndpoint))
    }
}
