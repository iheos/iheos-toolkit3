package gov.nist.hit.ds.httpSoapValidator.testSupport;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
		
		@Override
		public void write(int b) throws IOException {
			ps.write(b);
		}
		
	}

	@Override
	public void flushBuffer() throws IOException {
		throw new UnimplementedException();
	}

	@Override
	public int getBufferSize() {
		throw new UnimplementedException();
	}

	@Override
	public String getCharacterEncoding() {
		throw new UnimplementedException();
	}

	@Override
	public String getContentType() {
		throw new UnimplementedException();
	}

	@Override
	public Locale getLocale() {
		throw new UnimplementedException();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new ServletOutputStreamToPrintStream(System.out);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		throw new UnimplementedException();
	}

	@Override
	public boolean isCommitted() {
		throw new UnimplementedException();
	}

	@Override
	public void reset() {
		throw new UnimplementedException();
	}

	@Override
	public void resetBuffer() {
		throw new UnimplementedException();
	}

	@Override
	public void setBufferSize(int arg0) {
		throw new UnimplementedException();
	}

	@Override
	public void setCharacterEncoding(String arg0) {
		throw new UnimplementedException();
	}

	@Override
	public void setContentLength(int arg0) {
		throw new UnimplementedException();
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public void setLocale(Locale arg0) {
		throw new UnimplementedException();
	}

	@Override
	public void addCookie(Cookie arg0) {
		throw new UnimplementedException();
	}

	@Override
	public void addDateHeader(String arg0, long arg1) {
		throw new UnimplementedException();
	}

	@Override
	public void addHeader(String arg0, String arg1) {
		throw new UnimplementedException();
	}

	@Override
	public void addIntHeader(String arg0, int arg1) {
		throw new UnimplementedException();
	}

	@Override
	public boolean containsHeader(String arg0) {
		throw new UnimplementedException();
	}

	@Override
	public String encodeRedirectURL(String arg0) {
		throw new UnimplementedException();
	}

	@Override
	public String encodeRedirectUrl(String arg0) {
		throw new UnimplementedException();
	}

	@Override
	public String encodeURL(String arg0) {
		throw new UnimplementedException();
	}

	@Override
	public String encodeUrl(String arg0) {
		throw new UnimplementedException();
	}

	@Override
	public void sendError(int arg0) throws IOException {
		throw new UnimplementedException();
	}

	@Override
	public void sendError(int arg0, String arg1) throws IOException {
		throw new UnimplementedException();
	}

	@Override
	public void sendRedirect(String arg0) throws IOException {
		throw new UnimplementedException();
	}

	@Override
	public void setDateHeader(String arg0, long arg1) {
		throw new UnimplementedException();
	}

	@Override
	public void setHeader(String arg0, String arg1) {
		throw new UnimplementedException();
	}

	@Override
	public void setIntHeader(String arg0, int arg1) {
		throw new UnimplementedException();
	}

	@Override
	public void setStatus(int arg0) {
		throw new UnimplementedException();
	}

	@Override
	public void setStatus(int arg0, String arg1) {
		throw new UnimplementedException();
	}

}
