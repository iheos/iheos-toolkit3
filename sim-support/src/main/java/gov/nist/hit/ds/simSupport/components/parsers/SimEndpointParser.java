package gov.nist.hit.ds.simSupport.components.parsers;

import gov.nist.hit.ds.simSupport.annotations.SimComponentInject;
import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.soapSupport.FaultCode;
import gov.nist.hit.ds.soapSupport.SoapFaultException;
import gov.nist.hit.ds.soapSupport.core.Endpoint;
import org.apache.log4j.Logger;

public class SimEndpointParser extends SimComponentBase {
	static Logger logger = Logger.getLogger(SimEndpointParser.class);
	SimEndpoint simEndpoint = null;
	String endpoint;
	
	@SimComponentInject
	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint.getEndpoint();
	}
	
	public SimEndpoint parse(String endpoint) throws Exception {
		String simid;
		String actor;
		String transaction;
		int simIndex;
		String[] uriParts = endpoint.split("\\/");

		for (simIndex=0; simIndex<uriParts.length; simIndex++) {
			if ("sim".equals(uriParts[simIndex]))
				break;
		}
		if (simIndex >= uriParts.length) {
			throw new Exception("Endpoint <" + endpoint + "> does not parse as a simulator endpoint");
		}
		simid = uriParts[simIndex + 1];
		actor = uriParts[simIndex + 2];
		transaction = uriParts[simIndex + 3];
		simEndpoint = new SimEndpoint().
				setSimId(simid).
				setActor(actor).
				setTransaction(transaction);
		return simEndpoint;
	}
	
	public SimEndpoint getSimEndpoint() {
		return simEndpoint;
	}

	@Override
	public void run() throws SoapFaultException {
		logger.trace("Run SimEndpointParser");
		try {
			parse(endpoint);
		} catch (Exception e) {
			throw new SoapFaultException(
					ag,
					FaultCode.Receiver,
					e.getMessage());
		}
	}

	@Override
	public boolean showOutputInLogs() {
		return false;
	}
}
