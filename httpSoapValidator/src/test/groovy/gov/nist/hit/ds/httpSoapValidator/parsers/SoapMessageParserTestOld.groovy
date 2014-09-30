package gov.nist.hit.ds.httpSoapValidator.parsers
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.testSupport.EventAccess
import gov.nist.hit.ds.httpSoapValidator.validators.SoapMessageParser
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.soapSupport.SoapFaultException
import org.apache.commons.io.FileUtils
import spock.lang.Specification
/**
 * Created by bmajur on 7/16/14.
 */
class SoapMessageParserTestOld extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction displayName="Register" id="rb" code="rb" asyncCode="r.as"
       class="gov.nist.hit.ds.dsSims.reg.RegisterTransaction">
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <params multiPart="false" soap="true"/>
    </transaction>
    <actor displayName="Document Registry" id="reg"
      class="">
        <transaction id="rb"/>
    </actor>
</ActorsTransactions>
'''


    def initTest() {
        if (repoDataDir.exists()) {
            log.debug 'cleaning dataDir'
            FileUtils.cleanDirectory(repoDataDir)
        }
    }

    File repoDataDir
    def repoSource

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
    }

    def 'SoapMessageParser should succeed'() {
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
</soapenv:Envelope>'''
        when:
        def simId = new SimId('123')
        SimUtils.create('reg', simId)
        Closure closure = { simHandle ->
            new SoapMessageParser(simHandle, envelope).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        def eventAccess = new EventAccess(simId.id, transRunner.simHandle.event)


        then:
        !transRunner.simHandle.event.hasErrors()
        eventAccess.assertionGroupFile('SoapMessageParser').exists()
    }

    def 'SoapMessageParser should fault - no body'() {
        def envelope = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope" xmlns:wsa="http://www.w3.org/2005/08/addressing">
  <soapenv:Header>
    <wsa:To>http://localhost:9085/axis2/services/registryBonedoc</wsa:To><wsa:MessageID>urn:uuid:1CF198BACD3697AB7D1203091097442</wsa:MessageID>
    <wsa:Action soapenv:mustUnderstand="true">urn:ihe:iti:2007:RegisterDocumentSet-b</wsa:Action>
  </soapenv:Header>
</soapenv:Envelope>'''
        when:
        def simId = new SimId('123')
        SimUtils.create('reg', simId)
        Closure closure = { simHandle ->
            new SoapMessageParser(simHandle, envelope).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        def eventAccess = new EventAccess(simId.id, transRunner.simHandle.event)


        then:
        SoapFaultException e = thrown()
        e.message.startsWith 'Verify Soap Body'
    }

    def 'SoapMessageParser should fault - no header'() {
        def envelope = '''
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope" xmlns:wsa="http://www.w3.org/2005/08/addressing">
  <soapenv:Body>
    <lcm:SubmitObjectsRequest xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0">
      <rim:RegistryObjectList xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
        <rim:ExtrinsicObject id="Document01" mimeType="text/plain" objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"/>
      </rim:RegistryObjectList>
    </lcm:SubmitObjectsRequest>
  </soapenv:Body>
</soapenv:Envelope>'''
        when:
        def simId = new SimId('123')
        SimUtils.create('reg', simId)
        Closure closure = { simHandle ->
            new SoapMessageParser(simHandle, envelope).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        def eventAccess = new EventAccess(simId.id, transRunner.simHandle.event)


        then:
        SoapFaultException e = thrown()
        e.message.startsWith 'Verify Soap Header'
    }

    def 'SoapMessageParser should fail - XML parser'() {
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
        def simId = new SimId('123')
        SimUtils.create('reg', simId)
        Closure closure = { simHandle ->
            new SoapMessageParser(simHandle, envelope).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        def eventAccess = new EventAccess(simId.id, transRunner.simHandle.event)


        then:
        SoapFaultException e = thrown()
        e.message.startsWith 'Parse XML'
    }

}
