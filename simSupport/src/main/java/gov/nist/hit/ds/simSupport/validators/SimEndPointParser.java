package gov.nist.hit.ds.simSupport.validators;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.simSupport.datatypes.SimEndPoint;
import gov.nist.hit.ds.simSupport.engine.Inject;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.core.FaultCodes;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;

public class SimEndPointParser extends SimComponentBase {
	SimEndPoint simEndPoint;
	HttpParserBa httpParser;

	@Inject
	public void setHttpParser(HttpParserBa httpParser) {
		this.httpParser = httpParser;
	}
	
	public SimEndPoint getEndPoint() {
		return simEndPoint;
	}
		
	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		String endpoint = httpParser.getEndpoint();
		String simid;
		String actor;
		String transaction;
		er.externalChallenge("Endpoint is <" + endpoint + ">");
		try {
			int simIndex;
			String[] uriParts = endpoint.split("\\/");

			for (simIndex=0; simIndex<uriParts.length; simIndex++) {
				if ("sim".equals(uriParts[simIndex]))
					break;
			}
			if (simIndex >= uriParts.length) {
				throw new SoapFaultException(
						er,
						FaultCodes.EndpointUnavailable, 
						new ErrorContext("Endpoint <" + endpoint + "> does not parse as a simulator endpoint"));
			}
			simid = uriParts[simIndex + 1];
			actor = uriParts[simIndex + 2];
			transaction = uriParts[simIndex + 3];
		}
		catch (Exception e) {
			throw new SoapFaultException(
					er,
					FaultCodes.EndpointUnavailable, 
					new ErrorContext("Endpoint <" + endpoint + "> does not parse as a simulator endpoint"));
		}
		er.detail("simId=<" + simid + "> actor=<" + actor + "> transaction=<" + transaction + ">");
		simEndPoint =  new SimEndPoint().
				setSimId(simid).
				setActor(actor).
				setTransaction(transaction);
	}

}
