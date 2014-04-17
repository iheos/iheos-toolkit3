package gov.nist.hit.ds.eventLog;

import gov.nist.hit.ds.repository.AssetHelper;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.SimpleType;
import org.apache.log4j.Logger;

class InOutMessagesDAO {
	Asset inOutAsset;
	final static String reqHdrType = "reqHdrType";
	final static String reqBodyType = "reqBodyType";
	final static String resType = "resType";
	static Logger logger = Logger.getLogger(InOutMessagesDAO.class);

	def init(Asset parent) throws RepositoryException {
		inOutAsset = AssetHelper.createChildAsset(parent, "Input/Output Messages", "", new SimpleType("simInOut"));
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

    void save(InOutMessages iom, Asset inOutAsset) {
        this.inOutAsset = inOutAsset
        iom.with {
            putRequestHeader(reqHdr)
            putRequestBody(reqBody)
        }
    }

	public void putRequestHeader(String hdr) throws RepositoryException {
		logger.debug("Header\n" + hdr);
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Request Header", "", new SimpleType(reqHdrType));
		a.setOrder(1);
		a.updateContent(hdr, "text/plain");
	}
	
	public String getRequestHeader() throws RepositoryException {
		AssetIterator ai = inOutAsset.getAssetsByType(new SimpleType(reqHdrType));
		if (!ai.hasNextAsset())
			throw new RepositoryException("Error Retrieving request message from Repository");
		return new String(ai.nextAsset().getContent());
	}

	public void putRequestBody(byte[] bytes) throws RepositoryException {
		logger.debug("Body\n" + new String(bytes));
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Request Body", "", new SimpleType(reqBodyType));
		a.setOrder(2);
		a.updateContent(bytes);
		a.setMimeType("application/octet-stream");
	}
	
	public byte[] getRequestBody()  throws RepositoryException {
		AssetIterator ai = inOutAsset.getAssetsByType(new SimpleType(reqBodyType));
		if (!ai.hasNextAsset())
			throw new RepositoryException("Error Retrieving request message from Repository");
		return ai.nextAsset().getContent();
	}
	
	public void putResponse(String response) throws RepositoryException {
		logger.debug("Response\n" + response);
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Response", "", new SimpleType(resType));
		a.setOrder(3);
		a.updateContent(response, "text/plain");
	}

	public String getResponse() throws RepositoryException {
		AssetIterator ai = inOutAsset.getAssetsByType(new SimpleType(resType));
		if (!ai.hasNextAsset())
			throw new RepositoryException("Error Retrieving response message from Repository");
		return new String(ai.nextAsset().getContent());
	}

}