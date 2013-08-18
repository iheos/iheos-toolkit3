package gov.nist.hit.ds.eventLog;

import gov.nist.hit.ds.utilities.io.Io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class InOutMessages {
	File inOutDir;
	
	InOutMessages(File inOutDir) {
		this.inOutDir = inOutDir;
	}

	public File getResponseBodyFile() {
		return new File(inOutDir, "response_body.txt");
	}

	public File getResponseHeaderFile() {
		return new File(inOutDir, "response_hdr.txt");
	}

	public File getRequestBodyFile() {
		return new File(inOutDir, "request_body.txt");
	}

	public File getRequestHeaderFile() {
		return new File(inOutDir, "request_hdr.txt");
	}

	public String getRequestHeader() throws IOException {
		return Io.stringFromFile(getRequestHeaderFile());
	}

	public String getResponseHeader() throws IOException {
		return Io.stringFromFile(getResponseHeaderFile());
	}

	public byte[] getRequestBody() throws IOException {
		File f = getRequestBodyFile();
		return Io.bytesFromFile(f);
	}

	public byte[] getResponseBody() throws IOException {
		File f = getResponseBodyFile();
		return Io.bytesFromFile(f);
	}

	public void putRequestHeader(String hdr) throws IOException {
		File f = getRequestHeaderFile();
		OutputStream out = null;
		try {
			out = new FileOutputStream(f);
			out.write(hdr.getBytes());
		} finally {
			if (out != null)
				out.close();
		}
	}

	public void putRequestBody(byte[] bytes) throws IOException {
		OutputStream out = null;
		try {
			out = new FileOutputStream(getRequestBodyFile());
			out.write(bytes);
		} finally {
			if (out != null)
				out.close();
		}
	}
}