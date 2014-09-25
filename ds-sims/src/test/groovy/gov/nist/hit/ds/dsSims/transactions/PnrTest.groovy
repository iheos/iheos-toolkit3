package gov.nist.hit.ds.dsSims.transactions
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimSystemConfig
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
    <transaction displayName="Provide and Register" id="prb" code="prb">
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="gov.nist.hit.ds.dsSims.transactions.Pnr"/>
    </transaction>
    <actor displayName="Document Recipient" id="docrec">
        <simFactoryClass class="gov.nist.hit.ds.simSupport.factories.DocumentRecipientActorFactory"/>
        <transaction id="prb"/>
    </actor>
</ActorsTransactions>
'''
    def factory = new ActorTransactionTypeFactory()
    def simId = new SimId('1234')
    def actorType = 'docrec'
    def transactionName = 'prb'
    def repoName = 'Sim'
    def endpoint = new Endpoint('http://localhost:8080/xdstools3/sim/1234/docrec/prb')
    def header = 'x'
    def body = 'y'.getBytes()
    File repoDataDir
    RepositorySource repoSource
    SimHandle simHandle

    void setup() {
        SimSupport.initialize()
        factory.clear()
        factory.loadFromString(config)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simHandle = SimUtils.create(actorType, simId, new SimSystemConfig())
    }

    // TODO: How to pass in options/selections???

//    def cleanup() {
//        new SimUtils().delete(simId, repoName)
//    }

    def 'Test'() {
        setup:
        def header = 'x'
        def body = 'y'.getBytes()
        def transactionType = factory.getTransactionType(transactionName)
        when:
        def runner = new TransactionRunner(simId, repoName, transactionType, header, body)
        runner.run()

        then:
        true
    }

    def 'Test2'() {
        when:
        SimUtils.runTransaction(endpoint, header, body, repoName)

        then:
        true
    }

}
