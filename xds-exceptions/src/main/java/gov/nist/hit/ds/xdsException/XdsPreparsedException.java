package gov.nist.hit.ds.xdsException;

public class XdsPreparsedException extends ToolkitRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public XdsPreparsedException(String msg) {
		super(msg);
	}
	public XdsPreparsedException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
