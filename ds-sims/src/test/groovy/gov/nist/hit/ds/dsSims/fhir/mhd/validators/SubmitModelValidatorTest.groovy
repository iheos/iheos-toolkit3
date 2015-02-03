package gov.nist.hit.ds.dsSims.fhir.mhd.validators
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.dsSims.fhir.mhd.SubmitModel
import gov.nist.hit.ds.eventLog.testSupport.EventAccess
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimEventAccess
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification
/**
 * Created by bmajur on 12/14/14.
 */
class SubmitModelValidatorTest extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction name="MHD" id="mh" code="mh">
       <implClass value="NotUsed"/>
        <request action="NotUsed"/>
        <response action="NotUsed"/>
    </transaction>
    <actor name="NotUsed" id="NotUsed">
      <impllass value=""/>
        <transaction id="mh"/>
    </actor>
</ActorsTransactions>
'''

    def feedHeader = '''
Content-Type: application/atom+xml

'''

    def feed = '''
<feed xmlns="http://www.w3.org/2005/Atom">
    <title>example submission bundle</title>
    <id>urn:uuid:f5207893-3074-4d38-8c83-0fdbfd79bdc0</id>
    <category scheme="http://hl7.org/fhir/tag" term="http://ihe.net/fhir/tag/iti-65"/>
    <entry>
        <title>title</title>
        <content type="text/xml">
            <DocumentManifest xmlns="http://hl7.org/fhir">
                <content>
                    <reference value="0ff2318b-c524-42a4-bec4-f221c808133e"/>
                </content>
                <content>
                    <reference value="documententry2"/>
                </content>
            </DocumentManifest>
        </content>
    </entry>
    <entry>
        <title>example DocumentEntry</title>
        <id>0ff2318b-c524-42a4-bec4-f221c808133e</id>
        <content type="text/xml">
            <DocumentReference xmlns="http://hl7.org/fhir">
                <mimeType value="text/plain"/>
                <size value="11"/>
                <hash value="A2E2298974C2BE5A6E2C635247210C2CFCB42A94"/>
                <location value="document1"/>
            </DocumentReference>
        </content>
    </entry>
    <entry>
        <title>example DocumentEntry</title>
        <id>documententry2</id>
        <content type="text/xml">
            <DocumentReference xmlns="http://hl7.org/fhir">
                <mimeType value="text/plain"/>
                <size value="11"/>
                <hash value="805F6F66D093A19E589E3500EEBE2165A6BC51FC"/>
                <location value="document2"/>
            </DocumentReference>
        </content>
    </entry>
    <entry>
        <title>example DocumentEntry</title>
        <id>document1</id>
        <content type="text/xml">
            <Binary xmlns="http://hl7.org/fhir" contentType="text/plain">
                TUhEIG9uIEZISVI=
            </Binary>
        </content>
    </entry>
    <entry>
        <title>example DocumentEntry</title>
        <id>document2</id>
        <content type="text/xml">
            <Binary xmlns="http://hl7.org/fhir" contentType="text/plain">
                TUhEIFRlc3Rpbmc=
            </Binary>
        </content>
    </entry>
</feed>'''

    File repoDataDir
    RepositorySource repoSource
    SimIdentifier simId
    String repoName = 'sim'

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimIdentifier(SimUtils.defaultRepoName, 'SubmitModelValidatorTest')
        SimUtils.recreate('NotUsed', simId)
    }

    def 'Test contentType parsing'() {
        setup: 'Build validator'
        SubmitHeaderValidator val = new SubmitHeaderValidator()

        when: 'parser run'
        String contentType = val.contentType(feedHeader)

        then: 'Content type is application/atom+xml'
        contentType == 'application/atom+xml'
    }

    def 'Test Good Validation'() {
        when:
        Closure closure = { SimHandle simHandle ->
            FeedTransaction ft = new FeedTransaction((simHandle))
            SubmitModel sm = ft.buildSubmitModel(true, new String(simHandle.event.inOut.reqBody))
            new SubmitModelValidator(simHandle, sm).asPeer().run()
        }
        def transRunner = new TransactionRunner('mh', simId, closure)
        transRunner.simHandle.event.inOut.reqHdr = feedHeader
        transRunner.simHandle.event.inOut.reqBody = feed.getBytes()
        def eventAccess = new SimEventAccess(simId, transRunner.simHandle.event)
        transRunner.runTest()

        then:
        !transRunner.simHandle.event.hasErrors()
    }

    def emptyFeed = '''
<feed xmlns="http://www.w3.org/2005/Atom">
</feed>
'''

    def 'Test Empty Validation'() {
        when:
        Closure closure = { SimHandle simHandle ->
            FeedTransaction ft = new FeedTransaction((simHandle))
            SubmitModel sm = ft.buildSubmitModel(true, new String(simHandle.event.inOut.reqBody))
            new SubmitModelValidator(simHandle, sm).asPeer().run()
        }
        def transRunner = new TransactionRunner('mh', simId, closure)
        transRunner.simHandle.event.inOut.reqHdr = feedHeader
        transRunner.simHandle.event.inOut.reqBody = emptyFeed.getBytes()
        transRunner.runTest()
        def errors = transRunner.simHandle.event.errorAssertionIds()

        then:
        //transRunner.simHandle.event.hasErrors()
        errors == ['mhdsm010', 'mhdsm020', 'mhdsm070'] as Set
    }

    def manifestOnlyFeed = '''
<feed xmlns="http://www.w3.org/2005/Atom">
    <title>example submission bundle</title>
    <id>urn:uuid:f5207893-3074-4d38-8c83-0fdbfd79bdc0</id>
    <category scheme="http://hl7.org/fhir/tag" term="http://ihe.net/fhir/tag/iti-65"/>
    <entry>
        <title>title</title>
        <content type="text/xml">
            <DocumentManifest xmlns="http://hl7.org/fhir">
                <content id="0ff2318b-c524-42a4-bec4-f221c808133e"/>
                <content id="documententry2"/>
            </DocumentManifest>
        </content>
    </entry>
</feed>'''

    def 'Test Manifest only feed'() {
        when:
        Closure closure = { SimHandle simHandle ->
            FeedTransaction ft = new FeedTransaction((simHandle))
            SubmitModel sm = ft.buildSubmitModel(true, new String(simHandle.event.inOut.reqBody))
            new SubmitModelValidator(simHandle, sm).asPeer().run()
        }
        def transRunner = new TransactionRunner('mh', simId, closure)
        transRunner.simHandle.event.inOut.reqHdr = feedHeader
        transRunner.simHandle.event.inOut.reqBody = manifestOnlyFeed.getBytes()
        transRunner.runTest()
        def errors = transRunner.simHandle.event.errorAssertionIds()

        then:
        errors == ['mhdsm020'] as Set
    }

    def docRefOnlyfeed = '''
<feed xmlns="http://www.w3.org/2005/Atom">
    <title>example submission bundle</title>
    <id>urn:uuid:f5207893-3074-4d38-8c83-0fdbfd79bdc0</id>
    <category scheme="http://hl7.org/fhir/tag" term="http://ihe.net/fhir/tag/iti-65"/>
    <entry>
        <title>example DocumentEntry</title>
        <id>0ff2318b-c524-42a4-bec4-f221c808133e</id>
        <content type="text/xml">
            <DocumentReference xmlns="http://hl7.org/fhir">
                <mimeType value="text/plain"/>
                <size value="11"/>
                <hash value="A2E2298974C2BE5A6E2C635247210C2CFCB42A94"/>
                <location value="document1"/>
            </DocumentReference>
        </content>
    </entry>
</feed>'''

    def 'Test docRef only feed'() {
        when:
        Closure closure = { SimHandle simHandle ->
            FeedTransaction ft = new FeedTransaction((simHandle))
            SubmitModel sm = ft.buildSubmitModel(true, new String(simHandle.event.inOut.reqBody))
            new SubmitModelValidator(simHandle, sm).asPeer().run()
        }
        def transRunner = new TransactionRunner('mh', simId, closure)
        transRunner.simHandle.event.inOut.reqHdr = feedHeader
        transRunner.simHandle.event.inOut.reqBody = docRefOnlyfeed.getBytes()
        transRunner.runTest()
        def errors = transRunner.simHandle.event.errorAssertionIds()

        then:
        errors == ['mhdsm010', 'mhdsm030', 'mhdsm060'] as Set
    }

    def missingBinaryFeed = '''
<feed xmlns="http://www.w3.org/2005/Atom">
    <title>example submission bundle</title>
    <id>urn:uuid:f5207893-3074-4d38-8c83-0fdbfd79bdc0</id>
    <category scheme="http://hl7.org/fhir/tag" term="http://ihe.net/fhir/tag/iti-65"/>
    <entry>
        <title>title</title>
        <content type="text/xml">
            <DocumentManifest xmlns="http://hl7.org/fhir">
                <content>
                    <reference value="0ff2318b-c524-42a4-bec4-f221c808133e"/>
                </content>
                <content>
                    <reference value="documententry2"/>
                </content>
            </DocumentManifest>
        </content>
    </entry>
    <entry>
        <title>example DocumentEntry</title>
        <id>0ff2318b-c524-42a4-bec4-f221c808133e</id>
        <content type="text/xml">
            <DocumentReference xmlns="http://hl7.org/fhir">
                <mimeType value="text/plain"/>
                <size value="11"/>
                <hash value="A2E2298974C2BE5A6E2C635247210C2CFCB42A94"/>
                <location value="document1"/>
            </DocumentReference>
        </content>
    </entry>
    <entry>
        <title>example DocumentEntry</title>
        <id>documententry2</id>
        <content type="text/xml">
            <DocumentReference xmlns="http://hl7.org/fhir">
                <mimeType value="text/plain"/>
                <size value="11"/>
                <hash value="805F6F66D093A19E589E3500EEBE2165A6BC51FC"/>
                <location value="document2"/>
            </DocumentReference>
        </content>
    </entry>
    <entry>
        <title>example DocumentEntry</title>
        <id>document1</id>
        <content type="text/xml">
            <Binary xmlns="http://hl7.org/fhir" contentType="text/plain">
                TUhEIG9uIEZISVI=
            </Binary>
        </content>
    </entry>
</feed>'''

    def 'Test missingBinaryFeed'() {
        when:
        Closure closure = { SimHandle simHandle ->
            FeedTransaction ft = new FeedTransaction((simHandle))
            SubmitModel sm = ft.buildSubmitModel(true, new String(simHandle.event.inOut.reqBody))
            new SubmitModelValidator(simHandle, sm).asPeer().run()
        }
        def transRunner = new TransactionRunner('mh', simId, closure)
        transRunner.simHandle.event.inOut.reqHdr = feedHeader
        transRunner.simHandle.event.inOut.reqBody = missingBinaryFeed.getBytes()
        transRunner.runTest()
        def errors = transRunner.simHandle.event.errorAssertionIds()

        then:
        errors == ['mhdsm030'] as Set
    }

    def missingEntryWrappers = '''
<feed xmlns="http://www.w3.org/2005/Atom">
    <title>example submission bundle</title>
    <id>urn:uuid:f5207893-3074-4d38-8c83-0fdbfd79bdc0</id>
    <category scheme="http://hl7.org/fhir/tag" term="http://ihe.net/fhir/tag/iti-65"/>
        <title>title</title>
        <content type="text/xml">
            <DocumentManifest xmlns="http://hl7.org/fhir">
                <content id="0ff2318b-c524-42a4-bec4-f221c808133e"/>
                <content id="documententry2"/>
            </DocumentManifest>
        </content>
        <title>example DocumentEntry</title>
        <id>0ff2318b-c524-42a4-bec4-f221c808133e</id>
        <content type="text/xml">
            <DocumentReference xmlns="http://hl7.org/fhir">
                <mimeType value="text/plain"/>
                <size value="11"/>
                <hash value="A2E2298974C2BE5A6E2C635247210C2CFCB42A94"/>
                <location value="document1"/>
            </DocumentReference>
        </content>
        <title>example DocumentEntry</title>
        <id>documententry2</id>
        <content type="text/xml">
            <DocumentReference xmlns="http://hl7.org/fhir">
                <mimeType value="text/plain"/>
                <size value="11"/>
                <hash value="805F6F66D093A19E589E3500EEBE2165A6BC51FC"/>
                <location value="document2"/>
            </DocumentReference>
        </content>
        <title>example DocumentEntry</title>
        <id>document1</id>
        <content type="text/xml">
            <Binary xmlns="http://hl7.org/fhir" contentType="text/plain">
                TUhEIG9uIEZISVI=
            </Binary>
        </content>
        <title>example DocumentEntry</title>
        <id>document2</id>
        <content type="text/xml">
            <Binary xmlns="http://hl7.org/fhir" contentType="text/plain">
                TUhEIFRlc3Rpbmc=
            </Binary>
        </content>
</feed>'''

    def 'Test missingEntryWrappers'() {
        when:
        Closure closure = { SimHandle simHandle ->
            FeedTransaction ft = new FeedTransaction((simHandle))
            SubmitModel sm = ft.buildSubmitModel(true, new String(simHandle.event.inOut.reqBody))
            new SubmitModelValidator(simHandle, sm).asPeer().run()
        }
        def transRunner = new TransactionRunner('mh', simId, closure)
        transRunner.simHandle.event.inOut.reqHdr = feedHeader
        transRunner.simHandle.event.inOut.reqBody = missingEntryWrappers.getBytes()
        transRunner.runTest()
        def errors = transRunner.simHandle.event.errorAssertionIds()

        then:
        errors == ['mhdsm010', 'mhdsm020'] as Set
    }

    def badContentRef = '''
<feed xmlns="http://www.w3.org/2005/Atom">
    <title>example submission bundle</title>
    <id>urn:uuid:f5207893-3074-4d38-8c83-0fdbfd79bdc0</id>
    <category scheme="http://hl7.org/fhir/tag" term="http://ihe.net/fhir/tag/iti-65"/>
    <entry>
        <title>title</title>
        <content type="text/xml">
            <DocumentManifest xmlns="http://hl7.org/fhir">
                <content id="xxx0ff2318b-c524-42a4-bec4-f221c808133e"/>
                <content id="documententry2"/>
            </DocumentManifest>
        </content>
    </entry>
    <entry>
        <title>example DocumentEntry</title>
        <id>0ff2318b-c524-42a4-bec4-f221c808133e</id>
        <content type="text/xml">
            <DocumentReference xmlns="http://hl7.org/fhir">
                <mimeType value="text/plain"/>
                <size value="11"/>
                <hash value="A2E2298974C2BE5A6E2C635247210C2CFCB42A94"/>
                <location value="document1"/>
            </DocumentReference>
        </content>
    </entry>
    <entry>
        <title>example DocumentEntry</title>
        <id>documententry2</id>
        <content type="text/xml">
            <DocumentReference xmlns="http://hl7.org/fhir">
                <mimeType value="text/plain"/>
                <size value="11"/>
                <hash value="805F6F66D093A19E589E3500EEBE2165A6BC51FC"/>
                <location value="document2"/>
            </DocumentReference>
        </content>
    </entry>
    <entry>
        <title>example DocumentEntry</title>
        <id>document1</id>
        <content type="text/xml">
            <Binary xmlns="http://hl7.org/fhir" contentType="text/plain">
                TUhEIG9uIEZISVI=
            </Binary>
        </content>
    </entry>
    <entry>
        <title>example DocumentEntry</title>
        <id>document2</id>
        <content type="text/xml">
            <Binary xmlns="http://hl7.org/fhir" contentType="text/plain">
                TUhEIFRlc3Rpbmc=
            </Binary>
        </content>
    </entry>
</feed>'''

    def 'Test badContentRef'() {
        when:
        Closure closure = { SimHandle simHandle ->
            FeedTransaction ft = new FeedTransaction((simHandle))
            SubmitModel sm = ft.buildSubmitModel(true, new String(simHandle.event.inOut.reqBody))
            new SubmitModelValidator(simHandle, sm).asPeer().run()
        }
        def transRunner = new TransactionRunner('mh', simId, closure)
        transRunner.simHandle.event.inOut.reqHdr = feedHeader
        transRunner.simHandle.event.inOut.reqBody = badContentRef.getBytes()
        transRunner.runTest()
        def errors = transRunner.simHandle.event.errorAssertionIds()

        then:
        errors == ['mhdsm060'] as Set
    }

}
