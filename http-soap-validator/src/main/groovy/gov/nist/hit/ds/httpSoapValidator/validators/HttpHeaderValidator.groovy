package gov.nist.hit.ds.httpSoapValidator.validators
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

public class HttpHeaderValidator extends ValComponentBase {
    private static Logger log = Logger.getLogger(HttpHeaderValidator);
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

    @Fault(code=FaultCode.Sender)
    @Validation(id="HttpMessage001", msg='MTOM vs SIMPLE SOAP', ref="??")
    public void mtomSimpleSoapMatches() throws SoapFaultException {
        def expected = (handle.transactionType.multiPart) ? 'MTOM' : 'SIMPLE SOAP'
        def found = (httpParser.isMultipart()) ? 'MTOM' : 'SIMPLE SOAP'
        assertEquals(expected, found)
    }

    @Fault(code=FaultCode.Sender)
    @Validation(id="HttpMessage002", msg="HTTP contentType", ref="??")
    public void soapExpected() throws SoapFaultException {
        String contentTypeString = httpParser.getHttpMessage().getHeader("content-type")
        String contentType = new HttpHeader(contentTypeString)?.getValue()?.toLowerCase()
        assertEquals('application/soap+xml', contentType)
    }

    void run() {
        header = handle?.event?.inOut?.reqHdr
        body = handle?.event?.inOut?.reqBody

        assert header
        assert body

        httpParser = new HttpParserBa(header.getBytes())

        runValidationEngine()
	}

	@Override
	public boolean showOutputInLogs() {
		return true;
	}


}
