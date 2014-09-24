package gov.nist.hit.ds.simSupport
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.simSupport.service.SimService
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
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
    <transaction displayName="Provide and Register" id="prb" code="prb" asyncCode="pr.as">
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
    </transaction>
    <transaction displayName="Update" id="update" code="update" asyncCode="update.as">
        <request action="urn:ihe:iti:2010:UpdateDocumentSet"/>
        <response action="urn:ihe:iti:2010:UpdateDocumentSetResponse"/>
    </transaction>
    <actor displayName="Document Registry" id="reg">
        <simFactoryClass class="gov.nist.hit.ds.simSupport.factories.DocumentRegistryActorFactory"/>
        <transaction id="rb"/>
        <transaction id="sq"/>
        <transaction id="update"/>
    </actor>
    <actor displayName="Document Repository" id="rep">
        <simFactoryClass class="gov.nist.hit.ds.registrySim.factory.DocumentRepositoryActorFactory"/>
        <transaction id="prb"/>
        <property name="repositoryUniqueId" value="1.2.3.4"/>
    </actor>
</ActorsTransactions>
'''
    def factory = new ActorTransactionTypeFactory()
    def simId = '1234'
    def actorType = 'reg'

    void setup() {
        SimSupport.initialize()
        factory.clear()
        factory.loadFromString(config)
    }

    def create() { new SimService().create(simId, actorType) }
    def load() { return new SimService().load(simId) }
    def delete() { new SimService().delete(simId) }
    def exists() { new SimService().exists(simId)}

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

    def 'Sim should be loadable'() {
        when: create()
        then: exists()
        then: load()
        when: delete()
        then: !exists()
    }

    def 'Create second copy of sim should fail'() {
        when: create()
        then: exists()
        when: create()
        then: thrown ToolkitRuntimeException
        when: delete()
        then: !exists()
    }

}
