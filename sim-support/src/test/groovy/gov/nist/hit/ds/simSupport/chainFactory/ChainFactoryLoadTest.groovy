package gov.nist.hit.ds.simSupport.chainFactory

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.simSupport.loader.ActorDefinitionVerifier
import spock.lang.Specification

/**
 * Created by bmajur on 6/25/14.
 */
class ChainFactoryLoadTest extends Specification {
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
    <transaction displayName="Foo2 Query" id="foo2" code="foo2" asyncCode="foo2.as">
        <request action="urn:foo"/>
        <response action="urn:foo-response"/>
        <simChain resource="chain2.properties"/>
    </transaction>
    <transaction displayName="Foo3 Query" id="foo3" code="foo3" asyncCode="foo3.as">
        <request action="urn:foo"/>
        <response action="urn:foo-response"/>
        <simChain resource="chain3.properties"/>
    </transaction>
    <transaction displayName="Foo4 Query" id="foo4" code="foo4" asyncCode="foo4.as">
        <request action="urn:foo"/>
        <response action="urn:foo-response"/>
        <simChain resource="chain4.properties"/>
    </transaction>
    <actor displayName="Bar Registry" id="bar">
        <simClass class=""/>
        <transaction id="foo"/>
    </actor>
    <actor displayName="Bar2 Registry" id="bar2">
        <simClass class=""/>
        <transaction id="foo2"/>
    </actor>
    <actor displayName="Bar3 Registry" id="bar3">
        <simClass class=""/>
        <transaction id="foo3"/>
    </actor>
    <actor displayName="Bar4 Registry" id="bar4">
        <simClass class=""/>
        <transaction id="foo4"/>
    </actor>
</ActorsTransactions>
'''
    void setup() {
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().load(config)
    }

    def 'Good config should verify'() {
        given: ''
        def verifier = new ActorDefinitionVerifier()

        when: ''

        then: ''
        verifier.verify('bar') == null
    }

    def 'Config with bad chain def should not verify'() {
        given: ''
        def verifier = new ActorDefinitionVerifier()

        when: ''
        def errs = verifier.verify('bar2')

        then: ''
        errs.contains('Error verifying Actor')
        errs.contains('bar2')
        errs.contains('Cannot load SimChain definition from')
    }

    def 'Config with bad simStep def should not verify'() {
        given: ''
        def verifier = new ActorDefinitionVerifier()

        when: ''
        def errs = verifier.verify('bar3')
//        println "Big Error: ${errs}"

        then: ''
        errs.contains('Error verifying Actor')
        errs.contains('bar3')
        errs.contains('Component class not found')
    }

    def 'Config with bad simStep class should not verify'() {
        given: ''
        def verifier = new ActorDefinitionVerifier()

        when: ''
        def errs = verifier.verify('bar4')
//        println "Big Error: ${errs}"

        then: ''
        errs.contains('Error verifying Actor')
        errs.contains('bar4')
        errs.contains('does not implement the SimComponent interface')
    }
}
