package gov.nist.hit.ds.simSupport.loader;

import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.http.parser.HttpParseException;
import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.utilities.html.HttpMessageContent;
import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.utilities.xml.XmlText;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * Classes that extend this class must set the File dir field to 
 * establish where the input data is to be found.
 * @author bmajur
 *
 */
public abstract class AbstractLogLoader extends SimComponentBase {
	File dir;
	String header = null;
	byte[] body = null;
	static Logger logger = Logger.getLogger(AbstractLogLoader.class);

	@Override
	public void run(MessageValidatorEngine mve) {
		if (header == null && body == null)
			load();
	}

	void load()   {
		if (header != null && body != null)
			return;
		if (dir == null)
			logger.error("Loading from <" + dir + ">");
		else
			logger.info("Loading from <" + dir + ">");
		try {
			header = Io.stringFromFile(new File(dir, "request_hdr.txt"));
			if (new File(dir, "request_body.txt").exists())
				body = Io.stringFromFile(new File(dir, "request_body.txt")).getBytes();
			else
				body = Io.bytesFromFile(new File(dir, "request_body.bin"));
		} catch (IOException e) {
			ag.err(Code.NoCode, e);
		}
	}

	public HttpMessageContent getMessageContent() {
		return new HttpMessageContent().
				setBody(body).
				setHeader(header);
	}

	public HttpServletRequest getServletRequest() throws HttpParseException, ParseException, IOException {
		return new HttpParserBa(getMessageContent().getBytes()).getHttpMessage();
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
}
