package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.shared.ValidationLevel
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import org.apache.log4j.BasicConfigurator
import spock.lang.Specification
/**
 * Created by bmajur on 1/5/15.
 */
class CxFormatValidatorTest extends Specification {
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
    def repoName = 'CxFormatValidatorTest'

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
        Closure closure = { SimHandle simHandle ->
            new CxFormatValidator(simHandle, 'context').validate(value)
        }
        transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.simHandle.event.addArtifact('Metadata', '')
        transRunner.runTest()

        assertionGroup = transRunner.simHandle.event.getAssertionGroup('TopLevel')
    }


    def 'Good'() {
        when: run('123^^^&1.2.3&ISO')

        then:
        !transRunner.simHandle.event.hasErrors()
    }

    def 'Missing ^'() {
        when: run('123^^&1.2.3&ISO')

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rocx010')
        assertionGroup.getFailedAssertions().size() == 1
    }

    def 'Extra ^'() {
        when: run('123^^^^&1.2.3&ISO')

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rocx010')
        assertionGroup.getFailedAssertions().size() == 1
    }

    def 'Extra ^ at end'() {
        when: run('123^^^&1.2.3&ISO^')

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rocx010')
        assertionGroup.getFailedAssertions().size() == 1
    }

    def 'Extra & at end'() {
        when: run('123^^^&1.2.3&ISO&')

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rocx010')
        assertionGroup.getFailedAssertions().size() == 1
    }

    def 'Not OID'() {
        when: run('123^^^&1.2,3&ISO')

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rocx010')
        assertionGroup.getFailedAssertions().size() == 1
    }

    def 'Extra parts'() {
        when: run('123^^a^&1.2.3&ISO')

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rocx010')
        assertionGroup.getFailedAssertions().size() == 1
    }

    def 'Extra parts 2'() {
        when: run('123^a^^&1.2.3&ISO')

        then:
        transRunner.simHandle.event.hasErrors()
        assertionGroup.assertionFailed('rocx010')
        assertionGroup.getFailedAssertions().size() == 1
    }
}
