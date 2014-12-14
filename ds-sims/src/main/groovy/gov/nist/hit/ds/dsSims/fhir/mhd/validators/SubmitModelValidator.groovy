package gov.nist.hit.ds.dsSims.fhir.mhd.validators

import gov.nist.hit.ds.dsSims.fhir.mhd.SubmitModel
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang.StringUtils

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

    @Validation(id='sm010', msg='Single Manifest present', ref='')
    def sm010() { assertEquals(1, submitModel.docManifests?.size())}

    @Validation(id='sm020', msg='One or more DocRef present', ref='')
    def sm020() { assertTrue(submitModel.docReferenceMap.size() > 0)}

    @Validation(id='sm030', msg='Each DocRef has a Binary attachment', ref='')
    def sm030() {
        submitModel.docReferenceMap.keySet().each {
            infoFound("id ${it}")
            assertTrue(submitModel.binaryMap.containsKey(it))
        }
    }

    @Validation(id='sm040', msg='Each Binary attachment has a DocRef', ref='')
    def sm040() {
        submitModel.binaryMap.keySet().each {
            infoFound("id ${it}")
            assertTrue(submitModel.docReferenceMap.containsKey(it))
        }
    }

    @Validation(id='sm050', msg='DocRef size/hash match Binary attachment', ref='')
    def sm050() {
        submitModel.docReferenceMap.each { id, dr ->
            infoFound("id ${id}")
            def expectedHash = dr.hash.@value.text()
            def expectedSizeStr = dr.size.@value.text()
            def base64 = submitModel.binaryMap[id]
            if (!base64) return
            def text = base64decode(base64)
            if (expectedHash) {
                assertEquals(expectedHash, DigestUtils.sha1Hex(text))
            }
            if (expectedSizeStr) {
                int expectedSize = Integer.parseInt(expectedSizeStr)
                assertEquals(expectedSize, text.size())
            }
        }
    }

    @Validation(id='sm060', msg='All DocRef referenced by Manifest', ref='')
    def sm060() {
        def manReferences = submitModel.docManifest.content.collect { it.@id.text() }
        submitModel.docReferenceMap.keySet().each { assertIn(manReferences, it)}
    }

    String base64decode(String s) {
        return StringUtils.newStringUtf8(Base64.decodeBase64(s));
    }

    String base64encode(String s) {
        return Base64.encodeBase64String(StringUtils.getBytesUtf8(s));
    }

}


