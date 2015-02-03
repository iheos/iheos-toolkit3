package gov.nist.hit.ds.dsSims.fhir
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.dsSims.fhir.mhd.validators.MhdDocRefValidator
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification
/**
 * Created by bmajur on 11/16/14.
 */
class MhdDocRefTest extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction name="Register" code="rb" asyncCode="r.as">
       <implClass value="RegisterTransaction"/>
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <params multiPart="false" soap="true"/>
    </transaction>
    <actor displayName="Document Registry" id="mhd"
      class="">
        <transaction id="rb"/>
    </actor>
</ActorsTransactions>
'''

    File repoDataDir
    RepositorySource repoSource
    SimIdentifier simId
    def repoName = 'MhdDocRefTest'
    ActorTransactionTypeFactory factory = new ActorTransactionTypeFactory()
    TransactionType ttype
    def actorTypeString = 'mhd'

    def setup() {
        SimSupport.initialize()
        factory.clear()
        factory.loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimIdentifier(repoName, 'test')
        SimUtils.create(actorTypeString, simId)
    }

    def 'Should collect two authenticator ref values'() {
        def text = '''
<DocumentReference>
    <contained>
        <!-- DocumentEntry.legalAuthenticator
            V2 XCN encoding equivalent is
            ^Welby^Marcus^^^Dr^MD
            -->
        <Practitioner id="auth1">
            <name>
                <family value="Welby"/>
                <given value="Marcus"/>
                <prefix value="Dr"/>
            </name>
        </Practitioner>
    </contained>
    <contained>
        <!-- DocumentEntry.legalAuthenticator
            V2 XCN encoding equivalent is
            ^Welby^Marcus^^^Dr^MD
            -->
        <Practitioner id="auth2">
            <name>
                <family value="Welby"/>
                <given value="Marcus"/>
                <prefix value="Dr"/>
            </name>
        </Practitioner>
    </contained>
    <authenticator>
        <reference value="#auth1"/>
    </authenticator>
    <authenticator>
        <reference value="#auth2"/>
    </authenticator>
</DocumentReference>
'''

        when:
        def xml = new XmlSlurper().parseText(text)
        def validator = new MhdDocRefValidator(SimUtils.create(actorTypeString, new SimIdentifier(SimUtils.defaultRepoName, 'tmp')), xml)

        then:
        validator.authenticatorRefValues() == ['#auth1', '#auth2']
        validator.authenticatorTags() == ['auth1', 'auth2']
        validator.containedPractitioners().size() == 2
        validator.containedPractitioners('auth1').size() == 1
    }

    def 'Should collect ome sourcepatient element'() {
        def text = '''
<DocumentReference>
    <extension url="http://ihe.net/fhir/Profile/XDS/extensions#sourcePatient">
        <valueResource>
            <reference value="#sourcepatient"/>
        </valueResource>
    </extension>
    <contained>
        <!-- this could contain the full sourcePatientInfo detail
             referenced by <subject/> DocumentEntry.patientId
             this is the Source patientID -->
        <Patient id="sourcepatient">
            <identifier>
                <system value="urn:oid:7.7.7"/>
                <value value="DTG1234"/>
            </identifier>
            <name>
                <family value="Brown"/>
                <given value="Charlie"/>
            </name>
            <gender>
                <coding>
                    <code value="M">
                    </code>
                </coding>
            </gender>
            <birthDate value="1951-08-24T09:35:00"/>
            <address>
                <line value="100 Main St"/>
                <city value="Burlington"/>
                <state value="MA"/>
                <zip value="01803"></zip>
                <country value="USA"/>
            </address>
        </Patient>
    </contained>
</DocumentReference>
'''
        when:
        def xml = new XmlSlurper().parseText(text)
        def validator = new MhdDocRefValidator(SimUtils.create(actorTypeString, new SimIdentifier(SimUtils.defaultRepoName, 'tmp')), xml)

        then:
        validator.sourcePatientExtensions().size() == 1
        validator.sourcePatientRefValues().size() == 1
        validator.sourcePatientRefTags().size() == 1
        validator.sourcePatients().size() == 1
        validator.sourcePatients().first().name() == 'Patient'
    }

    def 'ClassCode test'() {
        def text = getClass().getResource('/mhd/full_docref.xml').text

        when:
        def xml = new XmlSlurper().parseText(text)
        Closure closure = { simHandle ->
            new MhdDocRefValidator(simHandle, xml).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.simHandle.event.addArtifact('Metadata', '')
        transRunner.runTest()

        then:
        transRunner.simHandle.event.getAssertions('mhd140')
        transRunner.simHandle.event.getAssertions('mhd150')
        transRunner.simHandle.event.getAssertions('mhd190')
        transRunner.simHandle.event.getAssertions('mhd200')
        transRunner.simHandle.event.getAssertions('mhd210')
        transRunner.simHandle.event.getAssertions('mhd220')
        transRunner.simHandle.event.getAssertions('mhd230')
        transRunner.simHandle.event.getAssertions('mhdmi040')
        transRunner.simHandle.event.getAssertions('fhirpract010')
        transRunner.simHandle.event.getAssertions('fhirpatient010')
        !transRunner.simHandle.event.hasErrors()
    }

}
