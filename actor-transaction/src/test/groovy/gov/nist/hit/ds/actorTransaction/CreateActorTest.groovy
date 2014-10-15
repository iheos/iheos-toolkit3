package gov.nist.hit.ds.actorTransaction

import spock.lang.Specification

/**
 * Created by bmajur on 5/22/14.
 */
class CreateActorTest extends Specification {
    static String config = '''
<ActorsTransactions>
    <transaction name="Stored Query" code="sq" asyncCode="sq.as">
        <implClass value="unused"/>
        <request action="urn:ihe:iti:2007:RegistryStoredQuery"/>
        <response action="urn:ihe:iti:2007:RegistryStoredQueryResponse"/>
    </transaction>
    <transaction name="Register"  code="rb" asyncCode="r.as">
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
        <simFactoryClass class="gov.nist.hit.ds.registrySim.factories.DocumentRegistryActorFactory"/>
        <transaction id="rb"/>
        <transaction id="sq"/>
        <transaction id="update"/>
    </actor>
    <actor name="Document Repository" id="rep">
        <simFactoryClass class="gov.nist.hit.ds.registrySim.factory.DocumentRepositoryActorFactory"/>
        <transaction id="prb"/>
        <property name="repositoryUniqueId" value="1.2.3.4"/>
    </actor>
</ActorsTransactions>
'''
    def setup() {
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(config)
    }

    def 'Registry actor type exists in configuration - later tests cannot function without this'() {
        expect: new ActorTransactionTypeFactory().getActorTypeIfAvailable('reg') != null
    }

    def 'Repository actor type exists in configuration - later tests cannot function without this'() {
        expect: new ActorTransactionTypeFactory().getActorTypeIfAvailable('rep') != null
    }

    def 'Actor properties for Registry should contain no extra properties'() {
        setup: 'actor and transaction types are loaded by setup()'

        when: ''
        ActorType actorType = new ActorTransactionTypeFactory().getActorTypeIfAvailable('reg')


        then: 'Verify actor properties are empty'
        actorType != null
        actorType.getProps().size() == 0   // nothing defined beyond required fields
    }

    def 'Actor properties for Repository should contain extra property repositoryUniqueId'() {
        setup: 'actor and transaction types are loaded by setup()'

        when: ''
        ActorType actorType = new ActorTransactionTypeFactory().getActorTypeIfAvailable('rep')

        then: 'Verify actor properties include repositoryUniqueId'
        actorType != null
        actorType.getProps().size() == 1
        actorType.getProps().containsKey('repositoryUniqueId')
    }

}
