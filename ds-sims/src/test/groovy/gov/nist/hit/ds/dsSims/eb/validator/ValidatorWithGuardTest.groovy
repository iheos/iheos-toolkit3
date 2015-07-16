package gov.nist.hit.ds.dsSims.eb.validator
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.simulator.SimIdentifier
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification
/**
 * Created by bmajur on 7/30/14.
 */
class ValidatorWithGuardTest extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction name="Register" id="rb" code="rb" asyncCode="r.as">
       <implClass value="RegisterTransaction"/>
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <params multiPart="false" soap="true"/>
    </transaction>
    <actor name="Document Registry" id="reg">
      <implClass value=""/>
        <transaction id="rb"/>
    </actor>
</ActorsTransactions>
'''

    File repoDataDir
    RepositorySource repoSource
    SimIdentifier simId
    def repoName = 'Sim'

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimIdentifier(repoName, '123')
        SimUtils.create('reg', simId)
    }

    def 'Basic guard test'() {
        when:
        Closure closure = { simHandle ->
            new TestValidatorWithGuard(simHandle.event).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        def assertionGroup = transRunner.simHandle.event.getAssertionGroup('TestValidatorWithGuard')
        println transRunner.simHandle.event.allAssetionGroups
//        transRunner.simHandle.event.allAssetionGroups[0].assertions.each { println it }
//        transRunner.simHandle.event.allAssetionGroups[0].assertionIds.each { println it }

        then:
        !transRunner.simHandle.event.hasErrors()
        assertionGroup.hasAssertion('TV101')
        !assertionGroup.hasAssertion('TV102')
    }

    def 'Two guard test'() {
        when:
        Closure closure = { simHandle ->
            new TestValidatorWithGuard(simHandle.event).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        def assertionGroup = transRunner.simHandle.event.getAssertionGroup('TestValidatorWithGuard')
        println transRunner.simHandle.event.allAssetionGroups
//        transRunner.simHandle.event.allAssetionGroups[0].assertions.each { println it }
//        transRunner.simHandle.event.allAssetionGroups[0].assertionIds.each { println it }

        then:
        !transRunner.simHandle.event.hasErrors()
        assertionGroup.hasAssertion('TV101')
        !assertionGroup.hasAssertion('TV102')
        assertionGroup.hasAssertion('TV103')
        !assertionGroup.hasAssertion('TV104')
    }

    def 'Guard with late trigger test'() {
        when:
        Closure closure = { simHandle ->
            new TestValidatorWithGuard3(simHandle.event).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        def assertionGroup = transRunner.simHandle.event.getAssertionGroup('TestValidatorWithGuard3')
        println transRunner.simHandle.event.allAssetionGroups
//        transRunner.simHandle.event.allAssetionGroups[0].assertions.each { println it }
//        transRunner.simHandle.event.allAssetionGroups[0].assertionIds.each { println it }

        then:
        !transRunner.simHandle.event.hasErrors()
        assertionGroup.hasAssertion('TV101')
        assertionGroup.hasAssertion('TV102')
    }

}
