package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataUtilities
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 2/12/15.
 */
class SStoDEHasMemberValidator extends ValComponentBase {
    Metadata m
    OMElement doc    // documententry
    List<OMElement> assocs // all the associations that link the above documententry to the SS
    @Delegate MetadataUtilities metadataUtilities

    SStoDEHasMemberValidator(Metadata _m, OMElement _doc, List<OMElement> _assocs) {
        m = _m
        doc = _doc
        assocs = _assocs
        metadataUtilities = new MetadataUtilities(m)
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ssdehm010', msg='Single SubmissionSet to DocumentEntry HasMember association', ref='ITI TF-3: 4.1.4.1')
    def ssdehm010() {
        if (assocs.isEmpty()) {
            fail("${docEntryTag(doc)} is not linked to the SubmissionSet with an ${assocTypeTag('HasMember')}")
        } else if (assocs.size() > 1) {
            fail("${docEntryTag(doc)} is linked to the SubmissionSet with multiple ${assocTypeTag('HasMember')}")
        } else {
            expected("HasMember association")
            found("${docEntryTag(doc)} linked to SubmissionSet with ${assocTag(assocs[0])}")
        }
    }

    boolean single() { assocs.size() == 1 }

    @Guard(methodNames=['single'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ssdehm020', msg='Has SubmissionSetStatus Slot', ref='ITI TF-3: 4.1.4.1')
    def ssdehm020() {
        def assoc = assocs[0]
        List<OMElement> sssSlots = slotsWithName(assoc, 'SubmissionSetStatus')
        expected("Single SubmissionSetStatus Slot")
        if (sssSlots.size() == 1) {
            found(assocTypeTag(sssSlots[0]))
        } else if (sssSlots.size() == 0) {
            found(' ')
            fail('Missing')
        } else {
            found("${sssSlots.size()} copies")
            fail('Multiple')
        }
    }

    boolean sssPresent() { assocs.size() == 1 && has_sss_slot(assocs[0])}

    @Guard(methodNames=['sssPresent'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='ssdehm030', msg='SubmissionSetStatus Slot value', ref='ITI TF-3: 4.1.4.1')
    def ssdehm030() {
        List<String> values = slotValues(assocs[0], 'SubmissionSetStatus')
        expected('Original')
        if (values.size() == 1) {
            found(values[0])
            if (values[0] != 'Original') fail('Wrong value')
        }
        else if (values.size() == 0) {
            found(' ')
            fail('Missing')
        }
        else {
            found(values)
            fail('Multiple')
        }
    }


}
