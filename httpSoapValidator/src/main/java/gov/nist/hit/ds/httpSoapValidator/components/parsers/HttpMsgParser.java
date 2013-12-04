package gov.nist.hit.ds.httpSoapValidator.components.parsers;

import gov.nist.hit.ds.http.parser.HttpParseException;
import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.annotations.Inject;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;
import gov.nist.hit.ds.utilities.html.HttpMessageContent;
import gov.nist.hit.ds.xdsException.ExceptionUtil;

public class HttpMsgParser extends SimComponentBase {
	String header = null;
	byte[] body;
	HttpParserBa hparser = null;

	@Inject
	public HttpMsgParser setMessageContent(HttpMessageContent content) {
		this.header = content.getHeader();
		this.body = content.getBody();
		return this;
	}
	
	public HttpParserBa getHttpParser() {
		return hparser;
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		try {
			hparser = new HttpParserBa(header.getBytes());
			hparser.setBody(body);
		} catch (HttpParseException e) {
			throw new SoapFaultException(
					null,
					FaultCode.Receiver,
					ExceptionUtil.exception_details(e));
		} catch (ParseException e) {
			throw new SoapFaultException(
					null,
					FaultCode.Receiver,
					ExceptionUtil.exception_details(e));
		} 
	}

	@Override
	public boolean showOutputInLogs() {
		return false;
	}

}
