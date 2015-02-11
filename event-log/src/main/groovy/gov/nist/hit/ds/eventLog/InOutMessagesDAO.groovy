package gov.nist.hit.ds.eventLog
import gov.nist.hit.ds.repository.AssetHelper
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.repository.simple.SimpleType
import groovy.util.logging.Log4j

@Log4j
class InOutMessagesDAO {
	Asset inOutAsset;
	final static String reqHdrType = "reqHdrType";
	final static String reqBodyType = "reqBodyType";
	final static String resHdrType = "resHdrType";
    final static String resBodyType = "resBodyType";

	def init(Asset parent) throws RepositoryException {
		inOutAsset = AssetHelper.createChildAsset(parent, "Input/Output Messages", "", new SimpleType("simInOut"));
        inOutAsset
	}

    InOutMessages load(Asset inOutAsset) {
        this.inOutAsset = inOutAsset
        InOutMessages iom = new InOutMessages()
        iom.with {
            reqHdr = getRequestHeader()
            reqBody = getRequestBody()
        }
        iom
    }

    // can be called multiple times - keeps track of what has been flushed to disk
    void save(InOutMessages iom) {
        iom.with {
            log.debug("Saving IO Messages - reqBody = ${reqBody != null} reqBodySaved = ${reqBodySaved}")
            if (reqHdr && !reqHdrSaved) { putRequestHeader(reqHdr); reqHdrSaved = true }
            if (reqBody && !reqBodySaved) { putRequestBody(reqBody); reqBodySaved = true }
            if (respHdr && !respHdrSaved) { putResponseHeader(respHdr); respHdrSaved = true }
            if (respBody && !respBodySaved) { putResponseBody(respBody);  respBodySaved = true }
        }
    }

	public void putRequestHeader(String hdr) throws RepositoryException {
//		log.debug("Header\n" + hdr);
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Request Header", "", new SimpleType(reqHdrType));
		a.setOrder(1);
		a.setContent(hdr, "text/plain");
	}
	
//	public String getRequestHeader() throws RepositoryException {
//		AssetIterator ai = inOutAsset.getAssetsByType(new SimpleType(reqHdrType));
//		if (!ai.hasNextAsset())
//			throw new RepositoryException("Error Retrieving request message from Repository");
//		return new String(ai.nextAsset().getContent());
//	}

	public void putRequestBody(byte[] bytes) throws RepositoryException {
        log.debug("Request body saved")
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Request Body", "", new SimpleType(reqBodyType));
		a.setOrder(2);
        a.setMimeType("application/octet-stream");
		a.updateContent(bytes);
	}
	
//	public byte[] getRequestBody()  throws RepositoryException {
//		AssetIterator ai = inOutAsset.getAssetsByType(new SimpleType(reqBodyType));
//		if (!ai.hasNextAsset())
//			throw new RepositoryException("Error Retrieving request message from Repository");
//		return ai.nextAsset().getContent();
//	}
	
	public void putResponseHeader(String hdr) throws RepositoryException {
		log.debug("Response\n" + hdr);
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Response Header", "", new SimpleType(resHdrType));
		a.setOrder(3);
		a.setContent(hdr, "text/plain");
	}

    public void putResponseBody(byte[] bytes) throws RepositoryException {
        log.debug("Response body saved")
        Asset a
        try {
            a = inOutAsset.getChildByName("Response Body")
        } catch (RepositoryException e) {
            a = AssetHelper.createChildAsset(inOutAsset, "Response Body", "", new SimpleType(resBodyType));
            a.setOrder(2);
            a.setMimeType("application/octet-stream");
        }
        a.updateContent(bytes);
    }

//    public String getResponseHeader() throws RepositoryException {
//		AssetIterator ai = inOutAsset.getAssetsByType(new SimpleType(resHdrType));
//		if (!ai.hasNextAsset())
//			throw new RepositoryException("Error Retrieving response header from Repository");
//		return new String(ai.nextAsset().getContent());
//	}

}
