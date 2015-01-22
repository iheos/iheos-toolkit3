package gov.nist.hit.ds.testClient.transactions;

import gov.nist.hit.ds.testClient.engine.StepContext;
import org.apache.axiom.om.OMElement;

abstract public class QueryTransaction extends BasicTransaction {
	
	public QueryTransaction(StepContext s_ctx, OMElement instruction, OMElement instruction_output) {
		super(s_ctx, instruction, instruction_output);
		parse_metadata = false;
	}


}
