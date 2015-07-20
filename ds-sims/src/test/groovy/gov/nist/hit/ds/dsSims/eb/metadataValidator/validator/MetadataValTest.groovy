package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.dsSims.eb.reg.UnconnectedRegistryValidation
import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataParser
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.simulator.SimIdentifier
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.toolkit.Toolkit
import gov.nist.hit.ds.toolkit.environment.Environment
import gov.nist.toolkit.installation.Installation
import gov.nist.toolkit.valsupport.client.ValidationContext
import gov.nist.toolkit.valsupport.engine.DefaultValidationContextFactory
import groovy.xml.StreamingMarkupBuilder
import spock.lang.Specification
/**
 * Created by bmajur on 1/6/15.
 */
class MetadataValTest extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction name="Register" code="rb" asyncCode="r.as" id="rb">
       <implClass value="RegisterTransaction"/>
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <params multiPart="false" soap="true"/>
    </transaction>
    <actor displayName="Document Registry" id="ebxml"
      class="">
        <transaction id="rb"/>
    </actor>
</ActorsTransactions>
'''

    File repoDataDir
    RepositorySource repoSource
    SimIdentifier simId
    def repoName = 'MetadataValTest'

    def setup() {
        // Initialize V3 toolkit
        SimSupport.initialize()
        // Initialize V2 toolkit
        Installation.installation().warHome(Toolkit.warRootFile)
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimIdentifier(repoName, 'test')
        SimUtils.recreate('ebxml', simId)
    }

    def assertionGroup
    def transRunner

    def run(String submission) {
        Metadata metadata = MetadataParser.parseNonSubmission(submission)
        ValidationContext vc = DefaultValidationContextFactory.validationContext()
        vc.isPnR = true
        vc.isRequest = true
        def validationInterface = new UnconnectedRegistryValidation()
        Environment environment = Environment.getDefaultEnvironment()

        Closure closure = { SimHandle simHandle ->
            simHandle.event.addArtifact('Metadata', submission)
            new MetadataVal(simHandle, metadata, vc, environment, validationInterface).run()
        }
        transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()

        println "Failed assertions are ${transRunner.simHandle.event.errorAssertionIds()}"
    }

    def 'Good Submission'() {
        String submission = getClass().classLoader.getResource('PnR1Doc.xml').text
        when: run(submission)

        then:
        !transRunner.simHandle.event.hasErrors()
    }

    def 'Doc Missing'() {
        String submission = getClass().classLoader.getResource('PnR1Doc.xml').text
        def root = new XmlSlurper().parseText(submission)
        root.SubmitObjectsRequest[0].RegistryObjectList[0].ExtrinsicObject.replaceNode {}
        def outputBuilder = new StreamingMarkupBuilder()
        submission = outputBuilder.bind{ mkp.yield root }
        when: run(submission)

        then:
        transRunner.simHandle.event.hasErrors()
        transRunner.simHandle.event.errorAssertionIds().sort() == ['rosubstr060', 'rosubstr070'].sort()
    }

    def 'Assoc Missing'() {
        String submission = getClass().classLoader.getResource('PnR1Doc.xml').text
        def root = new XmlSlurper().parseText(submission)
        root.SubmitObjectsRequest[0].RegistryObjectList[0].Association.replaceNode {}
        def outputBuilder = new StreamingMarkupBuilder()
        submission = outputBuilder.bind{ mkp.yield root }

        when:
        run(submission)
        println transRunner.simHandle.event.errorAssertionIds

        then:
        transRunner.simHandle.event.hasErrors()
        transRunner.simHandle.event.errorAssertionIds().sort() == ['ssdehm010'].sort()
    }

    def 'SS Missing'() {
        String submission = getClass().classLoader.getResource('PnR1Doc.xml').text
        def root = new XmlSlurper().parseText(submission)
        root.SubmitObjectsRequest[0].RegistryObjectList[0].RegistryPackage.replaceNode {}
        def outputBuilder = new StreamingMarkupBuilder()
        submission = outputBuilder.bind{ mkp.yield root }
        when: run(submission)

        then:
        transRunner.simHandle.event.hasErrors()
        transRunner.simHandle.event.errorAssertionIds().sort() == ['rosubstr025', 'rosubstr060', 'rosubstr070'].sort()
    }

    def 'Doc only'() {
        String submission = getClass().classLoader.getResource('PnR1Doc.xml').text
        def root = new XmlSlurper().parseText(submission)

        root.SubmitObjectsRequest[0].RegistryObjectList[0].RegistryPackage.replaceNode {}
        root.SubmitObjectsRequest[0].RegistryObjectList[0].Association.replaceNode {}

        def outputBuilder = new StreamingMarkupBuilder()
        submission = outputBuilder.bind{ mkp.yield root }
        when: run(submission)

        then:
        transRunner.simHandle.event.hasErrors()
        transRunner.simHandle.event.errorAssertionIds().sort() == ['rosubstr025'].sort()
    }

}
