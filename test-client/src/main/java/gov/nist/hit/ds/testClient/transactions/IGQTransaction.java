package gov.nist.hit.ds.testClient.transactions;

import gov.nist.hit.ds.testClient.engine.StepContext;
import org.apache.axiom.om.OMElement;

public class IGQTransaction extends StoredQueryTransaction {

	public IGQTransaction(StepContext s_ctx, OMElement instruction,
			OMElement instruction_output) {
		super(s_ctx, instruction, instruction_output);
	}

	protected String getBasicTransactionName() {
		return "igq";
	}


	public void configure() {
		useAddressing = true;
		soap_1_2 = true;
	}

}
