package gov.nist.hit.ds.utilities.storage;

/**
 * Details of a stored document
 * @author bill
 *
 */
public class StoredDocumentInt {
	public String pathToDocument;
	public String uid;
	public String mimeType;
	public String charset;
	public String hash;
	public String size;
	
	public byte[] content;
	
	public StoredDocumentInt() {
		
	}
	

}
