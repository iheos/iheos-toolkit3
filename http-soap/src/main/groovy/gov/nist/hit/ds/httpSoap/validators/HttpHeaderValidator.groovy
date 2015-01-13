package gov.nist.hit.ds.httpSoap.validators
import gov.nist.hit.ds.http.parser.HttpHeader
import gov.nist.hit.ds.http.parser.HttpParserBa
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Fault
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
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
    SimHandle simHandle

	public HttpHeaderValidator(SimHandle handle) {
        super(handle.event)
        this.simHandle = handle
		setName(getClass().getSimpleName());
	}
	
	public HttpHeaderValidator setMessageContent(HttpMessageContent content) {
		this.header = content.getHeader();
		this.body = content.getBody();
		return this;
	}

    @Fault(code=FaultCode.Sender)
    @Validation(id="HttpMessage001", msg='MTOM vs SIMPLE SOAP', ref="??")
    public void mtomSimpleSoapMatches() throws SoapFaultException {
        def expected = (simHandle.transactionType.multiPart) ? 'MTOM' : 'SIMPLE SOAP'
        def found = (httpParser.isMultipart()) ? 'MTOM' : 'SIMPLE SOAP'
        assertEquals(expected, found)
    }

    @Fault(code=FaultCode.Sender)
    @Validation(id="HttpMessage002", msg="HTTP contentType", ref="??")
    public void soapExpected() throws SoapFaultException {
        String contentTypeString = httpParser.getHttpMessage().getHeader("content-type")
        String contentType = new HttpHeader(contentTypeString)?.getValue()?.toLowerCase()
        assertEquals('multipart/related', contentType)
    }

    void run() {
        header = simHandle?.event?.inOut?.reqHdr
        body = simHandle?.event?.inOut?.reqBody

        assert header
        assert body

        httpParser = new HttpParserBa((header + "\r\n\r\n" + new String(body)).bytes)

        runValidationEngine()
	}

	@Override
	public boolean showOutputInLogs() {
		return true;
	}


}
