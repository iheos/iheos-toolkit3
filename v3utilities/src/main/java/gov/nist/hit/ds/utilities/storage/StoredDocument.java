package gov.nist.hit.ds.utilities.storage;

import gov.nist.hit.ds.utilities.io.Io;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class StoredDocument implements Serializable {

	
	private static final long serialVersionUID = 1L;
	StoredDocumentInt sdi;
//	public String pathToDocument;
//	public String uid;
//	public String mimeType;
//	public String charset;
//	public String hash;
//	public String size;
	
	transient public String cid;
	
	transient public byte[] content;
	
	public StoredDocument(StoredDocumentInt sdi) {
		this.sdi.pathToDocument = sdi.pathToDocument;
		this.sdi.uid = sdi.uid;
		this.sdi.mimeType = sdi.mimeType;
		this.sdi.charset = sdi.charset;
		this.sdi.hash = sdi.hash;
		this.sdi.size = sdi.size;
	}
	
	public StoredDocumentInt getStoredDocumentInt() {
		StoredDocumentInt sdint = new StoredDocumentInt();
		
		sdint.pathToDocument = sdi.pathToDocument;
		sdint.uid = sdi.uid;
		sdint.mimeType = sdi.mimeType;
		sdint.charset = sdi.charset;
		sdint.hash = sdi.hash;
		sdint.size = sdi.size;
		
		return sdint;
	}

	public File getFile() {
		return new File(sdi.pathToDocument);
	}
	
	public StoredDocument() {
		
	}
	
	public StoredDocument(String pathToDocument, String uid) {
		sdi.pathToDocument = pathToDocument;
		sdi.uid = uid;
	}
	
	public String getCharset() {
		return sdi.charset;
	}
	
	public void setMimetype(String mimeType) {
		sdi.mimeType = mimeType;
	}
	
	public String getMimetype() {
		return sdi.mimeType;
	}
	
	public void setHash(String hash) {
		sdi.hash = hash;
	}
	
	public void setSize(String size) {
		sdi.size = size;
	}
		
	public File getPathToDocument() {
		return new File(sdi.pathToDocument);
	}
	
	public byte[] getDocumentContents() throws IOException {
		File f = getPathToDocument();
		return Io.bytesFromFile(f);
	}

}
