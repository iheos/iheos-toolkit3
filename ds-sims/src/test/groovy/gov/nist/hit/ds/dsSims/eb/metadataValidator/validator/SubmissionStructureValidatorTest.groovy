package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.reg.UnconnectedRegistryValidation
import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.shared.ValidationLevel
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import org.apache.axiom.om.OMElement
import spock.lang.Specification

/**
 * Created by bmajur on 2/12/15.
 */

// TODO: Need tests for many more submission patterns
class SubmissionStructureValidatorTest extends Specification {
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
    def repoName = 'SubmissionStructureValidatorTest'
    Metadata m

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        Event.defaultValidationLevel = ValidationLevel.INFO
        simId = new SimIdentifier(repoName, 'test')
        SimUtils.recreate('ebxml', simId)
        m = new Metadata()
        vc = new ValidationContext()
        vc.isR = true
        vc.isRequest = true
        rvi = new UnconnectedRegistryValidation()
    }

    def transRunner
    def de
    def assocs
    ValidationContext vc
    RegistryValidationInterface rvi
    Event event

    def run() {
        Closure closure = { SimHandle simHandle ->
            new SubmissionStructureValidator(simHandle, vc, m, rvi).run()
        }
        transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.simHandle.event.addArtifact('Metadata', m.format())
        event = transRunner.simHandle.event
        transRunner.runTest()
    }

    def 'No SubmissionSet'() {
        when: run()
        then: event.assertionFailed('rosubstr025')

    }

    def 'Submission must have objects beyond SubmissionSet'() {
        setup:
        m.mkSubmissionSet('SS')

        when: run()

        then: event.assertionFailed('rosubstr022')
    }

    def 'RegistryPackage not a SS or Fol' () {
        setup:
        m.mkRegistryPackage('RP')

        when: run()

        then: event.assertionFailed('rosubstr110')
    }

    def 'No symbolic references to objects not in submission'() {
        setup:
        m.mkSubmissionSet('SS')
        m.mkAssociation('HasMember', 'SS', 'DE')

        when: run()

        then: event.hasErrors()
    }

    def 'Original DE in submission is good'() {
        setup:
        m.mkSubmissionSet('SS')
        OMElement hm = m.mkAssociation('HasMember', 'SS', 'DE')
        m.addSlot(hm, 'SubmissionSetStatus', 'Original')
        m.mkExtrinsicObject('DE', 'text/plain')

        when: run()

        then: !event.hasErrors()
    }

    def 'Original DE not in submission is bad'() {
        setup:
        m.mkSubmissionSet('SS')
        def hm = m.mkAssociation('HasMember', 'SS', 'urn:uuid:45')
        m.addSlot(hm, 'SubmissionSetStatus', 'Original')

        when: run()

        then: event.assertionFailed('rosubstr070')
    }

    def 'Reference DE not in submission'() {
        setup:
        m.mkSubmissionSet('SS')
        def hm = m.mkAssociation('HasMember', 'SS', 'urn:uuid:8')
        m.addSlot(hm, 'SubmissionSetStatus', 'Reference')

        when: run()

        then: !event.hasErrors()
    }

    def 'Reference DE in submission'() {
        setup:
        m.mkSubmissionSet('SS')
        def hm = m.mkAssociation('HasMember', 'SS', 'Doc1')
        m.addSlot(hm, 'SubmissionSetStatus', 'Reference')

        when: run()

        then: event.assertionFailed('rosubstr070')
    }

    def 'DE must be connected to SS'() {
        setup:
        m.mkExtrinsicObject('DE', 'text/plain')
        def hm = m.mkAssociation('HasMember', 'SS', 'DE')
        m.addSlot(hm, 'SubmissionSetStatus', 'Original')

        when: run()

        then: event.assertionFailed('rosubstr070')
    }

    def 'DE and no SS'() {
        setup:
        m.mkExtrinsicObject('DE', 'text/plain')

        when: run()

        then: event.assertionFailed('rosubstr070')
    }
}
