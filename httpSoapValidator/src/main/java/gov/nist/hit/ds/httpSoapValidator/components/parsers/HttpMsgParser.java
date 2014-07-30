package gov.nist.hit.ds.httpSoapValidator.components.parsers;

import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.http.parser.HttpParseException;
import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase;
import gov.nist.hit.ds.soapSupport.FaultCode;
import gov.nist.hit.ds.soapSupport.SoapFaultException;
import gov.nist.hit.ds.utilities.html.HttpMessageContent;
import gov.nist.hit.ds.xdsException.ExceptionUtil;

public class HttpMsgParser extends ValComponentBase {
	String header = null;
	byte[] body;
	HttpParserBa hparser = null;

	public HttpMsgParser setMessageContent(HttpMessageContent content) {
		this.header = content.getHeader();
		this.body = content.getBody();
		return this;
	}
	
	public HttpParserBa getHttpParser() {
		return hparser;
	}

	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		try {
			hparser = new HttpParserBa(header.getBytes());
			hparser.setBody(body);
		} catch (HttpParseException e) {
			throw new SoapFaultException(
					new AssertionGroup(),     // temp
					FaultCode.Receiver,
					ExceptionUtil.exception_details(e));
		} catch (ParseException e) {
			throw new SoapFaultException(
                    new AssertionGroup(),     // temp
					FaultCode.Receiver,
					ExceptionUtil.exception_details(e));
		} 
	}

    @Override
    public void run() throws SoapFaultException, RepositoryException {

    }

    @Override
	public boolean showOutputInLogs() {
		return false;
	}

}
