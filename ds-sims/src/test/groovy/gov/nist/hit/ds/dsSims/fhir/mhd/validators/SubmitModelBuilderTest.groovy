package gov.nist.hit.ds.dsSims.fhir.mhd.validators

import gov.nist.hit.ds.dsSims.fhir.mhd.SubmitModel
import spock.lang.Specification

/**
 * Created by bmajur on 12/14/14.
 */
class SubmitModelBuilderTest extends Specification {
    def 'Should parse'() {
        def bundleText = '''
<feed xmlns="http://www.w3.org/2005/Atom">
    <entry>
        <content type="text/xml">
            <DocumentManifest xmlns="http://hl7.org/fhir">
            </DocumentManifest>
        </content>
    </entry>
    <entry>
        <id>0ff2318b-c524-42a4-bec4-f221c808133e</id>
        <content type="text/xml">
            <DocumentReference xmlns="http://hl7.org/fhir">
            </DocumentReference>
        </content>
    </entry>
    <entry>
        <id>documententry2</id>
        <content type="text/xml">
            <DocumentReference xmlns="http://hl7.org/fhir">
            </DocumentReference>
        </content>
    </entry>
    <entry>
        <id>document1</id>
        <content type="text/xml">
            <Binary xmlns="http://hl7.org/fhir" contentType="text/plain">
            </Binary>
        </content>
    </entry>
    <entry>
        <id>document2</id>
        <content type="text/xml">
            <Binary xmlns="http://hl7.org/fhir" contentType="text/plain">
            </Binary>
        </content>
    </entry>
</feed>'''

        setup: 'Parse bundle'
        def bundle = new XmlSlurper().parseText(bundleText)
        def feedTransaction = new FeedTransaction(null)

        when: 'Build model'
        SubmitModel sm = feedTransaction.buildSubmitModel(true, bundle)

        then:
        sm.docManifests.size() == 1
        sm.docReferenceMap.keySet().size() == 2
        sm.binaryMap.keySet().size() == 2

        sm.docManifests[0].name() == 'DocumentManifest'

        sm.docReferenceMap['0ff2318b-c524-42a4-bec4-f221c808133e'].name() == 'DocumentReference'
        sm.docReferenceMap['documententry2'].name() == 'DocumentReference'

        sm.binaryMap['document1'].name() == 'Binary'
        sm.binaryMap['document2'].name() == 'Binary'
    }
}
