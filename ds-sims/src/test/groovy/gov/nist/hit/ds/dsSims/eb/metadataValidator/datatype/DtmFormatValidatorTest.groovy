package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.shared.ValidationLevel
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.simulator.SimIdentifier
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification

/**
 * Created by bmajur on 1/5/15.
 */
class DtmFormatValidatorTest extends Specification {
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
    def repoName = 'DtmFormatValidatorTest'

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        Event.defaultValidationLevel = ValidationLevel.INFO
        simId = new SimIdentifier(repoName, 'test')
        SimUtils.recreate('ebxml', simId)
    }

    def assertionGroup
    def transRunner

    def run(value) {
        Closure closure = { simHandle ->
            new DtmFormatValidator(simHandle, 'context').validate(value)
        }
        transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.simHandle.event.addArtifact('Metadata', '')
        transRunner.runTest()

        assertionGroup = transRunner.simHandle.event.getAssertionGroup('TopLevel')
        println "Failed assertions are ${assertionGroup.errorAssertionIds()}"
    }

    def 'Year only'() {
        when: run('1984')

        then:
        !transRunner.simHandle.event.hasErrors()
    }

    def 'Day'() {
        when: run('19840223')

        then:
        !transRunner.simHandle.event.hasErrors()
    }

    def 'Full'() {
        when: run('20050102030405')

        then:
        !transRunner.simHandle.event.hasErrors()
    }

    def 'Alpha'() {
        when: run('20f05010203040')

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rodtm030')
        assertionGroup.assertionFailed('rodtm020')
        assertionGroup.getFailedAssertions().size() == 2
    }

    def 'Spaces'() {
        when: run('20050102030405 ')
        println "Failed assertions are ${assertionGroup.getFailedAssertions()}"

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rodtm030')
        assertionGroup.assertionFailed('rodtm020')
        assertionGroup.getFailedAssertions().size() == 2
    }

    def 'Bad Month'() {
        when: run('20051302030405')
        println "Failed assertions are ${assertionGroup.getFailedAssertions()}"

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rodtm050')
        assertionGroup.getFailedAssertions().size() == 1
    }

    def 'Bad Day'() {
        when: run('20051232030405')
        println "Failed assertions are ${assertionGroup.getFailedAssertions()}"

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rodtm060')
        assertionGroup.getFailedAssertions().size() == 1
    }

    def 'Bad Hour'() {
        when: run('20051231330405')
        println "Failed assertions are ${assertionGroup.getFailedAssertions()}"

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rodtm070')
        assertionGroup.getFailedAssertions().size() == 1
    }

    def 'Bad Minute'() {
        when: run('20051231037405')
        println "Failed assertions are ${assertionGroup.getFailedAssertions()}"

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rodtm080')
        assertionGroup.getFailedAssertions().size() == 1
    }

    def 'Bad Second'() {
        when: run('20051231030495')
        println "Failed assertions are ${assertionGroup.getFailedAssertions()}"

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rodtm090')
        assertionGroup.getFailedAssertions().size() == 1
    }
}
