package gov.nist.hit.ds.simSupport.components.parsers;

import gov.nist.hit.ds.eventLog.errorRecording.ErrorContext;
import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.annotations.SimComponentInject;
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;

/**
 * TODO: Why does this exist?  How is it different from SimEndpointParser?
 * @author bmajur
 *
 */
public class SimEndPointParserSim extends SimComponentBase {
	SimEndpoint simEndPoint;
	HttpParserBa httpParser;

	@SimComponentInject
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

	@Override
	public boolean showOutputInLogs() {
		return false;
	}

}
