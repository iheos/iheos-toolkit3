package gov.nist.hit.ds.simSupport.chainFactory

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.simSupport.loader.ActorDefinitionVerifier
import spock.lang.Specification

/**
 * Created by bmajur on 6/26/14.
 */
class ChainFactoryRunTest extends Specification {
    static String config = '''
<ActorsTransactions>
    <transaction displayName="Foo Query" id="foo" code="foo" asyncCode="foo.as">
        <request action="urn:foo"/>
        <response action="urn:foo-response"/>
        <simChain resource="chain1.properties"/>
    </transaction>
    <actor displayName="Bar Registry" id="bar">
        <simClass class=""/>
        <transaction id="foo"/>
    </actor>
</ActorsTransactions>
'''
    void setup() {
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().load(config)
    }

    def 'Config should verify'() {
        given: ''
        def verifier = new ActorDefinitionVerifier()

        when: ''

        then: ''
        verifier.verify('bar') == null
    }


    def 'No Name'() {
        given: 'base of SoapEnvironment'

        when: ''

        then: ''
    }
}
