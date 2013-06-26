package gov.nist.hit.ds.http;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.utilities.storage.StoredDocument;
import gov.nist.hit.ds.utilities.xml.OMFormatter;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.axiom.om.OMElement;


public class HttpBodyGenerator {
	Map<String, StoredDocument> documentsToAttach = null;   // cid => document
	
	public String addBody(HttpServletResponse response, OMElement env, Map<String, StoredDocument> documentsToAttach, ErrorRecorder er)  {
		if (documentsToAttach != null) {
			return wrapXMLInMultipartResponse(response, new OMFormatter(env).toString(), er).toString();  
		} else {
			return new OMFormatter(env).toString();
		}
	}
	
	StringBuffer wrapXMLInMultipartResponse(HttpServletResponse response, String env, ErrorRecorder er) {

		// build body
		String boundary = "MIMEBoundary112233445566778899";
		StringBuffer contentTypeBuffer = new StringBuffer();
		String rn = "\r\n";

		contentTypeBuffer
		.append("multipart/related")
		.append("; boundary=")
		.append(boundary)
		.append(";  type=\"application/xop+xml\"")
		.append("; start=\"<" + mkCid(0) + ">\"")
		.append("; start-info=\"application/soap+xml\"");

		response.setHeader("Content-Type", contentTypeBuffer.toString());

		StringBuffer body = new StringBuffer();

		body.append("--").append(boundary).append(rn);
		body.append("Content-Type: application/xop+xml; charset=UTF-8; type=\"application/soap+xml\"").append(rn);
		body.append("Content-Transfer-Encoding: binary").append(rn);
		body.append("Content-ID: <" + mkCid(0) + ">").append(rn);
		body.append(rn);

		body.append(env.toString());

		body.append(rn);
		body.append(rn);

		if (documentsToAttach != null) {
			er.detail("Attaching " + documentsToAttach.size() + " documents as separate Parts in the Multipart");
			for (String cid : documentsToAttach.keySet()) {
				StoredDocument sd = documentsToAttach.get(cid);

				body.append("--").append(boundary).append(rn);
				body.append("Content-Type: ").append(sd.getMimetype()).append(rn);
				body.append("Content-Transfer-Encoding: binary").append(rn);
				body.append("Content-ID: <" + cid + ">").append(rn);
				body.append(rn);
				try {
					String contents;
					if (sd.getCharset() != null) {
						contents = new String(sd.getDocumentContents(), sd.getCharset());
					} else {
						contents = new String(sd.getDocumentContents());
					}
					body.append(contents);
				} catch (Exception e) {
					er.err(XdsErrorCode.Code.XDSRepositoryError, e);
				}
				body.append(rn);
			}
		}


		body.append("--").append(boundary).append("--").append(rn);

		return body;
	}

	String mkCid(int i) {
		return "doc" + i +"@ihexds.nist.gov";
	}

	
}
