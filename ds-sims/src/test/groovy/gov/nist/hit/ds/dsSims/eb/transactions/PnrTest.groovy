package gov.nist.hit.ds.dsSims.eb.transactions
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.dsSims.eb.topLevel.ValidatorManager
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.soapSupport.core.Endpoint
import gov.nist.hit.ds.tkapis.validation.ValidateMessageResponse
import spock.lang.Specification
/**
 * Created by bmajur on 9/24/14.
 */
class PnrTest extends Specification {
    static String config = '''
<ActorsTransactions>
    <transaction name="Provide and Register" id="prb" code="prb">
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="gov.nist.hit.ds.dsSims.eb.transactions.Pnr"/>
        <params multipart="true" soap="true"/>
    </transaction>
    <actor name="Document Recipient" id="docrec">
        <implClass value="gov.nist.hit.ds.simSupport.factories.DocumentRecipientActorFactory"/>
        <transaction id="prb"/>
    </actor>
</ActorsTransactions>
'''
    def simId
    def repoName = 'Sim'
    def endpoint = new Endpoint('http://localhost:8080/xdstools3/sim/1234/docrec/prb')
    File repoDataDir
    RepositorySource repoSource
    SimHandle simHandle
    def factory = new ActorTransactionTypeFactory()

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(config)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimId('PnrTest')
        SimUtils.recreate('docrec', simId, repoName)
    }

    ValidatorManager vMan
    ValidateMessageResponse response

    def run(String header, String body) {
        vMan = new ValidatorManager()
        response = vMan.validateMessage(ValidatorManager.soapAction, header, body.getBytes())
        simHandle = vMan.simHandle
    }


    def 'Test good message'() {
        setup:
        def header = getClass().classLoader.getResource('pnr/good/PnRSoapHeader.txt').text
        def body = getClass().classLoader.getResource('pnr/good/PnR1DocSoapBody.txt').text

        when:
        run(header,body)

        then:
        response.validationStatus == ValidationStatus.OK
    }

    def 'Test wrong boundary'() {
        setup:
        def header = getClass().classLoader.getResource('pnr/wrongboundary/PnRSoapHeader.txt').text
        def body = getClass().classLoader.getResource('pnr/wrongboundary/PnR1DocSoapBody.txt').text

        when:
        run(header,body)

        then:
        response.validationStatus != ValidationStatus.OK
    }

}
