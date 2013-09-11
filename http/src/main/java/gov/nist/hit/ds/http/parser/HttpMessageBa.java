package gov.nist.hit.ds.http.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpMessageBa implements HttpServletRequest {
	String requestURI;
	String method;
	String protocol;
	List<Header> headers = new ArrayList<Header>();
	byte[] body;
	MultipartMessageBa multipart;

	class Header {
		String name;
		String value;
		String lcname;
		
		Header() {}
		Header(String name, String value) {
			this.name = name;
			this.lcname = name.toLowerCase();
			this.value = value;
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
		
		for (int i=0; i<headers.size(); i++) {
			if (lcHeaderName.equals(headers.get(i).lcname))
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameter(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map getParameterMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getParameterValues(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getDateHeader(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Enumeration getHeaders(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIntHeader(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestURI() {
		return requestURI;
	}

	@Override
	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
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
