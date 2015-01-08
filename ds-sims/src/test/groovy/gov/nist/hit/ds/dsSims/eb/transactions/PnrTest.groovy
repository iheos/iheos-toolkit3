package gov.nist.hit.ds.dsSims.eb.transactions
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.topLevel.ValidatorManager
import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataParser
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.soapSupport.core.Endpoint
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
        <implClass value="Pnr"/>
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

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimId('PnrTest')
        SimUtils.recreate('docrec', simId, repoName)
    }

    def run(String header, String body) {
        Metadata metadata = MetadataParser.parseNonSubmission(submission)
        ValidationContext vc = new ValidationContext()
        vc.isPnR = true
        vc.isRequest = true
        def validationInterface = null

        Closure closure = { SimHandle simHandle ->
            simHandle.event.addArtifact('Metadata', submission)
            new ValidatorManager().validateMessage(ValidatorManager.soapAction, header, body.getBytes())
        }
        transRunner = new TransactionRunner('rb', simId, repoName, closure)
        transRunner.runTest()

        println "Failed assertions are ${transRunner.simHandle.event.errorAssertionIds()}"
    }


    def 'Test'() {
        setup:
        def header = getClass().classLoader.getResource('pnr/PnRSoapHeader.txt').text
        def body = getClass().classLoader.getResource('pnr/PnR1DocSoapBody.xml').text.getBytes()
        def transactionType = factory.getTransactionType('prb')
        simHandle.event.inOut.reqHdr = header
        simHandle.event.inOut.reqBody = body
        when:
        def runner = new TransactionRunner(simId, repoName, transactionType)
        runner.acceptRequest()

        then:
        true
    }

}
