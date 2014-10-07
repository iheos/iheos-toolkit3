package gov.nist.hit.ds.xdsException;

public class ToolkitRuntimeException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public ToolkitRuntimeException(String msg) {
		super(msg);
	}


	public ToolkitRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

    public ToolkitRuntimeException(Throwable cause) {
        super(cause);
    }
}
