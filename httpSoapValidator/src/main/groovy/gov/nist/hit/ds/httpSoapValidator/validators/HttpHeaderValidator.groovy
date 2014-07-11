package gov.nist.hit.ds.httpSoapValidator.validators

import gov.nist.hit.ds.http.parser.HttpHeader
import gov.nist.hit.ds.http.parser.HttpParserBa
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional
import gov.nist.hit.ds.utilities.html.HttpMessageContent
import groovy.util.logging.Log4j

/**
 * Validate HTTP message.  Launches either MtomMessageValidator or SimpleSoapMessageValidator as appropriate.
 * @author bill
 *   
 */
@Log4j
public class HttpHeaderValidator extends ValComponentBase {
	String header = null;
	byte[] body;
	HttpParserBa httpParser = null;
    SimHandle handle

	public HttpHeaderValidator(SimHandle handle) {
        super(handle.event)
        this.handle = handle
		setName(getClass().getSimpleName());
	}
	
	public HttpHeaderValidator setMessageContent(HttpMessageContent content) {
		this.header = content.getHeader();
		this.body = content.getBody();
		return this;
	}

    @ValidationFault(id="HttpMessage001", required=RequiredOptional.R, msg="MTOM/SIMPLE SOAP must match transaction requirements", faultCode=FaultCode.Sender, ref="??")
    public void mtomSimpleSoapMatches() throws SoapFaultException {
        infoFound(" ${(httpParser.isMultipart()) ? 'MTOM' : 'SIMPLE SOAP'} found.")
        assertEquals(handle.transactionType.multiPart, httpParser.isMultipart())
    }

    @ValidationFault(id="HttpMessage002", required=RequiredOptional.R, msg="SOAP Expected", faultCode=FaultCode.Sender, ref="??")
    public void soapExpected() throws SoapFaultException {
        infoFound((handle.transactionType.soap) ? 'SOAP expected' : 'SOAP not expected')
        String contentTypeString = httpParser.getHttpMessage().getHeader("content-type")
        String contentType = new HttpHeader(contentTypeString)?.getValue()?.toLowerCase()
        infoFound("content-type is ${contentType}")
        assertEquals('application/soap+xml', contentType)
    }

    void run() {
        header = handle.event.inOut.reqHdr
        body = handle.event.inOut.reqBody

        assert header
        assert body

        httpParser = new HttpParserBa(header.getBytes())

        runValidationEngine()

        if (httpParser.isMultipart()) {

        } else {

        }

	}

	@Override
	public boolean showOutputInLogs() {
		return true;
	}


}
