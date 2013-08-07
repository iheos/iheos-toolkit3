package gov.nist.hit.ds.http.environment;

import gov.nist.hit.ds.utilities.io.Io;

import java.io.File;
import java.io.IOException;

/**
 * Holds references to input header and body of the HTTP message.  Here the term
 * event corresponds to a transaction, an input to the current actor.
 * @author bill
 *
 */
public class EventLog {
	File eventLogDir;
	
	public EventLog(File eventLogDir) {
		this.eventLogDir = eventLogDir;
	}
	
	public File getHeaderFile() {
		return new File(eventLogDir,"request_hdr.txt");
	}
	
	public File getBodyFile() {
		return new File(eventLogDir, "request_body.bin");
	}
	
	public EventLog setHeader(String header) throws IOException {
		Io.stringToFile(getHeaderFile(), header);
		return this;
	}
	
	public EventLog setBody(byte[] body) throws IOException {
		Io.bytesToFile(getBodyFile(), body);
		return this;
	}

	public String getHeader() throws IOException {
		return Io.stringFromFile(getHeaderFile());
	}
	
	public byte[] getBody() throws IOException {
		return Io.bytesFromFile(getBodyFile());
	}
}
