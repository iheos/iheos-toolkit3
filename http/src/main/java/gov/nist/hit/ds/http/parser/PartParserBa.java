package gov.nist.hit.ds.http.parser;

import gov.nist.hit.ds.eventLog.errorRecording.ErrorContext;
import gov.nist.hit.ds.eventLog.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.http.parser.HttpHeader.HttpHeaderParseException;

public class PartParserBa extends HttpParserBa {
	IAssertionGroup er = null;
	PartBa part = new PartBa();
	
	public PartParserBa(byte[] msg) throws HttpParseException, HttpHeaderParseException, ParseException {
		init(msg, part);
		initPart();
	}
	
	public PartParserBa(byte[] msg, IAssertionGroup er, boolean appendixV) throws HttpParseException, HttpHeaderParseException, ParseException {
		this.appendixV = appendixV;
		init(msg, part);
		initPart();
	}
	
	void initPart() throws HttpParseException {
        logger.debug("get content-id header in:\n" + message.getHeaders().toString());
		String contentIDHeaderString = message.getHeader("content-id");
		if (appendixV == false && (contentIDHeaderString == null || contentIDHeaderString.equals("")))
			return;
		try {
			HttpHeader contentIDHeader = new HttpHeader(contentIDHeaderString);
			part.contentID = contentIDHeader.getValue();
			if (part.contentID == null || part.contentID.equals(""))
				throw new HttpParseException("Part has no Content-ID header");
			part.contentID = part.contentID.trim();
			if (!isWrappedIn(part.contentID, "<",">")) {
				if (er != null)
					er.err(XdsErrorCode.Code.NoCode, new ErrorContext("Part Content-ID header value must be wrapped in <   >: Content-ID is " + part.contentID, "http://www.w3.org/TR/2005/REC-xop10-20050125/  Example 2"), this);
				else
					throw new HttpParseException("Part Content-ID header value must be wrapped in <   >: Content-ID is " + part.contentID);
			} else {
				part.contentID = unWrap(part.contentID);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	static public boolean isWrappedIn(String st, String start, String end) {
		if (st.length() < 3)
			return false;
		if (!st.startsWith(start))
			return false;
		if (!st.endsWith(end))
			return  false;
		return true;
	}
	
	static public String unWrap(String st) {
		return st.substring(1, st.length()-1);
	}
	
	public String getContentId() {
		return part.contentID;
	}

}
