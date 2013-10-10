package gov.nist.hit.ds.testEngine.transactions;

import gov.nist.hit.ds.testEngine.context.StepContext;

import org.apache.axiom.om.OMElement;

public class GenericSoap11Transaction extends StoredQueryTransaction {

	public GenericSoap11Transaction(StepContext s_ctx, OMElement instruction,
			OMElement instruction_output) {
		super(s_ctx, instruction, instruction_output);
		setXdsVersion(BasicTransaction.xds_a);  // Simple Soap Header
		setParseMetadata(false);
	}

}
