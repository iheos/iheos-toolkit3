package gov.nist.hit.ds.simSupport.transaction;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.simSupport.engine.SimComponent;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.utilities.html.HttpMessageContent;
import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.utilities.xml.XmlText;

import java.io.File;
import java.io.IOException;

/**
 * Load HTTP event from log format - 2 files
 * 	- test file with header - request_hdr.txt
 *  - byte array file with body  - request_body.bin
 * @author bmajur
 *
 */
public class LogLoader implements SimComponent {
	File dir;
	String header = null;
	byte[] body = null;
	ErrorRecorder er;

	public LogLoader() {
	}
	
	public LogLoader setSource(File dir) {
		this.dir = dir;
		return this;
	}

	public LogLoader setSource(String dir) {
		this.dir = new File(dir);
		return this;
	}

	LogLoader load()   {
		er.detail("Loading from <" + dir + ">");
		try {
			header = Io.stringFromFile(new File(dir, "request_hdr.txt"));
			if (new File(dir, "request_body.txt").exists())
				body = Io.stringFromFile(new File(dir, "request_body.txt")).getBytes();
			else
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
	
	/**
	 * No guarantee the body is XML.  But since this loader
	 * is use for testing, let the using code throw the 
	 * exceptions if it is not xml.
	 * @return
	 */
	public XmlText getXmlText() {
		return new XmlText().setXml(body);
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

	@Override
	public String getDescription() {
		return "Load a pre-existing log file as input to the simulator";
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}
}
