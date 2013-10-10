package gov.nist.hit.ds.testEngine.transactions;

import gov.nist.hit.ds.testEngine.context.StepContext;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import org.apache.axiom.om.OMElement;

public class MockTransaction extends BasicTransaction {

	public MockTransaction(StepContext s_ctx, OMElement instruction,
			OMElement instruction_output) {
		super(s_ctx, instruction, instruction_output);
	}

	@Override
	protected void run(OMElement request) throws Exception {
		testLog.add_name_value(instruction_output, "Endpoint", "http://localhost:8080/myendpoint");
		testLog.add_name_value(	instruction_output, 
				"InputMetadata", XmlUtil.om_factory.createOMElement("MyTopElement", null));
		testLog.add_name_value(instruction_output, "OutHeader", XmlUtil.om_factory.createOMElement("Header", null));
		testLog.add_name_value(instruction_output, "OutAction", "Action");
		testLog.add_name_value(instruction_output, "ExpectedInAction", "Action");
		testLog.add_name_value(instruction_output, "InHeader", XmlUtil.om_factory.createOMElement("Header", null));
		testLog.add_name_value(instruction_output, "Result", XmlUtil.om_factory.createOMElement("RegistryResponse", null));
	}

	@Override
	protected void parseInstruction(OMElement part)
			throws XdsInternalException, MetadataException {
		parseBasicInstruction(part);
	}

	@Override
	protected String getRequestAction() {
		return "Request";
	}

	@Override
	protected String getBasicTransactionName() {
		return "Mock";
	}

}
