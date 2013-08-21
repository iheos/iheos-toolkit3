package gov.nist.hit.ds.eventLog;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.SimpleType;

import java.io.IOException;

public class InOutMessages {
	Asset inOutAsset;
	
	Asset init(Asset parent) throws RepositoryException {
		inOutAsset = AssetHelper.createChildAsset(parent, "Input/Output Messages", "", new SimpleType("simInOut"));
		return inOutAsset;
	}

	public void putRequestHeader(String hdr) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Request Header", "", new SimpleType("simpleType"));
		AssetHelper.setOrder(a, 1);
		a.updateContent(hdr, "text/plain");
	}
	
	public String getRequestHeader() throws RepositoryException {
		return null;
	}

	public void putRequestBody(byte[] bytes) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Request Body", "", new SimpleType("simpleType"));
		AssetHelper.setOrder(a, 2);
		a.updateContent(bytes);
		AssetHelper.setMimeType(a, "application/octet-stream");
	}
	
	public byte[] getRequestBody()  throws RepositoryException {
		return null;
	}
	
	public void putResponseHeader(String hdr) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Response Header", "", new SimpleType("simpleType"));
		AssetHelper.setOrder(a, 3);
		a.updateContent(hdr, "text/plain");
	}

	public String getResponseHeader() throws IOException {
		return null;
	}

	public void putResponsetBody(byte[] bytes) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Response Body", "", new SimpleType("simpleType"));
		AssetHelper.setOrder(a, 4);
		a.updateContent(bytes);
		AssetHelper.setMimeType(a, "application/octet-stream");
	}
	

	public byte[] getResponseBody()  throws RepositoryException {
		return null;
	}

}