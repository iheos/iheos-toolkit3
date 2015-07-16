package gov.nist.hit.ds.dsSims.eb.metadata
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.dsSims.eb.generator.RimGenerator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SubmissionSetModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.validator.SubmissionSetValidator
import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataParser
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.utilities.xml.Parse
import gov.nist.toolkit.valsupport.client.ValidationContext
import gov.nist.toolkit.valsupport.engine.DefaultValidationContextFactory
import org.apache.axiom.om.OMElement
import spock.lang.Specification
/**
 * Created by bmajur on 8/21/14.
 */
class SSSqResponseTestEx extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction name="Register" id="rb" code="rb" asyncCode="r.as">
       <implClass value="RegisterTransaction"/>
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
    SimId simId
    def repoName = 'Sim'

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimId('SSSqResponseTest')
        SimUtils.create('reg', simId, repoName)
    }

    def ssModel(spec) {
        def ssXml = new RimGenerator().evalSubmissionSet(spec)
        OMElement xml = Parse.parse_xml_string(ssXml)
        Metadata m = MetadataParser.parseNonSubmission(xml)
        SubmissionSetModel model = new SubmissionSetModel(m, xml)
        println model
        [ssXml, model]
    }

    def run(submissionSpec, vc) {
        def (ssXml, model) = ssModel(submissionSpec)
        println ssXml
        Closure closure = { simHandle ->
            new SubmissionSetValidator(simHandle, model, vc, [] as Set).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, repoName, closure)
        transRunner.simHandle.event.addArtifact('Metadata', ssXml)
        transRunner
    }

    def 'SQ Response SubmissionSet should validate'() {
        def id = 'urn:uuid:ec05c9a2-c17b-4746-a3c1-882f03e92d1c'
        def submissionSpec =     [
                    type: 'SubmissionSet',
                    'id': id,
                    'lid': id,
                    'status': 'Approved',
                    attributes: [[name:'version', value:'1.1'],
                            [name:'submissionTime', values:['2004']]]
        ]

        when:
        ValidationContext vc = DefaultValidationContextFactory.validationContext()
        vc.isSQ = true
        vc.isResponse = true
        def transRunner = run(submissionSpec, vc)
        transRunner.runTest()

        then:
        !transRunner.simHandle.event.hasErrors()
    }

    def 'submissionTime format should fail'() {
        def id = 'SS1'
        def submissionSpec =     [
                type: 'SubmissionSet',
                'id': id,
                'lid': id,
                'status': 'Approved',
                attributes: [[name:'version', value:'1.1'],
                             [name:'submissionTime', values:['football']]]
        ]

        when:
        ValidationContext vc = DefaultValidationContextFactory.validationContext()
        vc.isSQ = true
        vc.isResponse = true
        def transRunner = run(submissionSpec, vc)
        transRunner.runTest()
        def assertionGroup = transRunner.simHandle.event.getAssertionGroup('SubmissionSetSlotsValidator')

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('DTM002')
    }

    def 'SQ Response SubmissionSet missing status should fail'() {
        def id = 'SS1'
        def submissionSpec =     [
                type: 'SubmissionSet',
                'id': id,
                'lid': id,
                attributes: [[name:'version', value:'1.1']]
        ]

        when:
        ValidationContext vc = DefaultValidationContextFactory.validationContext()
        vc.isSQ = true
        vc.isResponse = true
        def transRunner = run(submissionSpec, vc)
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasErrors()
    }

    def 'SQ Response SubmissionSet missing version should fail'() {
        def id = 'SS1'
        def submissionSpec =     [
                type: 'SubmissionSet',
                'id': id,
                'lid': id,
                'status': 'Approved',
                attributes: []
        ]

        when:
        ValidationContext vc = DefaultValidationContextFactory.validationContext()
        vc.isSQ = true
        vc.isResponse = true
        def transRunner = run(submissionSpec, vc)
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasErrors()
    }

    def 'SQ Response SubmissionSet missing id should fail'() {
        def id = 'SS1'
        def submissionSpec =     [
                type: 'SubmissionSet',
                'status': 'Approved',
                attributes: [[name:'version', value:'1.1']]
        ]

        when:
        ValidationContext vc = DefaultValidationContextFactory.validationContext()
        vc.isSQ = true
        vc.isResponse = true
        def transRunner = run(submissionSpec, vc)
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasErrors()
    }

}
