package gov.nist.hit.ds.simSupport
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimSystemConfig
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification
/**
 * Created by bmajur on 9/22/14.
 */
class SimulatorFactoryTest extends Specification {
    static String config = '''
<ActorsTransactions>
    <transaction displayName="Stored Query" id="sq" code="sq" asyncCode="sq.as">
        <request action="urn:ihe:iti:2007:RegistryStoredQuery"/>
        <response action="urn:ihe:iti:2007:RegistryStoredQueryResponse"/>
    </transaction>
    <transaction displayName="Register" id="rb" code="rb" asyncCode="r.as">
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
    </transaction>
    <transaction displayName="Provide and Register" id="prb" code="prb">
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
    </transaction>
    <transaction displayName="Update" id="update" code="update" asyncCode="update.as">
        <request action="urn:ihe:iti:2010:UpdateDocumentSet"/>
        <response action="urn:ihe:iti:2010:UpdateDocumentSetResponse"/>
    </transaction>
    <actor displayName="Document Registry" id="reg">
        <simFactoryClass class="gov.nist.hit.ds.dsSims.factories.DocumentRegistryActorFactory"/>
        <transaction id="rb"/>
        <transaction id="sq"/>
        <transaction id="update"/>
    </actor>
    <actor displayName="Document Repository" id="rep">
        <simFactoryClass class="gov.nist.hit.ds.registrySim.factory.DocumentRepositoryActorFactory"/>
        <transaction id="prb"/>
        <property name="repositoryUniqueId" value="1.2.3.4"/>
    </actor>
    <actor displayName="Document Recipient" id="docrec">
        <simFactoryClass class="gov.nist.hit.ds.simSupport.factories.DocumentRecipientActorFactory"/>
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
