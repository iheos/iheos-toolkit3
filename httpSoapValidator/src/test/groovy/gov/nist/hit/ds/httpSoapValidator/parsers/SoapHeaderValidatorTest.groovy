package gov.nist.hit.ds.httpSoapValidator.parsers
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.testSupport.EventAccess
import gov.nist.hit.ds.httpSoapValidator.components.validators.SoapHeaderValidator
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.xml.Parse
import org.apache.axiom.om.OMElement
import spock.lang.Specification
/**
 * Created by bmajur on 7/23/14.
 */
class SoapHeaderValidatorTest extends Specification {
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

    File repoDataDir
    RepositorySource repoSource
    SimId simId

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimId('123')
        SimUtils.create('reg', simId)
    }

    def 'SoapHeaderValidator should succeed'() {
        def header = '''
  <soapenv:Header xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope" xmlns:wsa="http://www.w3.org/2005/08/addressing">
    <wsa:To>http://localhost:9085/axis2/services/registryBonedoc</wsa:To><wsa:MessageID>urn:uuid:1CF198BACD3697AB7D1203091097442</wsa:MessageID>
    <wsa:Action soapenv:mustUnderstand="true">urn:ihe:iti:2007:RegisterDocumentSet-b</wsa:Action>
  </soapenv:Header>
'''
        when:
        OMElement xml = Parse.parse_xml_string(header)
        Closure closure = { simHandle ->
            new SoapHeaderValidator(simHandle, xml).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        def eventAccess = new EventAccess(simId.id, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        !transRunner.simHandle.event.hasErrors()
        eventAccess.assertionGroupFile('SoapHeaderValidator').exists()
    }

    def 'SoapHeaderValidator fails - namespace on MessageID'() {
        def header = '''
  <soapenv:Header xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope" xmlns:wsa="http://www.w3.org/2005/08/addressing">
    <wsa:To>http://localhost:9085/axis2/services/registryBonedoc</wsa:To><soapenv:MessageID>urn:uuid:1CF198BACD3697AB7D1203091097442</soapenv:MessageID>
    <wsa:Action soapenv:mustUnderstand="true">urn:ihe:iti:2007:RegisterDocumentSet-b</wsa:Action>
  </soapenv:Header>
'''
        when:
        OMElement xml = Parse.parse_xml_string(header)
        Closure closure = { simHandle ->
            new SoapHeaderValidator(simHandle, xml).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        def eventAccess = new EventAccess(simId.id, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasFault()
        eventAccess.assertionGroupFile('SoapHeaderValidator').exists()
        thrown SoapFaultException
        transRunner.simHandle.event.assertionGroup.hasAssertion('WSA011')
    }


}
