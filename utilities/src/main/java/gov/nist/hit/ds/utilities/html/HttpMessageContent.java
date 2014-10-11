package gov.nist.hit.ds.utilities.html;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HttpMessageContent {
	String header;
	byte[] body;

    public HttpMessageContent() {}

    public HttpMessageContent(String header, byte[] body) {
        this.header = header;
        this.body = body;
    }

	public String getHeader() {
		return header;
	}

	public HttpMessageContent setHeader(String header) {
		this.header = header;
		return this;
	}
	public byte[] getBody() {
		return body;
	}

	public HttpMessageContent setBody(byte[] body) {
		this.body = body;
		return this;
	}
	
	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write(header.getBytes());
        os.write("\r\n\r\n".getBytes());
		os.write(body);
		return os.toByteArray();
	}
	
}
