package gov.nist.hit.ds.simSupport.validators;

import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.annotations.Inject;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.core.Endpoint;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;

import org.apache.log4j.Logger;

public class SimEndpointParser extends SimComponentBase {
	static Logger logger = Logger.getLogger(SimEndpointParser.class);
	SimEndpoint simEndpoint = null;
	String endpoint;
	
	@Inject
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
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
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
}
