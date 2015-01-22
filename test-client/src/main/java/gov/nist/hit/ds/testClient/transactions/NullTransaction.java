package gov.nist.hit.ds.testClient.transactions;

import gov.nist.hit.ds.testClient.engine.StepContext;
import gov.nist.hit.ds.xdsExceptions.XdsException;
import gov.nist.hit.ds.xdsExceptions.XdsInternalException;
import org.apache.axiom.om.OMElement;

public class NullTransaction extends BasicTransaction {

	public NullTransaction(StepContext s_ctx, OMElement instruction, OMElement instruction_output) {
		super(s_ctx, instruction, instruction_output);
		defaultEndpointProcessing = false;
		noMetadataProcessing = true;
	}


	protected String getBasicTransactionName() {
		return "Null";
	}

	protected String getRequestAction() {
		return null;
	}

	protected void parseInstruction(OMElement part) throws XdsInternalException {
		parseBasicInstruction(part);
	}

	protected void run(OMElement request) throws XdsException {
	}

}