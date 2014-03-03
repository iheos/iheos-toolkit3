package gov.nist.toolkit.testengine.transactions;

import gov.nist.toolkit.registrysupport.MetadataSupport;
import gov.nist.toolkit.testengine.engine.StepContext;
import gov.nist.toolkit.xdsexception.MetadataException;
import gov.nist.toolkit.xdsexception.XdsInternalException;

import org.apache.axiom.om.OMElement;

public class MockTransaction extends BasicTransaction {

	public MockTransaction() {
		super();
	}

	 
	protected void run(OMElement request) throws Exception {
		testLog.add_name_value(instruction_output, "Endpoint", "http://localhost:8080/myendpoint");
		testLog.add_name_value(	instruction_output, 
				"InputMetadata", MetadataSupport.om_factory.createOMElement("MyTopElement", null));
		testLog.add_name_value(instruction_output, "OutHeader", MetadataSupport.om_factory.createOMElement("Header", null));
		testLog.add_name_value(instruction_output, "OutAction", "Action");
		testLog.add_name_value(instruction_output, "ExpectedInAction", "Action");
		testLog.add_name_value(instruction_output, "InHeader", MetadataSupport.om_factory.createOMElement("Header", null));
		testLog.add_name_value(instruction_output, "Result", MetadataSupport.om_factory.createOMElement("RegistryResponse", null));
	}

	 
	protected void parseInstruction(OMElement part)
			throws XdsInternalException, MetadataException {
		parseBasicInstruction(part);
	}

	 
	protected String getRequestAction() {
		return "Request";
	}

	 
	protected String getBasicTransactionName() {
		return "Mock";
	}

}
