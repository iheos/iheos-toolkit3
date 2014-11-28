package gov.nist.hit.ds.simSupport.simGeneration
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.AsyncType
import gov.nist.hit.ds.actorTransaction.TlsType
import gov.nist.hit.ds.simSupport.config.SimConfig
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import gov.nist.hit.ds.simSupport.simulator.SimConfigFactory
import spock.lang.Specification
/**
 * Created by bmajur on 6/5/14.
 */
class CreateSimTest extends Specification {
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
    <transaction name="Provide and Register" code="prb" asyncCode="pr.as">
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
        <implClass value="gov.nist.hit.ds.registrySim.factories.DocumentRegistryActorFactory"/>
        <transaction id="rb"/>
        <transaction id="sq"/>
        <transaction id="update"/>
    </actor>
    <actor name="Document Repository" id="rep">
        <implClass unused="gov.nist.hit.ds.registrySim.factory.DocumentRepositoryActorFactory"/>
        <transaction id="prb"/>
    </actor>
</ActorsTransactions>
'''

    ActorTransactionTypeFactory atFactory

    void setup() {
        atFactory = new ActorTransactionTypeFactory()
        atFactory.clear()
        atFactory.loadFromString(config)
    }

    def 'SimConfig should contain transaction configs'() {
        given:
        def server = 'localhost'
        def port = '8080'
        def base = '/xdstools3/sim'
        def simId = new SimId('123')
        def actorTypeName = 'reg'

        when:
        SimConfigFactory factory = new SimConfigFactory()
        SimConfig actorSimConfig = factory.buildSim(server, port, base, simId, atFactory.getActorType(actorTypeName))

        then:
        actorSimConfig
        actorSimConfig.getActorType().getShortName() == actorTypeName
    }

    def 'SimConfig lookup should return correct endpoint'() {
        given:
        def server = 'localhost'
        def port = '8080'
        def base = '/xdstools3/sim'
        def simId = new SimId('123')
        def actorTypeName = 'reg'

        when:
        SimConfigFactory factory = new SimConfigFactory()
        SimConfig actorSimConfig = factory.buildSim(server, port, base, simId, atFactory.getActorType(actorTypeName))
        EndpointValue endpoint = actorSimConfig.getEndpoint(
                new ActorTransactionTypeFactory().getTransactionType("rb"),
                TlsType.TLS,
                AsyncType.SYNC)

        then:
        endpoint.value == 'https://localhost:8080/xdstools3/sim/123/reg/rb'
    }
}
