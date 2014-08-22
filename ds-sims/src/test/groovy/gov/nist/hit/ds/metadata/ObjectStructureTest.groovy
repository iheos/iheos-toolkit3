package gov.nist.hit.ds.metadata

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.dsSims.generator.RimGenerator
import gov.nist.hit.ds.dsSims.metadataValidator.engine.MetadataValidator
import gov.nist.hit.ds.eventLog.testSupport.EventAccess
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.utilities.xml.Parse
import org.apache.axiom.om.OMElement
import spock.lang.Specification

/**
 * Created by bmajur on 8/21/14.
 */
class ObjectStructureTest extends Specification {
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

    def 'SQ Response SubmissionSet should validate'() {
        def id = 'SS1'
        def submissionSpec =     [
                [
                    type: 'SubmissionSet',
                    'id': id,
                    'status': 'Approved',
                    attributes: [[name:'version', value:'1.1']]
                ]
        ]
        def ssXml = new RimGenerator().toRimXml(submissionSpec,'SubmitObjectsRequest')

        when:
        OMElement xml = Parse.parse_xml_string(ssXml)
        Metadata m = MetadataParser.parseNonSubmission(xml)
        ValidationContext vc = new ValidationContext()
        vc.isSQ = true
        vc.isResponse = true
        Closure closure = { simHandle ->
            new MetadataValidator(simHandle, m, vc, null).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.simHandle.event.addArtifact('Metadata', ssXml)
        def eventAccess = new EventAccess(simId.id, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        !transRunner.simHandle.event.hasErrors()
    }
}
