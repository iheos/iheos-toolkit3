package gov.nist.hit.ds.simServlet
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.utilities.storage.StoredDocument
/**
 * Created by bmajur on 10/16/14.
 */
class WrapMtom {
    public String wrap(String envelope, StringBuilder bodyBuffer) {

        // build body
        String boundary = "MIMEBoundary112233445566778899";
        StringBuilder contentTypeBuffer = new StringBuilder();
        Map<String, StoredDocument> documentsToAttach = null;  // cid => document
        String rn = "\r\n";

        contentTypeBuffer
                .append("multipart/related")
                .append("; boundary=")
                .append(boundary)
                .append(";  type=\"application/xop+xml\"")
                .append("; start=\"<" + mkCid(0) + ">\"")
                .append("; start-info=\"application/soap+xml\"");

//        response.setHeader("Content-Type", contentTypeBuffer.toString());

        bodyBuffer.append("--").append(boundary).append(rn);
        bodyBuffer.append("Content-Type: application/xop+xml; charset=UTF-8; type=\"application/soap+xml\"").append(rn);
        bodyBuffer.append("Content-Transfer-Encoding: binary").append(rn);
        bodyBuffer.append("Content-ID: <" + mkCid(0) + ">").append(rn);
        bodyBuffer.append(rn);

        bodyBuffer.append(envelope);

        bodyBuffer.append(rn);
        bodyBuffer.append(rn);

        if (documentsToAttach != null) {
//            er.detail("Attaching " + documentsToAttach.size() + " documents as separate Parts in the Multipart");
            for (String cid : documentsToAttach.keySet()) {
                StoredDocument sd = documentsToAttach.get(cid);

                bodyBuffer.append("--").append(boundary).append(rn);
                bodyBuffer.append("Content-Type: ").append(sd.mimeType).append(rn);
                bodyBuffer.append("Content-Transfer-Encoding: binary").append(rn);
                bodyBuffer.append("Content-ID: <" + cid + ">").append(rn);
                bodyBuffer.append(rn);
                try {
                    String contents;
                    if (sd.charset != null) {
                        contents = new String(sd.getDocumentContents(), sd.charset);
                    } else {
                        contents = new String(sd.getDocumentContents());
                    }
                    bodyBuffer.append(contents);
                } catch (Exception e) {
                    // TODO: fix this
                    er.err(XdsErrorCode.Code.XDSRepositoryError, e);
                }
                bodyBuffer.append(rn);
            }
        }


        bodyBuffer.append("--").append(boundary).append("--").append(rn);

        return contentTypeBuffer.toString();
    }

    String mkCid(int i) {
        return "doc" + i +"@ihexds.nist.gov";
    }

}


