package gov.nist.hit.ds.httpSoapValidator.validators;

import gov.nist.hit.ds.http.parser.HttpParseException;
import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.simSupport.engine.Inject;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.html.HttpMessageContent;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
