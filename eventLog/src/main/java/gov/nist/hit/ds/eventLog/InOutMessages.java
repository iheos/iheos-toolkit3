package gov.nist.hit.ds.eventLog;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.utilities.io.Io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class InOutMessages {
	Asset inOutAsset;
	
	Asset init(Asset parent) throws RepositoryException {
		inOutAsset = AssetHelper.createChildAsset(parent, "Input/Output Messages", null, new SimpleType("simInOut"));
		return inOutAsset;
	}

	public void putRequestHeader(String hdr) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Request Header", null, new SimpleType("simpleType"));
		AssetHelper.setOrder(a, 1);
		a.updateContent(hdr, "text/plain");
	}
	
	public String getRequestHeader() throws RepositoryException {
		return null;
	}

	public void putRequestBody(byte[] bytes) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Request Body", null, new SimpleType("simpleType"));
		AssetHelper.setOrder(a, 2);
		a.updateContent(bytes);
		AssetHelper.setMimeType(a, "application/octet-stream");
	}
	
	public byte[] getRequestBody()  throws RepositoryException {
		return null;
	}
	
	public void putResponseHeader(String hdr) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Response Header", null, new SimpleType("simpleType"));
		AssetHelper.setOrder(a, 3);
		a.updateContent(hdr, "text/plain");
	}

	public String getResponseHeader() throws IOException {
		return null;
	}

	public void putResponsetBody(byte[] bytes) throws RepositoryException {
		Asset a = AssetHelper.createChildAsset(inOutAsset, "Response Body", null, new SimpleType("simpleType"));
		AssetHelper.setOrder(a, 4);
		a.updateContent(bytes);
		AssetHelper.setMimeType(a, "application/octet-stream");
	}
	

	public byte[] getResponseBody()  throws RepositoryException {
		return null;
	}

}