package gov.nist.hit.ds.dsSims.eb.metadataValidator.field
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.OidValidator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.object.RegistryObjectModel
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.utilities.xml.XmlUtil
import org.apache.axiom.om.OMElement

import javax.xml.namespace.QName
/**
 * Created by bmajur on 8/21/14.
 */
class TopAttsSubValidator extends ValComponentBase {
    ValComponentBase base
    ValidationContext vc
    List<String> statusValues
    RegistryObjectModel model

    static final String table416 = "ITI TF-3: Table 4.1-6";

    TopAttsSubValidator(ValComponentBase base, RegistryObjectModel model, ValidationContext vc, List<String> statusValues) {
        super(base.event)
        this.base = base
        this.vc = vc
        this.statusValues = statusValues
        this.model = model
        println "TopAtts"
        println model.toString()
    }

    // Guards
    def sqResponse() { vc.isSQ && vc.isResponse }
    def xc() { vc.isXC }
    def hasHome() { model.home }
    def hasStatus() { model.status != null }
    def hasVersionInfo() { XmlUtil.childrenWithLocalName(model.ro, "VersionInfo").size() > 0 }

    @Guard(methodNames = ['sqResponse'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='Top001', msg='availabilityStatus must be present', ref="ITI TF-3: Table 4.1-6")
    def statusCheck() {
        assertFalse(model.status == null)
    }

    @Guard(methodNames = ['sqResponse', 'hasStatus'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='Top002', msg='availabilityStatus must be properly coded', ref="ITI TF-3: Table 4.1-6")
    def statusValidation() {
        assertIn(statusValues, model.status)
    }

    @Guard(methodNames = ['sqResponse'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='Top003', msg='One VersionInfo attribute must be coded', ref="ITI TF-3: Table 4.1-6")
    def versionInfoCheck() {
        List<OMElement> versionInfos = XmlUtil.childrenWithLocalName(model.ro, "VersionInfo");
        assertEquals(1, versionInfos.size())
    }

    @Guard(methodNames = ['sqResponse', 'hasVersionInfo'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='Top004', msg='VersionInfo must have a value', ref="ITI TF-3: Table 4.1-6")
    def versionInfoValueCheck() {
        List<OMElement> versionInfos = XmlUtil.childrenWithLocalName(model.ro, "VersionInfo");
        def value = versionInfos[0].getAttribute(new QName('versionName')).getAttributeValue()
        assertHasValue(value)
    }

    @Guard(methodNames = ['sqResponse', 'xc'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='Top005', msg='homeCommunityId must be coded', ref="ITI TF-3: Table 4.1-6")
    def homeCheck() {
        if (!hasHome())
            fail('Missing')
    }

    @Guard(methodNames = ['sqResponse', 'xc', 'hasHome'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='Top005', msg='homeCommunityId length <= 64', ref="ITI TF-3: Table 4.1-6")
    def homeSizeCheck() {
        if (model.home.length() > 64)
            fail('Too long (max 64 chars', "Found ${model.home.length()}")
    }

    @Guard(methodNames = ['sqResponse', 'xc', 'hasHome'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='Top005', msg='homeCommunityId must OID based URN', ref="ITI TF-3: Table 4.1-6")
    def homeOIDCheck() {
        String[] parts = model.home.split(":")
        if (parts.length < 3 || !parts[0].equals("urn") || !parts[1].equals("oid"))
            fail('homeCommunityId must begin with urn:oid: prefix', model.home)
        new OidValidator(this).validate(parts[parts.length-1])
    }
}
