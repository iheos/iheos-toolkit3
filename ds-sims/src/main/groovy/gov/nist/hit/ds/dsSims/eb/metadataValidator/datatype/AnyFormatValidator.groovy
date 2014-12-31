package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

public class AnyFormatValidator extends AbstractFormatValidator {
    String value

    String formatName() { return 'Any' }

	public AnyFormatValidator(SimHandle _simHandle, String _context, String _value) {
        super(_simHandle, _context)
        value = _value
    }

    @Validation(id='roany010', msg='Any format', ref='')
    def roany010() {
        infoFound("${context} is ${value}")
    }
}
