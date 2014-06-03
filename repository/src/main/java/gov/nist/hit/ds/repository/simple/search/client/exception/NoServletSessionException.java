package gov.nist.hit.ds.repository.simple.search.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class NoServletSessionException extends Exception  implements IsSerializable{

	/**
	 * Imported from v2
	 */
	private static final long serialVersionUID = 1L;
	
	public NoServletSessionException(String msg) {
		super(msg);
	}

	public NoServletSessionException() {}

}
