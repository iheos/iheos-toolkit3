package gov.nist.hit.ds.repository.simple.search.client.exception;


import com.google.gwt.user.client.rpc.IsSerializable;

public class RepositoryConfigException extends Exception implements IsSerializable {

	/**
	 * Imported from v2
	 */
	private static final long serialVersionUID = -2910306789199740013L;
	
	
	public RepositoryConfigException() {}
	

	public RepositoryConfigException(String msg) {
		super(msg);
	}

	public RepositoryConfigException(String msg, Exception e) {
		super(msg, e);
	}


}
