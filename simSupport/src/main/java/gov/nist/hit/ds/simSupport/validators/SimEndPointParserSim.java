package gov.nist.hit.ds.simSupport.validators;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.annotations.Inject;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.core.FaultCode;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;

public class SimEndPointParserSim extends SimComponentBase {
	SimEndpoint simEndPoint;
	HttpParserBa httpParser;

	@Inject
	public void setHttpParser(HttpParserBa httpParser) {
		this.httpParser = httpParser;
	}

	public SimEndpoint getEndPoint() {
		return simEndPoint;
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		String endpoint = httpParser.getEndpoint();
		ag.externalChallenge("Endpoint is <" + endpoint + ">");
		try {
			simEndPoint = new SimEndpointParser().parse(endpoint);
		} catch (Exception e1) {
			throw new SoapFaultException(
					ag,
					FaultCode.EndpointUnavailable, 
					new ErrorContext("Endpoint <" + endpoint + "> does not parse as a simulator endpoint"));
		}
		ag.detail("simId=<" + simEndPoint.getSimId() + "> actor=<" + simEndPoint.getActor() + "> transaction=<" + simEndPoint.getTransaction() + ">");
	}

}
