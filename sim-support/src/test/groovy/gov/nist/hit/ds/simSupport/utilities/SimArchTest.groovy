package gov.nist.hit.ds.simSupport.utilities

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.AsyncType
import gov.nist.hit.ds.actorTransaction.TlsType
import gov.nist.hit.ds.repoSupport.RepoUtils
import gov.nist.hit.ds.repository.simple.SimpleId
import gov.nist.hit.ds.repository.simple.SimpleType
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import spock.lang.Specification

/**
 * Tests Simulator layout in repository.
 * The static utility classes RepoUtils and SimUtils are the primary targets of these tests.
 *
 * Created by bmajur on 2/2/15.
 */
class SimArchTest extends Specification {
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
    def actorType = 'docrec'
    def simId = new SimId('mydocrec')

    def setup() { // Initialize repository system
        SimSupport.initialize()
        factory.clear()
        factory.loadFromString(config)
    }

//    def 'Create user'() { // User is used as repository name
//        when:  'Create user (repository)'
//        def userName = 'bill'
//        def repoName = userName
//        def repoType = new SimpleType('user')
//        def repoArtifactId = new SimpleId(userName)
//        def user = RepoUtils.mkRepository(userName, repoType)
//
//        then:
//        RepoUtils.repositoryExists(repoArtifactId)
//
//        when: 'Create sim (for user)'
//        SimHandle simHandle = SimUtils.createSimForUser(actorType, simId, userName)  // create sim for user
//
//        then:
//        SimUtils.exists(simId, userName)
//
//        when: 'Pull config and verify tail of endpoint -> bill/mydocrec/docrec/prb'
//        EndpointValue endpointVal = simHandle.actorSimConfig.getEndpoint(factory.getTransactionTypeIfAvailable('prb'), TlsType.NOTLS, AsyncType.SYNC)
//        String endpoint = endpointVal.toString()
//
//        then:
//        endpoint.contains('bill/mydocrec/docrec/prb')
//    }

}
