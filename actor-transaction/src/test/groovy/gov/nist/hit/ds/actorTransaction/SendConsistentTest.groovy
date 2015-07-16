package gov.nist.hit.ds.actorTransaction

import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
import spock.lang.Specification

/**
 * Created by bmajur on 2/9/15.
 */
class SendConsistentTest extends Specification {
    def goodServer = '''
<ActorsTransactions>
    <transaction name="ss1" id="server1" code="ss1">
        <params multipart="true" soap="true"/>
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="gov.nist.hit.ds.dsSims.eb.transactions.Pnr"/>
    </transaction>
    <transaction name="ss2" id="server2" code="ss2">
        <params multipart="true" soap="true"/>
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="gov.nist.hit.ds.ebDocsrcSim.transaction.Pnr"/>
    </transaction>
    <actor name="doxy" id="dox">
        <implClass value=""/>
        <transaction id="server1" type="server"/>
        <transaction id="server1" type="server"/>
    </actor>
    </ActorsTransactions>
'''
    def goodClient = '''
<ActorsTransactions>
    <transaction name="ss1" id="server1" code="ss1">
        <params multipart="true" soap="true"/>
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="gov.nist.hit.ds.dsSims.eb.transactions.Pnr"/>
    </transaction>
    <transaction name="ss2" id="server2" code="ss2">
        <params multipart="true" soap="true"/>
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="gov.nist.hit.ds.ebDocsrcSim.transaction.Pnr"/>
    </transaction>
    <actor name="doxy" id="dox">
        <implClass value=""/>
        <transaction id="server1" type="client"/>
        <transaction id="server1" type="client"/>
    </actor>
    </ActorsTransactions>
'''
    def mixed = '''
<ActorsTransactions>
    <transaction name="ss1" id="server1" code="ss1">
        <params multipart="true" soap="true"/>
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="gov.nist.hit.ds.dsSims.eb.transactions.Pnr"/>
    </transaction>
    <transaction name="ss2" id="server2" code="ss2">
        <params multipart="true" soap="true"/>
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="gov.nist.hit.ds.ebDocsrcSim.transaction.Pnr"/>
    </transaction>
    <actor name="doxy" id="dox">
        <implClass value=""/>
        <transaction id="server1" type="client"/>
        <transaction id="server1" type="server"/>
    </actor>
    </ActorsTransactions>
'''


    def factory = new ActorTransactionTypeFactory()

    // Consistent direction (all client or all server) parses correctly
    def 'Good server test'() {
        when:
        factory.clear()
        factory.loadFromString(goodServer)

        then:
        notThrown ToolkitRuntimeException
    }

    def 'Good client test'() {
        when:
        factory.clear()
        factory.loadFromString(goodClient)

        then:
        notThrown ToolkitRuntimeException
    }

    def 'Mixed'() {
        when:
        factory.clear()
        factory.loadFromString(mixed)

        then:
        thrown ToolkitRuntimeException
    }
}
