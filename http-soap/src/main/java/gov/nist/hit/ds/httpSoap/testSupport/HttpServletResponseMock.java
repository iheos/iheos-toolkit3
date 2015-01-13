package gov.nist.hit.ds.httpSoap.testSupport;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Locale;

public class HttpServletResponseMock implements HttpServletResponse {
	String contentType;
	
	class UnimplementedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		UnimplementedException() {
			super("Unimplemented Method");
		}
	}
	
	class ServletOutputStreamToPrintStream extends ServletOutputStream {
		PrintStream ps;

		ServletOutputStreamToPrintStream(PrintStream ps) {
			this.ps = ps;
		}
		
		
		public void write(int b) throws IOException {
			ps.write(b);
		}
		
	}

	
	public void flushBuffer() throws IOException {
		throw new UnimplementedException();
	}

	
	public int getBufferSize() {
		throw new UnimplementedException();
	}

	
	public String getCharacterEncoding() {
		throw new UnimplementedException();
	}

	
	public String getContentType() {
		throw new UnimplementedException();
	}

	
	public Locale getLocale() {
		throw new UnimplementedException();
	}

	
	public ServletOutputStream getOutputStream() throws IOException {
		return new ServletOutputStreamToPrintStream(System.out);
	}

	
	public PrintWriter getWriter() throws IOException {
		throw new UnimplementedException();
	}

	
	public boolean isCommitted() {
		throw new UnimplementedException();
	}

	
	public void reset() {
		throw new UnimplementedException();
	}

	
	public void resetBuffer() {
		throw new UnimplementedException();
	}

	
	public void setBufferSize(int arg0) {
		throw new UnimplementedException();
	}

	
	public void setCharacterEncoding(String arg0) {
		throw new UnimplementedException();
	}

	
	public void setContentLength(int arg0) {
		throw new UnimplementedException();
	}

	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	
	public void setLocale(Locale arg0) {
		throw new UnimplementedException();
	}

	
	public void addCookie(Cookie arg0) {
		throw new UnimplementedException();
	}

	
	public void addDateHeader(String arg0, long arg1) {
		throw new UnimplementedException();
	}

	
	public void addHeader(String arg0, String arg1) {
		throw new UnimplementedException();
	}

	
	public void addIntHeader(String arg0, int arg1) {
		throw new UnimplementedException();
	}

	
	public boolean containsHeader(String arg0) {
		throw new UnimplementedException();
	}

	
	public String encodeRedirectURL(String arg0) {
		throw new UnimplementedException();
	}

	
	public String encodeRedirectUrl(String arg0) {
		throw new UnimplementedException();
	}

	
	public String encodeURL(String arg0) {
		throw new UnimplementedException();
	}

	
	public String encodeUrl(String arg0) {
		throw new UnimplementedException();
	}

	
	public void sendError(int arg0) throws IOException {
		throw new UnimplementedException();
	}

	
	public void sendError(int arg0, String arg1) throws IOException {
		throw new UnimplementedException();
	}

	
	public void sendRedirect(String arg0) throws IOException {
		throw new UnimplementedException();
	}

	
	public void setDateHeader(String arg0, long arg1) {
		throw new UnimplementedException();
	}

	
	public void setHeader(String arg0, String arg1) {
		throw new UnimplementedException();
	}

	
	public void setIntHeader(String arg0, int arg1) {
		throw new UnimplementedException();
	}

	
	public void setStatus(int arg0) {
		throw new UnimplementedException();
	}

	
	public void setStatus(int arg0, String arg1) {
		throw new UnimplementedException();
	}

}
