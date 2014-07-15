package gov.nist.hit.ds.dsSims.reg
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.testSupport.EventAccess
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
 * Created by bmajur on 7/12/14.
 */
class TransactionFailureTest extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction displayName="Fault" id="fault" code="fault" asyncCode="fault.as"
       class="gov.nist.hit.ds.dsSims.bad.FailsWithFaultValidator">
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <params multiPart="false" soap="true"/>
    </transaction>
    <transaction displayName="Error" id="error" code="error" asyncCode="errof.as"
       class="gov.nist.hit.ds.dsSims.bad.FailsWithErrorValidator">
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <params multiPart="false" soap="true"/>
    </transaction>
    <actor displayName="Document Registry" id="my"
      class="">
        <transaction id="fault"/>
        <transaction id="error"/>
    </actor>
</ActorsTransactions>
'''

    File repoDataDir
    def repoSource

    def initTest() {
        if (repoDataDir.exists()) {
            log.debug 'cleaning dataDir'
            FileUtils.cleanDirectory(repoDataDir)
        }
    }
    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
    }


    def 'Should fail with fault'() {
        when: ''
        def simId = new SimId('123')
        def endpoint = 'http://localhost:8080/tools/sim/123/my/fault'
        if (!SimUtils.exists(simId)) SimUtils.mkSim('my', simId)
        def runner = new TransactionRunner(endpoint, null, null)
        def handle = runner.simHandle
        def eventAccess = new EventAccess(simId.id, handle.event)
        runner.run()

        then:
        thrown SoapFaultException
        handle.event.hasFault()
        eventAccess
        eventAccess.simDir().exists()
        eventAccess.eventLogDir().exists()
        eventAccess.eventDir().exists()
        eventAccess.assertionGroupFile('FailsWithFaultValidator').exists()
        eventAccess.faultFile().exists()
    }

    def 'Should fail with error'() {
        when: ''
        def simId = new SimId('123')
        def endpoint = 'http://localhost:8080/tools/sim/123/my/error'
        if (!SimUtils.exists(simId)) SimUtils.mkSim('my', simId)
        def runner = new TransactionRunner(endpoint, null, null)
        def handle = runner.simHandle
        def eventAccess = new EventAccess(simId.id, handle.event)
        runner.run()

        then:
        notThrown SoapFaultException
        handle.event.hasErrors()
        eventAccess
        eventAccess.simDir().exists()
        eventAccess.eventLogDir().exists()
        eventAccess.eventDir().exists()
        eventAccess.assertionGroupFile('FailsWithErrorValidator').exists()
        !eventAccess.faultFile().exists()
    }
}
