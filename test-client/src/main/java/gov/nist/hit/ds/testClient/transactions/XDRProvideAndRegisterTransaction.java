package gov.nist.hit.ds.testClient.transactions;

import gov.nist.hit.ds.testClient.engine.StepContext;
import org.apache.axiom.om.OMElement;

public class XDRProvideAndRegisterTransaction extends
		ProvideAndRegisterTransaction {

	public XDRProvideAndRegisterTransaction(StepContext s_ctx,
			OMElement instruction, OMElement instruction_output) {
		super(s_ctx, instruction, instruction_output);
	}
	
	protected  String getBasicTransactionName() {
		return "xdrpr";
	}


}
