package gov.nist.hit.ds.xdsException;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EnvironmentNotSelectedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EnvironmentNotSelectedException(String msg) {
		super(msg);
	}

	public EnvironmentNotSelectedException(String msg, Exception e) {
		super(msg, e);
	}

}
