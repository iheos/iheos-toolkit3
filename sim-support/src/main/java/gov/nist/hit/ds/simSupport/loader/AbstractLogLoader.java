package gov.nist.hit.ds.simSupport.loader;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.Assertion;
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus;
import gov.nist.hit.ds.http.parser.HttpParseException;
import gov.nist.hit.ds.http.parser.HttpParserBa;
import gov.nist.hit.ds.http.parser.ParseException;
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase;
import gov.nist.hit.ds.utilities.html.HttpMessageContent;
import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.utilities.xml.XmlText;
import gov.nist.hit.ds.xdsExceptions.ExceptionUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * Classes that extend this class must set the File dir field to 
 * establish where the input data is to be found OR set header
 * and body with the actual values.
 * @author bmajur
 *
 */
public abstract class AbstractLogLoader extends ValComponentBase {
	File dir;
	String header = null;
	byte[] body = null;
	static Logger logger = Logger.getLogger(AbstractLogLoader.class);

    public AbstractLogLoader() {
        super(new Event(null));
    }

	@Override
	public void run() {
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
            Assertion a = new Assertion();
            a.setStatus(AssertionStatus.INTERNALERROR);
            a.setFound(ExceptionUtil.exception_details(e));
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
