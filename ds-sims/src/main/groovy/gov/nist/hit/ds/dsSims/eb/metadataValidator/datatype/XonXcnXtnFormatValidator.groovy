package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype

import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
/**
 * 
 * @author bill
 *
 */
public class XonXcnXtnFormatValidator extends AbstractFormatValidator {

    String formatName() { return 'XON|XCN|XTN' }

    XonXcnXtnFormatValidator(SimHandle _simHandle, String context) {
        super(_simHandle, context);
    }

    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxonxcnxtn010', msg='XON|XCN|XTN format', ref='ITI TF-3: Table 4.1-6 (intendedRecipient) and XDR and XDM for Direct Messaging Specification')
    def roxonxcnxtn010() {
        infoFound("${context} is ${value}")
        parts = value.split('|')
    }

    String[] parts

    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxonxcnxtn020', msg='Format is XON|XCN|XTN  where XTN is required', ref='ITI TF-3: Table 4.1-6 (intendedRecipient)')
    def roxonxcnxtn020() {
        infoFound("Found ${parts.length} | separated parts - must 1, 2, or 3")
        assertTrue(parts.length <= 3)
    }

    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxonxcnxtn030', msg='Either Organization Name (XON format) or Extended Person Name (XCN) shall be present', ref='ITI TF-3: Table 4.1-6 (intendedRecipient)')
    def roxonxcnxtn030() {
        assertFalse(parts.length != 3 || parts[2].equals(""))
    }

    boolean xonPresent() {  parts.length > 0 && !parts[0].equals("") }
    boolean xcnPresent() {  parts.length > 1 && !parts[1].equals("") }
    boolean xtnPresent() {  parts.length > 2 && !parts[2].equals("") }

    @Guard(methodNames=['xonPresent'])
    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxonxcnxtn040', msg='Validate XON', ref='ITI TF-3: Table 4.1-6 (intendedRecipient)')
    def roxonxcnxtn040() {
        new XonFormatValidator(simHandle, context + ": intendedRecipient(Organization Name)", parts[0]).asSelf(this).run()
    }

    @Guard(methodNames=['xcnPresent'])
    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxonxcnxtn050', msg='Validate XCN', ref='ITI TF-3: Table 4.1-6 (intendedRecipient)')
    def roxonxcnxtn050() {
        new XcnFormatValidator(simHandle, context + ": intendedRecipient(Extended Person Name)", parts[1]).asSelf(this).run()
    }

    @Guard(methodNames=['xtnPresent'])
    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxonxcnxtn060', msg='Validate XCN', ref='ITI TF-3: Table 4.1-6 (intendedRecipient)')
    def roxonxcnxtn060() {
        new XtnFormatValidator(simHandle, context + ": intendedRecipient(Telecommunication)", parts[2]).asSelf(this).run()
    }
}
