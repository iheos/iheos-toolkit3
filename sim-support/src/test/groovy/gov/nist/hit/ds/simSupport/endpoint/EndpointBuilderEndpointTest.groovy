package gov.nist.hit.ds.simSupport.endpoint

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.AsyncType
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.actorTransaction.TlsType
import gov.nist.hit.ds.simSupport.client.SimId
import spock.lang.Specification

/**
 * Created by bmajur on 6/6/14.
 */
class EndpointBuilderEndpointTest extends Specification {
    static String config = '''
<ActorsTransactions>
    <transaction name="Stored Query" id="sq" code="sq" asyncCode="sq.as">
        <request action="urn:ihe:iti:2007:RegistryStoredQuery"/>
        <response action="urn:ihe:iti:2007:RegistryStoredQueryResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Register" id="rb" code="rb" asyncCode="r.as">
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Provide and Register" id="prb" code="prb" asyncCode="pr.as">
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="unused"/>
    </transaction>
    <transaction name="Update" id="update" code="update" asyncCode="update.as">
        <request action="urn:ihe:iti:2010:UpdateDocumentSet"/>
        <response action="urn:ihe:iti:2010:UpdateDocumentSetResponse"/>
        <implClass value="unused"/>
    </transaction>
    <actor name="Document Registry" id="reg">
        <implClass value="unused"/>
        <transaction id="rb"/>
        <transaction id="sq"/>
        <transaction id="update"/>
    </actor>
    <actor name="Document Repository" id="rep">
        <implClass value="unused"/>
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

    def 'Build simple endpoint'() {
        when:
        def builder = new EndpointBuilder('localhost', '8080', 'xdstools2/sims', new SimId('123'))
        def transType = new ActorTransactionTypeFactory().getTransactionType('rb')
        def elabel = new EndpointType(transType, TlsType.NOTLS, AsyncType.SYNC)
        def endpoint = builder.makeEndpoint(atFactory.getActorType('reg'), elabel)

        then:
        endpoint.value == 'http://localhost:8080/xdstools2/sims/123/reg/rb'
    }

}
