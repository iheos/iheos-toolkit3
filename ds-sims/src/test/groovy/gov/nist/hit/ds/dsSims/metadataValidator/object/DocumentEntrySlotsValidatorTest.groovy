package gov.nist.hit.ds.dsSims.metadataValidator.object
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.eventLog.testSupport.EventAccess
import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimSystemConfig
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.utilities.xml.Parse
import org.apache.axiom.om.OMElement
import spock.lang.Specification
/**
 * Created by bmajur on 9/4/14.
 */
class DocumentEntrySlotsValidatorTest extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction displayName="Register" id="rb" code="rb" asyncCode="r.as"
       class="gov.nist.hit.ds.dsSims.transactions.RegisterTransaction">
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

    def DEsubmission = '''
'''

    File repoDataDir
    RepositorySource repoSource
    SimId simId
    def repoName = 'Sim'
    def pnrText
    ValidationContext vc
    OMElement ele
    Metadata m
    DocumentEntryModel model

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimId('123')
        SimUtils.create('reg', simId, new SimSystemConfig())
        pnrText = this.getClass().classLoader.getResource('PnR1Doc.xml').getText()
        vc = new ValidationContext()
        vc.isRequest = true
        vc.isPnR = true
        ele = Parse.parse_xml_string(pnrText)
        m = new Metadata(ele)
        model = new DocumentEntryModel(m, m.extrinsicObjects.get(0))
    }

    def 'DocumentEntry Slots'() {
        when:
        Closure closure = { simHandle ->
            new DocumentEntrySlotsValidator(simHandle, model, vc).run()
        }
        def transRunner = new TransactionRunner('rb', simId, repoName, closure)
        transRunner.simHandle.event.addArtifact('Metadata', pnrText)
        def eventAccess = new EventAccess(simId.id, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        !transRunner.simHandle.event.hasErrors()
//        eventAccess.assertionGroupFile('DSMetadataProcessing').exists()
    }
}
