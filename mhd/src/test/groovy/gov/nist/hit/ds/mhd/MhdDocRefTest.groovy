package gov.nist.hit.ds.mhd
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
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
       <implClass value="gov.nist.hit.ds.dsSims.transactions.RegisterTransaction"/>
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
    SimId simId
    def repoName = 'MhdDocRefTest'

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimId('test')
        SimUtils.create('mhd', simId, repoName)
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
</DocumentReference>'''

        when:
        def xml = new XmlSlurper().parseText(text)
        def validator = new MhdDocRefValidator(SimUtils.create(new SimId('tmp')), xml)

        then:
        validator.authenticatorRefValues() == ['#auth1', '#auth2']
        validator.authenticatorTags() == ['auth1', 'auth2']
        validator.containedPractitioners().size() == 2
        validator.containedPractitioners('auth1').size() == 1
    }

    def 'ClassCode test'() {
        def text1 = '''
<DocumentReference>
    <class>
        <coding>
            <system value="urn:oid:1.3.6.1.4.1.21367.100.1"/>
            <code value="DEMO-Pt Mgmt"/>
            <display value="Patient Management"/>
        </coding>
    </class>
</DocumentReference>'''

        def text = getClass().getResource('/mhd/full_docref.xml').text

        when:
        def xml = new XmlSlurper().parseText(text)
        Closure closure = { simHandle ->
            new MhdDocRefValidator(simHandle, xml).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, repoName, closure)
        transRunner.simHandle.event.addArtifact('Metadata', '')
        transRunner.runTest()
        println transRunner.simHandle.event.getAssertions('mhd140')

        then:
        transRunner.simHandle.event.getAssertions('mhd140')
        transRunner.simHandle.event.getAssertions('fhirpract010')
        !transRunner.simHandle.event.hasErrors()
    }

}
