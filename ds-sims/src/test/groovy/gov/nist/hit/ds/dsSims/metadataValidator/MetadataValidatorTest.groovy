package gov.nist.hit.ds.dsSims.metadataValidator

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.dsSims.generator.RimGenerator
import gov.nist.hit.ds.dsSims.metadataValidator.engine.MetadataValidator
import gov.nist.hit.ds.eventLog.testSupport.EventAccess
import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.metadata.MetadataParser
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimSystemConfig
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification
/**
 * Created by bmajur on 8/18/14.
 */
class MetadataValidatorTest extends Specification {
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

    File repoDataDir
    RepositorySource repoSource
    SimId simId
    def repoName = 'Sim'

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimId('123')
        SimUtils.create('reg', simId, new SimSystemConfig().repoName)
    }

    def 'Submission passes metadata validator'() {
        def id = 'SS1'
        def submissionSpec =     [    [
                                              type: 'SubmissionSet',
                                              'id': id,
                                              attributes: [[name:'submissionTime', values:['2004']]]
                                      ] ]
        def ssXml = new RimGenerator().toRimXml(submissionSpec,'SubmitObjectsRequest')
        Metadata metadata = MetadataParser.parseNonSubmission(ssXml)
        ValidationContext vc = new ValidationContext()
        vc.isR = true
        vc.isRequest = true

        when:
        Closure closure = { simHandle ->
            new MetadataValidator(simHandle, metadata, vc, null).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, repoName, closure)
        transRunner.simHandle.event.addArtifact('Metadata', ssXml)
        def eventAccess = new EventAccess(simId.id, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        !transRunner.simHandle.event.hasErrors()
    }

}
