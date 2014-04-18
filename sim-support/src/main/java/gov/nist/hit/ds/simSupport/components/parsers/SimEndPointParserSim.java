package gov.nist.hit.ds.simSupport.components.parsers;

import gov.nist.hit.ds.eventLog.assertion.Assertion;
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorContext;
import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.annotations.SimComponentInject;
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.FaultCode;
import gov.nist.hit.ds.soapSupport.SoapFaultException;

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
        Assertion a = new Assertion();
        a.setFound("Endpoint is <" + endpoint + ">");
        a.setStatus(AssertionStatus.INFO);
        ag.addAssertion(a);
		try {
			simEndPoint = new SimEndpointParser().parse(endpoint);
		} catch (Exception e1) {
			throw new SoapFaultException(
					ag,
					FaultCode.EndpointUnavailable,
					new ErrorContext("Endpoint <" + endpoint + "> does not parse as a simulator endpoint"));
		}
        a = new Assertion();
        a.setFound("simId=<" + simEndPoint.getSimId() + "> actor=<" + simEndPoint.getActor() + "> transaction=<" + simEndPoint.getTransaction() + ">");
        a.setStatus(AssertionStatus.INFO);
        ag.addAssertion(a);
	}

	@Override
	public boolean showOutputInLogs() {
		return false;
	}

}
