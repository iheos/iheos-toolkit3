package gov.nist.toolkit.valregmsg.message;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import gov.nist.toolkit.valsupport.engine.MessageValidatorEngine;
import gov.nist.toolkit.valsupport.message.MessageValidator;
import org.apache.axiom.om.OMElement;

/**
 * Validate a Query Response message.
 * @author bill
 *
 */
public class QueryResponseValidator extends MessageValidator {
	OMElement xml;

	public QueryResponseValidator(ValidationContext vc, OMElement xml) {
		super(vc);
		this.xml = xml;
	}

	public void run(IAssertionGroup er, MessageValidatorEngine mvc) {
		this.er = er;
		

	}

}
