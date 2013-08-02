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

public class SimEndPointParserSim extends SimComponentBase {
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
		er.externalChallenge("Endpoint is <" + endpoint + ">");
		try {
			simEndPoint = new SimEndpointParser().parse(endpoint);
		} catch (Exception e1) {
			throw new SoapFaultException(
					er,
					FaultCodes.EndpointUnavailable, 
					new ErrorContext("Endpoint <" + endpoint + "> does not parse as a simulator endpoint"));
		}
		er.detail("simId=<" + simEndPoint.getSimId() + "> actor=<" + simEndPoint.getActor() + "> transaction=<" + simEndPoint.getTransaction() + ">");
	}

}
