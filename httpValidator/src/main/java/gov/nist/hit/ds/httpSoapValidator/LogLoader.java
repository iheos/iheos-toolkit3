package gov.nist.hit.ds.httpSoapValidator;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.simSupport.SimFailureException;
import gov.nist.hit.ds.simSupport.engine.ValSim;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.utilities.io.Io;

import java.io.File;
import java.io.IOException;

/**
 * Load HTTP event from log format - 2 files
 * 	- test file with header - request_hdr.txt
 *  - byte array file with body  - request_body.bin
 * @author bmajur
 *
 */
public class LogLoader implements ValSim {
	File dir;
	String header = null;
	byte[] body = null;
	ErrorRecorder er;

	public LogLoader(File dir) {
		this.dir = dir;
	}

	LogLoader load()   {
		er.detail("Loading from <" + dir + ">");
		try {
			header = Io.stringFromFile(new File(dir, "request_hdr.txt"));
			body = Io.bytesFromFile(new File(dir, "request_body.bin"));
		} catch (IOException e) {
			er.err(Code.NoCode, e);
		}
		return this;
	}

	public HttpMessageContent getMessageContent() {
		return new HttpMessageContent().
				setBody(body).
				setHeader(header);
	}

	@Override
	public void setErrorRecorder(ErrorRecorder er) {
		this.er = er;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void run(MessageValidatorEngine mve) {
		load();
	}
}
