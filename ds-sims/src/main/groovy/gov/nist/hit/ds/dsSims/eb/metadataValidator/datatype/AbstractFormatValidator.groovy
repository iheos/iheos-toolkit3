package gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype

import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import org.apache.axiom.om.OMElement

public abstract class AbstractFormatValidator extends ValComponentBase {
    SimHandle simHandle
    String context
    String value
    OMElement value_ele

    abstract String formatName();

	AbstractFormatValidator(SimHandle _simHandle, String _context) {
        super(_simHandle.event)
        simHandle = _simHandle
        context = _context
	}
	
	def validate(String input)  {
        value = input
        asSelf(this)
        run()
    }

	def validate(OMElement input) {
        value_ele = input
        asSelf(this)
        run()
    }
}
