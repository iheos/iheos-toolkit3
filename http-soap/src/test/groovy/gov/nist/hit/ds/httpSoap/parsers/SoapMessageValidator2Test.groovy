package gov.nist.hit.ds.httpSoap.parsers
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.testSupport.EventAccess
import gov.nist.hit.ds.httpSoap.validators.SoapMessageValidator
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimEventAccess
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.soapSupport.SoapFaultException
import spock.lang.Specification
/**
 * Created by bmajur on 7/20/14.
 */
class SoapMessageValidator2Test extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction name="Register" id="rb" code="rb" asyncCode="r.as">
       <implClass value="gov.nist.hit.ds.dsSims.reg.RegisterTransaction"/>
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <params multiPart="false" soap="true"/>
    </transaction>
    <actor name="Document Registry" id="reg">
      <implClass value=""/>
        <transaction id="rb"/>
    </actor>
</ActorsTransactions>
'''

    File repoDataDir
    RepositorySource repoSource
    SimIdentifier simId
    def repName = 'Sim'

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimIdentifier(repName, 'SoapMessageValidator2Test')
        SimUtils.create('reg', simId)
    }

    def 'SoapMessageValidator should succeed'() {
        def envelope = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope" xmlns:wsa="http://www.w3.org/2005/08/addressing">
  <soapenv:Header>
    <wsa:To>http://localhost:9085/axis2/services/registryBonedoc</wsa:To><wsa:MessageID>urn:uuid:1CF198BACD3697AB7D1203091097442</wsa:MessageID>
    <wsa:Action soapenv:mustUnderstand="true">urn:ihe:iti:2007:RegisterDocumentSet-b</wsa:Action>
  </soapenv:Header>
  <soapenv:Body>
    <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
      <rim:RegistryObjectList xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
        <rim:ExtrinsicObject id="Document01" mimeType="text/plain" objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"/>
      </rim:RegistryObjectList>
    </lcm:SubmitObjectsRequest>
  </soapenv:Body>
</soapenv:Envelope>
'''
        when:
        Closure closure = { simHandle ->
            new SoapMessageValidator(simHandle, envelope).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        def eventAccess = new SimEventAccess(simId, transRunner.simHandle.event)

        then:
        !transRunner.simHandle.event.hasErrors()
        eventAccess.assertionGroupFile('SoapMessageValidator').exists()
    }

    def 'SoapMessageValidator should fail on XML parse'() {
        def envelope = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope" xmlns:wsa="http://www.w3.org/2005/08/addressing">
  <soapenv:Header>
    <wsa:To>http://localhost:9085/axis2/services/registryBonedoc</wsa:To><wsa:MessageID>urn:uuid:1CF198BACD3697AB7D1203091097442</wsa:MessageID>
    <wsa:Action soapenv:mustUnderstand="true">urn:ihe:iti:2007:RegisterDocumentSet-b</wsa:Action>
  </soapenv:Header>
  <soapenv:Body>
    <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
      <rim:RegistryObjectList xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
        <rim:ExtrinsicObject id="Document01" mimeType="text/plain" objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"/>
      </rim:RegistryObjectList>
    </lcm:SubmitObjectsRequest>
  </soapenv:Body>
</soapenv:Envelope'''
        when:
        Closure closure = { simHandle ->
            new SoapMessageValidator(simHandle, envelope).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        def eventAccess = new SimEventAccess(simId, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasFault()
        eventAccess.assertionGroupFile('SoapMessageValidator').exists()
        Exception e = thrown()
        e instanceof SoapFaultException
        e.message.contains 'XML Parse errors'
    }

    def 'SoapMessageValidator should fail - no Envelope'() {
        def envelope = '''
<stuff/>
'''
        when:
        Closure closure = { simHandle ->
            new SoapMessageValidator(simHandle, envelope).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        def eventAccess = new SimEventAccess(simId, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasFault()
        eventAccess.assertionGroupFile('SoapMessageValidator').exists()
        Exception e = thrown()
        e instanceof SoapFaultException
        e.message.contains 'Top element must be Envelope'
        e.message.contains 'stuff'
    }

    def 'SoapMessageValidator should fail - Header missing'() {
        def envelope = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope" xmlns:wsa="http://www.w3.org/2005/08/addressing">
  <soapenv:Body>
    <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
      <rim:RegistryObjectList xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
        <rim:ExtrinsicObject id="Document01" mimeType="text/plain" objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"/>
      </rim:RegistryObjectList>
    </lcm:SubmitObjectsRequest>
  </soapenv:Body>
</soapenv:Envelope>
'''
        when:
        Closure closure = { simHandle ->
            new SoapMessageValidator(simHandle, envelope).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        def eventAccess = new SimEventAccess(simId, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasFault()
        eventAccess.assertionGroupFile('SoapMessageValidator').exists()
        Exception e = thrown()
        e instanceof SoapFaultException
        e.message.contains 'Header must be present and be first child of Envelope'
        e.message.contains 'Expected: Header; Found: Body'
    }

    def 'SoapMessageValidator should fail - Header out of order'() {
        def envelope = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope" xmlns:wsa="http://www.w3.org/2005/08/addressing">
  <soapenv:Body>
    <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
      <rim:RegistryObjectList xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
        <rim:ExtrinsicObject id="Document01" mimeType="text/plain" objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"/>
      </rim:RegistryObjectList>
    </lcm:SubmitObjectsRequest>
  </soapenv:Body>
  <soapenv:Header>
    <wsa:To>http://localhost:9085/axis2/services/registryBonedoc</wsa:To><wsa:MessageID>urn:uuid:1CF198BACD3697AB7D1203091097442</wsa:MessageID>
    <wsa:Action soapenv:mustUnderstand="true">urn:ihe:iti:2007:RegisterDocumentSet-b</wsa:Action>
  </soapenv:Header>
</soapenv:Envelope>
'''
        when:
        Closure closure = { simHandle ->
            new SoapMessageValidator(simHandle, envelope).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        def eventAccess = new SimEventAccess(simId, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasFault()
        eventAccess.assertionGroupFile('SoapMessageValidator').exists()
        Exception e = thrown()
        e instanceof SoapFaultException
        e.message.contains 'Header must be present and be first child of Envelope'
        e.message.contains 'Expected: Header; Found: Body'
    }

    def 'SoapMessageValidator should fail - Body missing'() {
        def envelope = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope" xmlns:wsa="http://www.w3.org/2005/08/addressing">
  <soapenv:Header>
    <wsa:To>http://localhost:9085/axis2/services/registryBonedoc</wsa:To><wsa:MessageID>urn:uuid:1CF198BACD3697AB7D1203091097442</wsa:MessageID>
    <wsa:Action soapenv:mustUnderstand="true">urn:ihe:iti:2007:RegisterDocumentSet-b</wsa:Action>
  </soapenv:Header>
</soapenv:Envelope>
'''
        when:
        Closure closure = { simHandle ->
            new SoapMessageValidator(simHandle, envelope).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        def eventAccess = new SimEventAccess(simId, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasFault()
        eventAccess.assertionGroupFile('SoapMessageValidator').exists()
        Exception e = thrown()
        e instanceof SoapFaultException
        e.message.contains 'Body must be present, must be second child of Envelope'
        e.message.contains 'Expected: Body; Found: null'
    }

    def 'SoapMessageValidator should fail - Envelope namespace'() {
        def envelope = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-env" xmlns:wsa="http://www.w3.org/2005/08/addressing">
  <soapenv:Header>
    <wsa:To>http://localhost:9085/axis2/services/registryBonedoc</wsa:To><wsa:MessageID>urn:uuid:1CF198BACD3697AB7D1203091097442</wsa:MessageID>
    <wsa:Action soapenv:mustUnderstand="true">urn:ihe:iti:2007:RegisterDocumentSet-b</wsa:Action>
  </soapenv:Header>
  <soapenv:Body>
    <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
      <rim:RegistryObjectList xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
        <rim:ExtrinsicObject id="Document01" mimeType="text/plain" objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"/>
      </rim:RegistryObjectList>
    </lcm:SubmitObjectsRequest>
  </soapenv:Body>
</soapenv:Envelope>
'''
        when:
        Closure closure = { simHandle ->
            new SoapMessageValidator(simHandle, envelope).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        def eventAccess = new SimEventAccess(simId, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasFault()
        eventAccess.assertionGroupFile('SoapMessageValidator').exists()
        Exception e = thrown()
        e instanceof SoapFaultException
        e.message.contains 'Correct Envelope Namespace'
    }

    def 'SoapMessageValidator should fail - Header namespace'() {
        def envelope = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope" xmlns:wsa="http://www.w3.org/2005/08/addressing">
  <soapenvx:Header xmlns:soapenvx="http://www.w3.org/2003/05/soap-envelopexx">
    <wsa:To>http://localhost:9085/axis2/services/registryBonedoc</wsa:To><wsa:MessageID>urn:uuid:1CF198BACD3697AB7D1203091097442</wsa:MessageID>
    <wsa:Action soapenv:mustUnderstand="true">urn:ihe:iti:2007:RegisterDocumentSet-b</wsa:Action>
  </soapenvx:Header>
  <soapenv:Body>
    <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
      <rim:RegistryObjectList xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
        <rim:ExtrinsicObject id="Document01" mimeType="text/plain" objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"/>
      </rim:RegistryObjectList>
    </lcm:SubmitObjectsRequest>
  </soapenv:Body>
</soapenv:Envelope>
'''
        when:
        Closure closure = { simHandle ->
            new SoapMessageValidator(simHandle, envelope).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        def eventAccess = new SimEventAccess(simId, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasFault()
        eventAccess.assertionGroupFile('SoapMessageValidator').exists()
        Exception e = thrown()
        e instanceof SoapFaultException
//        e.message.contains 'Correct Header Namespace'
    }

    def 'SoapMessageValidator should fail - Body namespace'() {
        def envelope = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope" xmlns:wsa="http://www.w3.org/2005/08/addressing">
  <soapenv:Header>
    <wsa:To>http://localhost:9085/axis2/services/registryBonedoc</wsa:To><wsa:MessageID>urn:uuid:1CF198BACD3697AB7D1203091097442</wsa:MessageID>
    <wsa:Action soapenv:mustUnderstand="true">urn:ihe:iti:2007:RegisterDocumentSet-b</wsa:Action>
  </soapenv:Header>
  <soapenvx:Body xmlns:soapenvx="http://www.w3.org/2003/05/soap-envelopex">
    <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
      <rim:RegistryObjectList xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
        <rim:ExtrinsicObject id="Document01" mimeType="text/plain" objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"/>
      </rim:RegistryObjectList>
    </lcm:SubmitObjectsRequest>
  </soapenvx:Body>
</soapenv:Envelope>
'''
        when:
        Closure closure = { simHandle ->
            new SoapMessageValidator(simHandle, envelope).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        def eventAccess = new SimEventAccess(simId, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasFault()
        eventAccess.assertionGroupFile('SoapMessageValidator').exists()
        Exception e = thrown()
        e instanceof SoapFaultException
//        e.message.contains 'Correct Body Namespace'
    }

}
