package gov.nist.hit.ds.utilities.io;


import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException;

public class Hash {
	public String compute_hash(ByteBuffer buffer) throws ToolkitRuntimeException {
		Sha1Bean sha = new Sha1Bean();
		sha.setByteStream(buffer.get());
		String hash = null;
		try {
			hash = sha.getSha1String();
		}
		catch (Exception e) {
			ToolkitRuntimeException ne = new ToolkitRuntimeException(e.getMessage());
			ne.setStackTrace(e.getStackTrace());
			throw ne;
		}
		return hash;
	}
	
	public String compute_hash(String doc)  throws ToolkitRuntimeException {
		return compute_hash(doc.getBytes());
	}

	public String compute_hash(byte[] bytes)  throws ToolkitRuntimeException {
		ByteBuffer b = new ByteBuffer();
		b.append(bytes, 0, bytes.length);
		return compute_hash(b);
	}
}
