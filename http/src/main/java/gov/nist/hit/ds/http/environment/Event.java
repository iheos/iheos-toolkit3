package gov.nist.hit.ds.http.environment;

import gov.nist.hit.ds.utilities.io.Io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Holds references to input header and body of the HTTP message.  Here the term
 * event corresponds to a transaction, an input to the current actor.
 * @author bill
 *
 */
public class Event {
	File eventDir;

	public Event(File eventDir) {
		this.eventDir = eventDir;
	}

	public File getEventDir() {
		return eventDir;
	}

	public File getResponseBodyFile() {
		return new File(eventDir, "response_body.txt");
	}

	public File getResponseHeaderFile() {
		return new File(eventDir, "response_hdr.txt");
	}

	public File getRequestBodyFile() {
		return new File(eventDir, "request_body.txt");
	}

	public File getRequestHeaderFile() {
		return new File(eventDir, "request_hdr.txt");
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
