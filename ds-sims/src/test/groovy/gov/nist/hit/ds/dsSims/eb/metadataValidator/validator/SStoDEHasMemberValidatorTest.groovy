package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataSupport
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
class SStoDEHasMemberValidatorTest extends Specification {
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
    def repoName = 'SStoDEHasMemberValidatorTest'
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
    }

    def transRunner
    def de
    def assocs
    Event event

    def run() {
        Closure closure = { SimHandle simHandle ->
            new SStoDEHasMemberValidator(simHandle, m, de, assocs).run()
        }
        transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.simHandle.event.addArtifact('Metadata', m.format())
        event = transRunner.simHandle.event
        transRunner.runTest()
    }

    def 'No associations'() {
        setup:
        de = m.mkExtrinsicObject('Doc1', 'text/xml')
        assocs = []

        when: run()

        then: event.assertionFailed('ssdehm010')
    }

    def 'No HasMember slots'() {
        setup:
        de = m.mkExtrinsicObject('Doc1', 'text/xml')
        def assoc = m.mkAssociation(MetadataSupport.assoctype_has_member, 'SS', 'Doc1')
        assocs = [assoc]

        when: run()

        then: event.assertionFailed('ssdehm020')
    }

    def 'Not Original'() {
        setup:
        de = m.mkExtrinsicObject('Doc1', 'text/xml')
        OMElement assoc = m.mkAssociation(MetadataSupport.assoctype_has_member, 'SS', 'Doc1')
        m.addSlot(assoc, 'SubmissionSetStatus', 'NotOriginal')
        assocs = [assoc]

        when: run()

        then: event.assertionFailed('ssdehm030')
    }

    def 'Good'() {
        setup:
        de = m.mkExtrinsicObject('Doc1', 'text/xml')
        OMElement assoc = m.mkAssociation(MetadataSupport.assoctype_has_member, 'SS', 'Doc1')
        m.addSlot(assoc, 'SubmissionSetStatus', 'Original')
        assocs = [assoc]

        when: run()
        println event.errorAssertionIds()
        println m.toString()

        then: !event.hasErrors()
    }
}
