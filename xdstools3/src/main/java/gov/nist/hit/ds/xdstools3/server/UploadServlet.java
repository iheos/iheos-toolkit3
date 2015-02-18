package gov.nist.hit.ds.xdstools3.server;

import gov.nist.hit.ds.http.parser.*;
import gov.nist.hit.ds.session.server.Session;
import gov.nist.hit.ds.xdsExceptions.ExceptionUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

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
			hp = new HttpParser();
            hp.appendixV = false;
                        hp.init(request);
//			filename = extractFileName(hp.getMultipartParser().getMultipartMessage().asMessage());
//			if(!(filename.endsWith(".xml") || filename.endsWith(".json"))){
//				throw new IOException("Invalid file format exception - must be xml or json");
//			}
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
            Session session = Session.getSession(request);
            logger.debug("UploadServlet: sessionId is " + session.getId());

			if (contentMap != null) {
                byte[] upload1 = contentMap.get("upload1FormElement");
                logger.debug("upload1 is " + ((upload1 == null) ? "null" : "not null"));
                byte[] upload2 = contentMap.get("upload2FormElement");
                logger.debug("upload2 is " + ((upload2 == null) ? "null" : "not null"));
				session.setLastUpload(
						"filename",
						upload1,
						asString(contentMap, "password1"),
						"filename",
						upload2,
						asString(contentMap, "password2"));
			}

		} catch (Exception e7) {
			logger.error(ExceptionUtil.exception_details(e7));
			body = e7.getMessage().getBytes();
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	String asString(Map<String, byte[]> map, String entry) {
		if (!map.containsKey(entry))
			return "";
		return new String(map.get(entry)).trim();
	}
	private String extractFileName(String multipartMessage) {
		String[] items = multipartMessage.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				String tmp = s.substring(s.indexOf("=") + 2, s.length()-1);
				return tmp.split("\"")[0];
			}
		}
		return "";
	}
}
