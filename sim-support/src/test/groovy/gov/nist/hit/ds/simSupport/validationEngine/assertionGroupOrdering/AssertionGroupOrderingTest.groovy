package gov.nist.hit.ds.simSupport.validationEngine.assertionGroupOrdering

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.simulator.SimIdentifier
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimEventAccess
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification

/**
 * Created by bmajur on 12/22/14.
 */
class AssertionGroupOrderingTest extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction name="Register" id="rb" code="rb" asyncCode="r.as">
       <implClass value="RegisterTransaction"/>
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
    SimIdentifier simId
    String repoName = 'sim'

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimIdentifier(repoName, 'AssertionGroupOrderingTest')
        SimUtils.recreate('reg', simId)
    }

    def 'Sequential validators should validate'() {
        when:
        Closure closure = { simHandle ->
            new Validator1(simHandle).asPeer().run()
            new Validator2(simHandle).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        def eventAccess = new SimEventAccess(simId, transRunner.simHandle.event)
        transRunner.runTest()
        Properties props1 = eventAccess.properties('Validator1')
        Properties props2 = eventAccess.properties('Validator2')

        then:
        props2.get('displayOrder') > props1.get('displayOrder')

    }

}
