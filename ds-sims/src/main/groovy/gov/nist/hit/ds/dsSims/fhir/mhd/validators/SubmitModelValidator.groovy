package gov.nist.hit.ds.dsSims.fhir.mhd.validators
import gov.nist.hit.ds.dsSims.fhir.mhd.SubmitModel
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Setup
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
/**
 * Created by bmajur on 12/8/14.
 */
class SubmitModelValidator extends ValComponentBase {
    SimHandle simHandle
    SubmitModel submitModel

    SubmitModelValidator(SimHandle _simHandle, SubmitModel _submitModel) {
        super(_simHandle.event)
        simHandle = _simHandle
        submitModel = _submitModel
    }

    // for unit testing only
    SubmitModelValidator() {}

    @Setup
    @Validation(id="mhdsminit", msg="Setup", ref="")
    def setup() {
        simHandle.event.addArtifact('isXml', submitModel.isXml)
        simHandle.event.addArtifact('docRefIds', submitModel.docReferenceMap.keySet())
        simHandle.event.addArtifact('documentIds', submitModel.binaryMap.keySet())
    }

    @Validation(id='mhdsm010', msg='Single DocumentManifest present', ref=['3.65.4.1.2.1 FHIR encoding of a resource bundle'])
    def mhdsm010() { assertEquals(1, submitModel.docManifests?.size())}



    @Validation(id='mhdsm020', msg='One or more DocumentReference present', ref=['3.65.4.1.2.1 FHIR encoding of a resource bundle'])
    def mhmhdsm020() { assertMoreThan(0, submitModel.docReferenceMap.size()) }

    @Validation(id='mhdsm030', msg='DocumentReference.location links to has a Binary entry', ref=['3.65.4.1.2.1 FHIR encoding of a resource bundle'])
    def mhdsm030() {
        submitModel.docReferenceMap.keySet().each {
            infoFound("DocmentReference id ${it}")
            def dr = submitModel.docReferenceMap[it]
            infoFound("DocumentReference exists in bundle?")
            assertNotNull(dr)
            infoFound("DocumentReference.location is ${dr.location.@value.text()}")
            infoFound("entry.content.Binary resource with id ${dr.location.@value.text()} exists?")
            assertTrue(submitModel.binaryMap.containsKey(dr.location.@value.text()))
        }
    }

    @Validation(id='mhdsm040', msg='Each Binary attachment has a DocRef', ref=['3.65.4.1.2.1 FHIR encoding of a resource bundle'])
    def mhdsm040() {
        submitModel.binaryMap.keySet().each { binaryId ->
            infoFound("Binary resource with id ${binaryId}")
            assertNotNull(docRefs().find { dr -> dr.location.@value.text() == binaryId})
        }
    }

    def docRefs() {
        submitModel.docReferenceMap.values().asList()
    }

    @Validation(id='mhdsm050', msg='DocRef size/hash match Binary attachment', ref=['3.65.4.1.2.1 FHIR encoding of a resource bundle'])
    def mhdsm050() {
        submitModel.docReferenceMap.each { docRefId, dr ->
            infoFound("DocumentReference id ${docRefId}")
            String expectedHash = dr.hash.@value.text()
            def expectedSizeStr = dr.size.@value.text()
            def location = dr.location.@value.text()
            println "location is ${location}"
            def binaryResource = submitModel.binaryMap[location]
            if (binaryResource) {
                String base64String = binaryResource.text().trim()
                println "base64String is ${base64String}"
                byte[] base64 = base64String.getBytes()
                println "base64 is ${base64}"
                String text = new String(base64decode(base64))
                println "text is ${text}"
                infoFound('Compare hash')
                if (expectedHash) {
                    println "sha1 of document is ${DigestUtils.shaHex(text.getBytes())}"
                    assertEquals(expectedHash.toLowerCase(), DigestUtils.shaHex(text.getBytes()).toLowerCase())
                } else {
                    infoFound('No hash element to compare against')
                }
                infoFound('Compare size')
                if (expectedSizeStr) {
                    int expectedSize = Integer.parseInt(expectedSizeStr)
                    assertEquals(expectedSize, text.size())
                } else {
                    infoFound('No size element to compare against')
                }
            }
        }
    }

    @Validation(id='mhdsm060', msg='All Document References are referenced by the Manifest', ref=['3.65.4.1.2.1 FHIR encoding of a resource bundle'])
    def mhdsm060() {
        def manReferences = submitModel.docManifests[0]?.content.collect { it.reference.@value.text() }
        submitModel.docReferenceMap.keySet().each { assertIn(manReferences, it)}
    }

    @Validation(id='mhdsm070', msg='Bundle shall have a tag with value http://ihe.net/fhir/tag/iti-65', ref=['3.65.4.1.2.1 FHIR encoding of a resource bundle'])
    def mhdsm070() {
        infoFound("bundle has root of ${submitModel.bundle.name()}")
        def tag = submitModel.bundle.category.find {
            it.@scheme.text() == 'http://hl7.org/fhir/tag' }
        infoFound("Looking for catagory with scheme of http://hl7.org/fhir/tag")
        assertNotNull(tag)
        if (!tag) return
        assertEquals('http://ihe.net/fhir/tag/iti-65', tag.@term.text())
    }


    static byte[] base64decode(byte[] s) {
        return Base64.decodeBase64(s)
    }

    static byte[] base64encode(String s) {
        return Base64.encodeBase64String(s)
    }

}


