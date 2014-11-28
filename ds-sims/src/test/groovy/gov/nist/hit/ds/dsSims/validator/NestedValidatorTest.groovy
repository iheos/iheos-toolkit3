package gov.nist.hit.ds.dsSims.validator
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.testSupport.EventAccess
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification
/**
 * Created by bmajur on 7/30/14.
 */
class NestedValidatorTest extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction name="Register" id="rb" code="rb" asyncCode="r.as">
       <implClass value="gov.nist.hit.ds.dsSims.transactions.RegisterTransaction"/>
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <params multiPart="false" soap="true"/>
    </transaction>
    <actor name="Document Registry" id="reg">
      <impllass value=""/>
        <transaction id="rb"/>
    </actor>
</ActorsTransactions>
'''

    File repoDataDir
    RepositorySource repoSource
    SimId simId
    String repoName = 'sim'

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimId('123')
        SimUtils.recreate('reg', simId, repoName)
    }

    def 'Sequential validators should validate'() {
        when:
        Closure closure = { simHandle ->
            new TestValidator1(simHandle.event).asPeer().run()
            new TestValidator2(simHandle.event).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, repoName,  closure)
        def eventAccess = new EventAccess(simId.id, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        !transRunner.simHandle.event.hasErrors()
        eventAccess.assertionGroupFile('TestValidator1').exists()
        eventAccess.assertionGroupFile('TestValidator2').exists()
    }

    def 'Validator with child should validate'() {
        when:
        Closure closure = { simHandle ->
            new TestValidator1WithSub(simHandle.event).asPeer().asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, repoName,  closure)
        def eventAccess = new EventAccess(simId.id, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        !transRunner.simHandle.event.hasErrors()
        eventAccess.assertionGroupFile('TestValidator1WithSub', []).exists()
        eventAccess.assertionGroupFile('TestValidatorSub1', []).exists()
    }

    def 'Error should propagate up tree'() {
        when:
        Closure closure = { simHandle ->
            new TestValidator1ErrorWithSub(simHandle.event).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, repoName, closure)
        def eventAccess = new EventAccess(simId.id, transRunner.simHandle.event)
        transRunner.runTest()

        then:  '''correct files are created'''
        transRunner.simHandle.event.hasErrors()
        eventAccess.assertionGroupFile('TestValidator1ErrorWithSub', ['TestValidator1ErrorWithSub']).exists()
        eventAccess.assertionGroupFile('TestValidatorErrorSub1', ['TestValidator1ErrorWithSub']).exists()
        eventAccess.propertiesFile('TestValidator1ErrorWithSub.parent', ['TestValidator1ErrorWithSub']).exists()
        eventAccess.propertiesFile('TestValidatorErrorSub1', ['TestValidator1ErrorWithSub']).exists()

        when: 'load child properties'
        Properties childProperties = new Properties()
        eventAccess.propertiesFile('TestValidatorErrorSub1', ['TestValidator1ErrorWithSub']).withReader {
            childProperties.load(it)
        }

        then: 'verify child is labeled with error'
        childProperties.getProperty('status') == 'ERROR'

        when: 'load parent properties'
        Properties parentProperties = new Properties()
        eventAccess.propertiesFile('TestValidator1ErrorWithSub.parent', ['TestValidator1ErrorWithSub']).withReader {
            parentProperties.load(it)
        }

        then: 'verify error has propogated up to parent'
        parentProperties.getProperty('status') == 'ERROR'
    }
}
