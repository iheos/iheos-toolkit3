package gov.nist.hit.ds.actorTransaction

import spock.lang.Specification

/**
 * Created by bill on 4/17/14.
 */
class EndpointLabelParsingTest extends Specification {
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
        <simFactoryClass class="gov.nist.hit.ds.registrySim.factories.DocumentRegistryActorFactory"/>
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
    def setup() {
        ActorTransactionTypeFactory.clear()
        new ActorTransactionTypeFactory().loadFromString(config)
    }

    def "Name TLS Async"() {
        when:
        def actorType = new ActorTransactionTypeFactory().getActorTypeIfAvailable("reg");
        def register = actorType.find("rb");
        def label = new EndpointType(actorType, "Register_TLS_ASYNC");

        then:
        register == label.getTransType()
        label.isAsync()
        label.isTls()
    }

    def "Name TLS Sync"() {
        when:
        def actorType = new ActorTransactionTypeFactory().getActorTypeIfAvailable("reg");
        def register = actorType.find("rb");
        def label = new EndpointType(actorType, "Register_TLS_SYNC");

        then:
        register == label.getTransType()
        !label.isAsync()
        label.isTls()
    }

    def "Name Async"() {
        when:
        def actorType = new ActorTransactionTypeFactory().getActorTypeIfAvailable("reg");
        def register = actorType.find("rb");
        def label = new EndpointType(actorType, "Register_ASYNC");

        then:
        register == label.getTransType()
        label.isAsync()
        !label.isTls()
    }

    def "Name"() {
        when:
        def actorType = new ActorTransactionTypeFactory().getActorTypeIfAvailable("reg");
        def register = actorType.find("rb");
        def label = new EndpointType(actorType, "Register");

        then:
        register == label.getTransType()
        !label.isAsync()
        !label.isTls()
    }
}
