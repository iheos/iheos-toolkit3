package gov.nist.hit.ds.simSupport.utilities
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimSystemConfig
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification
/**
 * Created by bmajur on 9/22/14.
 */
class SimCreateDeleteTest extends Specification {
    static String config = '''
<ActorsTransactions>
    <transaction name="Stored Query" code="sq" asyncCode="sq.as">
        <request action="urn:ihe:iti:2007:RegistryStoredQuery"/>
        <response action="urn:ihe:iti:2007:RegistryStoredQueryResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Register" code="rb" asyncCode="r.as">
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Provide and Register" code="prb">
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Update" code="update" asyncCode="update.as">
        <request action="urn:ihe:iti:2010:UpdateDocumentSet"/>
        <response action="urn:ihe:iti:2010:UpdateDocumentSetResponse"/>
        <implClass value="unused"/>
    </transaction>
    <actor name="Document Registry" id="reg">
        <implClass value="gov.nist.hit.ds.dsSims.factories.DocumentRegistryActorFactory"/>
        <transaction id="rb"/>
        <transaction id="sq"/>
        <transaction id="update"/>
    </actor>
    <actor name="Document Repository" id="rep">
        <implClass value="gov.nist.hit.ds.registrySim.factory.DocumentRepositoryActorFactory"/>
        <transaction id="prb"/>
    </actor>
    <actor name="Document Recipient" id="docrec">
        <implClass value="gov.nist.hit.ds.simSupport.factories.DocumentRecipientActorFactory"/>
        <transaction id="prb"/>
    </actor>
</ActorsTransactions>
'''
    def factory = new ActorTransactionTypeFactory()
    def simId = new SimId('1234')
    def actorType = 'docrec'
    def repoName = 'Sim'
    def ss = new SimSystemConfig()

    void setup() {
        SimSupport.initialize()
        factory.clear()
        factory.loadFromString(config)
    }

    def create() { SimUtils.create(actorType, simId, repoName) }
//    def load() { return new SimService().load(simId) }
    def delete() { new SimUtils().delete(simId, repoName) }
    def exists() { SimUtils.exists(simId, repoName)}

    def 'Delete of missing sim should pass'() {
        when: delete()

        then:  !exists()

        when: delete()

        then: !exists()
    }

    def 'Create should create sim'() {
        when: create()

        then: exists()

        when: delete()

        then: !exists()
    }

    def 'Attempt to create second copy of sim should not cause error'() {
        when: create()
        then: exists()
        when: create()
        then: exists()
        when: delete()
        then: !exists()
    }

//    def 'Create'() {
//        when: create()
//        then: exists()
//    }

}
