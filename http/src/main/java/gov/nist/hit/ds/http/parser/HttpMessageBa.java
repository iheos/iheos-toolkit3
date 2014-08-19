package gov.nist.hit.ds.http.parser;

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

public class HttpMessageBa implements HttpServletRequest {
	String requestURI;
	String method;
	String protocol;
	List<Header> headers = new ArrayList<Header>();
	byte[] body;
	MultipartMessageBa multipart;
    static Logger logger = Logger.getLogger(HttpMessageBa.class);


    class Header {
		String name;
		String value;
		String lcname;
		
		Header() {}
		Header(String name, String value) {
			this.name = name.trim();
			this.lcname = name.toLowerCase();
			this.value = value.trim();
		}
		
		public String toString() {
			return name + ": " + value;
		}
		
	}
	
	public String getEndpoint() {
		return requestURI;
	}


	public HttpMessageBa() {}

	public void setHeaderMap(Map<String, String> hdrs) {		
		for (String key : hdrs.keySet()) {
			String val = hdrs.get(key);
			
			Header h = new Header(key, val);
			headers.add(h);
		}
	}

	public List<String> getHeaders() {
		List<String> hs = new ArrayList<String>();
		for (Header h : headers) {
			hs.add(h.name + ": " + h.value);
		}
		return hs;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}
	
	public String getHeadersAsString() {
		StringBuffer buf = new StringBuffer();
		
		for (String hdr : getHeaders()) {
			buf.append(hdr);
			buf.append("\r\n");
		}
		
		return buf.toString();
	}

	public String asMessage() throws HttpParseException {
		StringBuffer buf = new StringBuffer();
		StringBuffer bodybuf = new StringBuffer();

		if (multipart == null)
			bodybuf.append(body);
		else
			bodybuf.append(multipart.asMessage());


		// fix Content-Length header
		removeHeader("Content-Length");
		if (bodybuf.length() != 0)
			addHeader("Content-Length: " + bodybuf.length());

		for (String hdr : getHeaders()) {
			buf.append(hdr);
			buf.append("\r\n");
		}
		buf.append("\r\n");

		buf.append(bodybuf);

		return buf.toString();
	}

	public boolean removeHeader(String headerName) {
		String lcHeaderName = headerName.toLowerCase();
		boolean changed = false;
		
		for (int i=0; i<headers.size(); i++) {
			Header h = headers.get(i);
			if (h.lcname.equals(headerName)) {
				headers.remove(i);
				i--;
				changed = true;
			}
		}
		return changed;
		
	}

	public String getHeader(String headerName) {
		if (headerName == null)
			return null;
		String lcHeaderName = headerName.toLowerCase();

        logger.debug("looking for header " + lcHeaderName);
		for (int i=0; i<headers.size(); i++) {
            logger.debug(headers.get(i).lcname);
			if (lcHeaderName.equals(headers.get(i).lcname))
                logger.debug("got it");
				return headers.get(i).name + ": " + headers.get(i).value;
		}
		return null;
	}

	public String getHeaderValue(String headerName) {
		if (headerName == null)
			return null;
		String lcHeaderName = headerName.toLowerCase();
		
		for (int i=0; i<headers.size(); i++) {
			if (lcHeaderName.equals(headers.get(i).lcname))
				return headers.get(i).value;
		}
		return null;
	}
	
	public String getContentTransferEncoding() {
		return getHeaderValue("content-transfer-encoding");
	}
	
	public String getContentType() {
		String v = getHeaderValue("content-type");
		String parts[] = v.split(";");
		if (parts.length == 1)
			return v;
		return parts[0];
	}
	
	public String getCharset() {
		String v = getHeaderValue("content-type");
		String parts[] = v.split(";");
		if (parts.length == 1)
			return null;
		String vals[] = parts[1].split("=");
		if (vals.length == 2)
			return vals[1];
		return null;
	}

	public void addHeader(String name, String value) {
		Header h = new Header(name, value);
		headers.add(h);
	}

	public void addHeader(String header) throws HttpParseException {
		String[] parts = header.split(":",2);
		if (parts.length != 2) {
			if (!header.startsWith("POST")) {
				throw new HttpParseException("Header [" + header + "] does not parse");
			} else {
				String[] postParts = header.split(" ");
				if (postParts == null || postParts.length != 3)
					throw new HttpParseException("POST Header [" + header + "] does not parse");
				method = postParts[0];
				requestURI = postParts[1];
				protocol = postParts[2];
			}
			return;
		}
		Header h = new Header(parts[0].trim(), parts[1].trim());
		headers.add(h);
	}
	
	public Enumeration<String> getHeaderNames() {
		List<String> names = new ArrayList<String>();
		for (Header h : headers) {
			names.add(h.name);
		}
		return new HeaderNamesEnumeration(names.iterator());
	}

	class HeaderNamesEnumeration implements Enumeration<String> {
		Iterator<String> it;
		
		HeaderNamesEnumeration(Iterator<String> it) {
			this.it = it;
		}

		public boolean hasMoreElements() {
			return it.hasNext();
		}

		public String nextElement() {
			return it.next();
		}

	}
	
	/*
	 * HTTPServietRequest
	 */

	@Override
	public Object getAttribute(String arg0) {
		return null;
	}

	@Override
	public Enumeration getAttributeNames() {
		return null;
	}

	@Override
	public String getCharacterEncoding() {
		return null;
	}

	@Override
	public int getContentLength() {
		return body.length;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new MyServletInputStream(body);
	}

	@Override
	public String getLocalAddr() {
		return null;
	}

	@Override
	public String getLocalName() {
		return null;
	}

	@Override
	public int getLocalPort() {
		return 0;
	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public Enumeration getLocales() {
		return null;
	}

	@Override
	public String getParameter(String arg0) {
		return null;
	}

	@Override
	public Map getParameterMap() {
		return null;
	}

	@Override
	public Enumeration getParameterNames() {
		return null;
	}

	@Override
	public String[] getParameterValues(String arg0) {
		return null;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		return null;
	}

	@Override
	public String getRemoteAddr() {
		return null;
	}

	@Override
	public String getRemoteHost() {
		return null;
	}

	@Override
	public int getRemotePort() {
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return null;
	}

	@Override
	public String getScheme() {
		return null;
	}

	@Override
	public String getServerName() {
		return null;
	}

	@Override
	public int getServerPort() {
		return 0;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public void removeAttribute(String arg0) {
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
	}

	@Override
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
	}

	@Override
	public String getAuthType() {
		return null;
	}

	@Override
	public String getContextPath() {
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		return null;
	}

	@Override
	public long getDateHeader(String arg0) {
		return 0;
	}

	@Override
	public Enumeration getHeaders(String arg0) {
		return null;
	}

	@Override
	public int getIntHeader(String arg0) {
		return 0;
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public String getPathInfo() {
		return null;
	}

	@Override
	public String getPathTranslated() {
		return null;
	}

	@Override
	public String getQueryString() {
		return null;
	}

	@Override
	public String getRemoteUser() {
		return null;
	}

	@Override
	public String getRequestURI() {
		return requestURI;
	}

	@Override
	public StringBuffer getRequestURL() {
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		return null;
	}

	@Override
	public String getServletPath() {
		return null;
	}

	@Override
	public HttpSession getSession() {
		return null;
	}

	@Override
	public HttpSession getSession(boolean arg0) {
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return false;
	}

	@Override
	public boolean isUserInRole(String arg0) {
		return false;
	}

	class MyServletInputStream extends ServletInputStream {
		byte[] input;
		int index = 0;
		MyServletInputStream(byte[] input) {
			this.input = input;
		}
		
		@Override
		public int read() throws IOException {
			if (input == null || index >= input.length)
				return -1;
			int val = input[index];
			index++;
			return val;
		}
		
	}
	
}
