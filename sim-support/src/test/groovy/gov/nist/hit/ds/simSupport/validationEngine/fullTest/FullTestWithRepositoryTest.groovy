package gov.nist.hit.ds.simSupport.validationEngine.fullTest
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.EventDAO
import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.repository.api.Repository
import gov.nist.hit.ds.repository.api.RepositoryFactory
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.repository.simple.SimpleType
import gov.nist.hit.ds.toolkit.installation.Installation
import groovy.util.logging.Log4j
import org.apache.commons.io.FileUtils
import spock.lang.Specification
/**
 * Created by bmajur on 6/28/14.
 */
@Log4j
class FullTestWithRepositoryTest extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction displayName="Register" id="rb" code="rb" asyncCode="r.as"
       class="">
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
    </transaction>
    <actor displayName="Document Registry" id="reg"
      class="">
        <transaction id="rb"/>
    </actor>
</ActorsTransactions>
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
    def eventLogDirName = 'EventLog'
    RepositorySource repoSource
    File repoDataDir
    RepositoryFactory repoFactory

    def setup() {
        Installation.reset()
        Installation.installation().initialize()
        Configuration.configuration()
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        repoFactory = new RepositoryFactory(repoSource)

        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
    }

    File eventLogFile() { new File(repoDataDir, eventLogDirName) }

    File eventFile(Event event) { new File(eventLogFile(), event.eventAsset.getDisplayName()) }

    File reqBodyFile(Event event) {
        new File(new File(eventFile(event), 'Input Output Messages'), 'Request Body.bytes')
    }

    File assertionGroupFile(Event event) {
        new File(new File(eventFile(event), 'Validators'), 'AssertionGroup.csv')
    }

    def initTest() {
        if (repoDataDir.exists()) {
            log.debug 'cleaning dataDir'
            FileUtils.cleanDirectory(repoDataDir)
        }
    }

    def myValidatorTransaction(Event event) {
        String msg = event.inOut.reqBody.toString()
        MyValidator myValidator = new MyValidator(event, msg)
        myValidator.runValidationEngine()
    }

    def 'Should Init'() {
        given: ''

        when: ''
        initTest()

        then: ''
        !eventLogFile().exists()
    }

    // Execute single validator (two steps) and verify the EventLog repository is created
    // and has the content
    def 'Validator should generate EventLog output'() {
        given: ''
        initTest()

        when: ''
        Repository eventRepo = repoFactory.createNamedRepository('eventRepo', '', new SimpleType('eventLog'), eventLogDirName)
        Event event = new EventFactory().buildEvent(eventRepo)
        event.init()

        // Setup
        String msg = 'Message'
        event.inOut.reqBody = msg.getBytes()
        new EventDAO(event).init()

        // Run
        myValidatorTransaction(event)

        // Cleanup
        event.flush()

        then: ''
        eventFile(event).exists()
        reqBodyFile(event).exists()
        assertionGroupFile(event).exists()
    }

    def 'Run Toy Service'() {
        given: ''

        when: ''
        def runner = new TestToolkitServiceRunner()
        runner.run('ToyService', ['one' : '2', 'two' : '2'])

        then: ''
    }


}



