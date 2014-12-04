package gov.nist.hit.ds.dsSims.eb.reg
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.testSupport.EventAccess
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.endpoint.EndpointBuilder
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
    <transaction name="Fault" code="fault" asyncCode="fault.as">
       <implClass value="gov.nist.hit.ds.dsSims.eb.bad.FailsWithFaultValidator"/>
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <params multiPart="false" soap="true"/>
    </transaction>
    <transaction name="Error" code="error" asyncCode="errof.as">
       <implClass value="gov.nist.hit.ds.dsSims.eb.bad.FailsWithErrorValidator"/>
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <params multiPart="false" soap="true"/>
    </transaction>
    <actor name="reg" id="reg">
      <implClass value=""/>
        <transaction id="fault"/>
        <transaction id="error"/>
    </actor>
</ActorsTransactions>
'''

    File repoDataDir
    def repoSource
    def repoName = 'Sim'
    ActorTransactionTypeFactory factory
    def simIdStr = 'TransactionFailureTest'

    def initTest() {
        if (repoDataDir.exists()) {
            log.debug 'cleaning dataDir'
            FileUtils.cleanDirectory(repoDataDir)
        }
    }
    def setup() {
        SimSupport.initialize()
        factory = new ActorTransactionTypeFactory()
        factory.clear()
        factory.loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
    }


    def 'Should fail with fault'() {
        when: ''
        def simId = new SimId(simIdStr)
        def simHandle = SimUtils.recreate('reg', simId, repoName)
        def endpointBuilder = new EndpointBuilder().parse('http://localhost:8080/tools/sim/123/act/fault')
        simHandle.transactionType = factory.getTransactionType(endpointBuilder.transCode)

        then:
        simHandle.transactionType

        when:
        def runner = new TransactionRunner(simHandle)
        def eventAccess = new EventAccess(simId.id, simHandle.event)
        runner.testRun()

        then:
        thrown SoapFaultException
        simHandle.event.hasFault()
        eventAccess
        eventAccess.simDir().exists()
        eventAccess.eventLogDir().exists()
        eventAccess.eventDir().exists()
        eventAccess.assertionGroupFile('FailsWithFaultValidator').exists()
        eventAccess.faultFile().exists()
    }

    def 'Should fail with error'() {
        when: ''
        def simId = new SimId(simIdStr)
        def simHandle = SimUtils.recreate('reg', simId, repoName)
        def endpointBuilder = new EndpointBuilder().parse('http://localhost:8080/tk/sim/123/reg/error')
        simHandle.transactionType = factory.getTransactionType(endpointBuilder.transCode)

        then:
        simHandle.transactionType

        when:
        def runner = new TransactionRunner(simHandle)
        def eventAccess = new EventAccess(simId.id, simHandle.event)
        runner.testRun()

        then:
        notThrown SoapFaultException
        simHandle.event.hasErrors()
        eventAccess
        eventAccess.simDir().exists()
        eventAccess.eventLogDir().exists()
        eventAccess.eventDir().exists()
        eventAccess.assertionGroupFile('FailsWithErrorValidator').exists()
        !eventAccess.faultFile().exists()
    }
}
