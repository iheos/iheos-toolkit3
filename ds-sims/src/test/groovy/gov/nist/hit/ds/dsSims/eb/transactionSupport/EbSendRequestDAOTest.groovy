package gov.nist.hit.ds.dsSims.eb.transactionSupport

import gov.nist.hit.ds.simSupport.utilities.SimSupport
import spock.lang.Specification

/**
 * Created by bmajur on 2/3/15.
 */
class EbSendRequestDAOTest extends Specification {
    def requestXml = '''
<sendRequest>
    <simReference>user/simid</simReference>
    <transactionName>prb</transactionName>
    <tls value="true"/>
    <metadata><foo/></metadata>
    <document id="Document01" mimeType="text/plain">doc content</document>
</sendRequest>'''

    def setup() {
        SimSupport.initialize()
    }

    def 'Test toModel'() {
        when:
        EbSendRequest request = EbSendRequestDAO.toModel(requestXml)

        then:
        request.simIdentifier.repoName == 'user'
        request.simIdentifier.simId.id == 'simid'
        request.metadata.startsWith('<foo')
        request.transactionName == 'prb'
        request.tls
        request.documents['Document01'].content == 'doc content'
    }

    def requestXml2 = '''
<sendRequest>
    <simReference>user/simid</simReference>
    <transactionName>prb</transactionName>
    <tls value="false"/>
    <metadata><foo/></metadata>
    <document id="Document01" mimeType="text/plain">doc content</document>
</sendRequest>'''

    def 'Test toModel no TLS'() {
        when:
        EbSendRequest request = EbSendRequestDAO.toModel(requestXml2)

        then:
        request.simIdentifier.repoName == 'user'
        request.simIdentifier.simId.id == 'simid'
        request.metadata.startsWith('<foo')
        request.transactionName == 'prb'
        !request.tls
        request.documents['Document01'].content == 'doc content'
    }
}
