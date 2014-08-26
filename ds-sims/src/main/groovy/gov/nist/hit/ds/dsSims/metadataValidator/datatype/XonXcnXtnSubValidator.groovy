package gov.nist.hit.ds.dsSims.metadataValidator.datatype

import gov.nist.hit.ds.dsSims.metadataValidator.object.SlotModel
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

/**
 *
 * @author bill
 *
 */
public class XonXcnXtnSubValidator extends ValComponentBase {
    SlotModel model

    public XonXcnXtnSubValidator(ValComponentBase base, SlotModel model) {
        super(base.event)
        this.model = model
    }

//	String xresource = "ITI TF-3: Table 4.1-6 (intendedRecipient) and " +
//					"XDR and XDM for Direct Messaging Specification";

    @Override
    void run() {
        runValidationEngine()
    }

    @ErrorCode(code = XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id = 'XonXcnXtn001', msg = 'Format is XON|XCN|XTN  where XTN is required', ref = "ITI TF-3: Table 4.1-6 (intendedRecipient) and XDR and XDM for Direct Messaging Specification")
    def formatCheck() {
        for (String input : model.values) {
            String[] parts = input.split("\\|");

            if (parts.length > 3)
                fail("Found ${parts.length} parts separated by |", 'Expected up to 3', input)

            if (parts.length != 3 || parts[2].equals(""))
                fail("Either Organization Name (XON format) or Extended Person Name (XCN) shall be present", input);

            if (parts.length > 0 && !parts[0].equals(""))
                new XonSubValidator(this, parts[0], "intendedRecipient(Organization Name)").run();

            if (parts.length > 1 && !parts[1].equals(""))
                new XcnSubValidator(er, context + ": intendedRecipient(Extended Person Name)", resource).validate(parts[1]);

            if (parts.length > 2 && !parts[2].equals(""))
                new XtnFormat(er, context + ": intendedRecipient(Telecommunication)", resource).validate(parts[2]);
        }
    }

}
