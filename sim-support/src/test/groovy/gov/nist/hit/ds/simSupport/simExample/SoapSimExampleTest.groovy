package gov.nist.hit.ds.simSupport.simExample

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.simSupport.simChain.SimChain
import gov.nist.hit.ds.simSupport.simChain.SimChainFactory
import gov.nist.hit.ds.simSupport.simEngine.SimEngine
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment
import spock.lang.Specification
/**
 * Created by bmajur on 6/18/14.
 *
 * Sim and val tests/examples so far don't demonstrate entire
 * picture. These show almost a soup to nuts example.  Almost
 * because only the in-memory version of the repository is used.
 */
class SoapSimExampleTest extends Specification {
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
    void setup() {
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().load(config)
    }


    // This test is more of an example than a test.  It shows
    // how to wire up a simulator with all the detail except
    // the linkage to the repository.
    def 'Sim Example'() {
        given: '''
In developing, everything coded in given and when is supplied
by the toolkit core. A sub-system built on top of core would
supply the simChain.properties file which is linked to an
endpoint/service address.
Normally the input event would come as input as part of an Event.
Here we compose the event in the given section so the sim
processing can be shown independent of the repository system.
'''
        def soapMessageText='''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
    xmlns:wsa="http://www.w3.org/2005/08/addressing">
    <soapenv:Header>
        <wsa:To>http://localhost:5000/tf6/services/xdsrepositoryb</wsa:To>
        <wsa:MessageID soapenv:mustUnderstand="true">urn:uuid:566EAD10FEBB55C5A61257193478400</wsa:MessageID>
        <wsa:Action>urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b</wsa:Action>
    </soapenv:Header>
    <soapenv:Body>
        <TheBody/>
    </soapenv:Body>
</soapenv:Envelope>
'''
        SoapEnvironment base = new SoapEnvironment()
        // Create in-memory event - we only use in-memory cache which means
        // there is no post-test cleanup to do
        Event event = new EventFactory().buildEvent(EventFactory.IN_MEMORY)
        // Insert SOAP Envelope
        event.inOut.reqBody = soapMessageText
        // Load sim chain from file based definition
        def simChainFactory = new SimChainFactory(event)
        simChainFactory.loadFromPropertyBasedResource('soapSimExampleChain.properties')
        SimChain simChain = simChainFactory.simChain
        // Load base - this is the linkage back to where the inputs came from
        // and details of the formatting so the response can be formed later
        simChain.base = base
        // Create simEngine to execute sim chain
        SimEngine engine = new SimEngine(simChain)

        when:
        // run the sim chain
        engine.run()

        then: ''
    }

    def 'Issue 1'() {
        given: '''
How is endpoint/service address tied to a simChain?
'''
        when: ''

        then: ''
    }
}
