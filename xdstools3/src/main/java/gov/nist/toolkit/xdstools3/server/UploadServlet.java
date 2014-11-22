package gov.nist.toolkit.xdstools3.server;

import gov.nist.toolkit.http.HttpMessage;
import gov.nist.toolkit.http.HttpParseException;
import gov.nist.toolkit.http.HttpParser;
import gov.nist.toolkit.http.MultipartMessage;
import gov.nist.toolkit.http.MultipartParser;
import gov.nist.toolkit.session.server.Session;
import gov.nist.hit.ds.xdsException.ExceptionUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class UploadServlet extends HttpServlet {

	static final Logger logger = Logger.getLogger(UploadServlet.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws IOException {
		byte[] body = null;
		String filename = null;
		byte[] body2 = null;
		String filename2 = null;
		Map<String, byte[]> contentMap = null;

		HttpParser hp;
		try {
			hp = new HttpParser(request, false);
			byte[] bodybytes = hp.getBody();
		} catch (HttpParseException e1) {
			logger.error("HTTPParser parse error: " + e1.getMessage());
			throw new IOException("Parse Error: " + e1.getMessage());
		}
		catch (RuntimeException e) {
			logger.error(ExceptionUtil.exception_details(e));
			throw new IOException("Parse Error: " + e.getMessage());
		}
		if (hp.isMultipart()) {
			logger.debug("Parse servlet input - is a multipart");

			MultipartParser mp = hp.getMultipartParser();
			MultipartMessage mm = mp.getMultipartMessage();

			contentMap = mm.getContentMap();
		} else {
			HttpMessage hm = hp.getHttpMessage();
//			logger.debug(hm.toString());
//			String bdy = hm.getBody();
//			System.out.println("body=" + bdy);
			logger.error("Cannot parse servlet input - not a multipart");
			//throw new IOException("Cannot parse servlet input - not a multipart");
		}

		try {
			HttpSession hsession = request.getSession();
			Session session = (Session) hsession.getAttribute("MySession");
			if (session == null) {
				PrintWriter pw = response.getWriter();
				pw.println("No Session established - that's impossible!");
				return;
			}

			if (contentMap != null) {
				session.setLastUpload(
						"filename",
						contentMap.get("upload1FormElement"),
						asString(contentMap, "password1"),
						"filename",
						contentMap.get("upload2FormElement"),
						asString(contentMap, "password2"));
			}

		} catch (Exception e7) {
			logger.error("Exception: " + e7.getMessage());
			body = e7.getMessage().getBytes();
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	String asString(Map<String, byte[]> map, String entry) {
		if (!map.containsKey(entry))
			return "";
		return new String(map.get(entry)).trim();
	}

}
