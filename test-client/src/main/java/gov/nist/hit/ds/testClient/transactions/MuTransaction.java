package gov.nist.hit.ds.testClient.transactions;

import gov.nist.hit.ds.testClient.engine.StepContext;
import org.apache.axiom.om.OMElement;

public class MuTransaction extends RegisterTransaction {

	public MuTransaction(StepContext s_ctx, OMElement instruction,
			OMElement instruction_output) {
		super(s_ctx, instruction, instruction_output);
	}

	protected String getRequestAction() {
		return "urn:ihe:iti:2010:UpdateDocumentSet";
	}

	protected String getBasicTransactionName() {
		return "update";
	}


}
