package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype

import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class XonXcnFormatValidator extends AbstractFormatValidator {
    String value

    String formatName() { return 'XON|XCN' }

    XonXcnFormatValidator(SimHandle _simHandle, String context, String _value) {
        super(_simHandle, context);
        value = _value
    }

    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxonxcn010', msg='XON|XCN format', ref='ITI TF-3: Table 4.1-6 (intendedRecipient)')
    def roxonxcn010() {
        infoFound("${context} is ${value}")
        parts = value.split('|')
    }

    String[] parts

    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxonxcn020', msg='XON or XCN must be present', ref='ITI TF-3: Table 4.1-6 (intendedRecipient)')
    def roxonxcn020() {
        infoFound("Found ${parts.length} | separated parts - must 1 or 2")
        assertTrue(parts.length <= 2)
    }

    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxonxcn030', msg='Either Organization Name (XON format) or Extended Person Name (XCN) shall be present', ref='ITI TF-3: Table 4.1-6 (intendedRecipient)')
    def roxonxcn030() {
        assertFalse(parts.length == 2 && parts[0].equals("") && parts[1].equals(""))
    }

    boolean xonPresent() { !parts[0].equals("") }
    boolean xcnPresent() { parts.length > 1 && !parts[1].equals("")  }

    @Guard(methodNames=['xonPresent'])
    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxonxcn040', msg='Validate XON', ref='ITI TF-3: Table 4.1-6 (intendedRecipient)')
    def roxonxcn040() {
        new XonFormatValidator(simHandle, context + ": intendedRecipient(Organization Name)", parts[0]).asSelf(this).run()
    }

    @Guard(methodNames=['xcnPresent'])
    @ErrorCode(code= XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='roxonxcn050', msg='Validate XON', ref='ITI TF-3: Table 4.1-6 (intendedRecipient)')
    def roxonxcn050() {
        new XcnFormatValidator(simHandle, context + ": intendedRecipient(Extended Person Name)", parts[1]).asSelf(this).run()
    }
}
