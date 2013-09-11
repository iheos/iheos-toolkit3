package gov.nist.hit.ds.eventLog;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.SimpleType;

public class InOutMessages {
	Asset inOutAsset;
	final static String reqHdrType = "reqHdrType";
	final static String reqBodyType = "reqBodyType";
	final static String resType = "resType";
	
	Asset init(Asset parent) throws RepositoryException {
		inOutAsset = AssetHelper.createChildAsset(parent, "Input/Output Messages", "", new SimpleType("simInOut"));
		return inOutAsset;
	}

	public void putRequestHeader(String hdr) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Request Header", "", new SimpleType(reqHdrType));
		AssetHelper.setOrder(a, 1);
		a.updateContent(hdr, "text/plain");
	}
	
	public String getRequestHeader() throws RepositoryException {
		AssetIterator ai = inOutAsset.getAssetsByType(new SimpleType(reqHdrType));
		if (!ai.hasNextAsset())
			throw new RepositoryException("Error Retrieving request message from Repository");
		return new String(ai.nextAsset().getContent());
	}

	public void putRequestBody(byte[] bytes) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Request Body", "", new SimpleType(reqBodyType));
		AssetHelper.setOrder(a, 2);
		a.updateContent(bytes);
		AssetHelper.setMimeType(a, "application/octet-stream");
	}
	
	public byte[] getRequestBody()  throws RepositoryException {
		AssetIterator ai = inOutAsset.getAssetsByType(new SimpleType(reqBodyType));
		if (!ai.hasNextAsset())
			throw new RepositoryException("Error Retrieving request message from Repository");
		return ai.nextAsset().getContent();
	}
	
	public void putResponse(String response) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Response", "", new SimpleType(resType));
		AssetHelper.setOrder(a, 3);
		a.updateContent(response, "text/plain");
	}

	public String getResponse() throws RepositoryException {
		AssetIterator ai = inOutAsset.getAssetsByType(new SimpleType(resType));
		if (!ai.hasNextAsset())
			throw new RepositoryException("Error Retrieving response message from Repository");
		return new String(ai.nextAsset().getContent());
	}

}