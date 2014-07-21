package gov.nist.hit.ds.registrysupport.logging;

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

public class LoggerException extends ToolkitRuntimeException 
{
	
	public LoggerException(String string)
	{
		super(string);
	}
	
	public LoggerException(String string, Exception e) {
		super(string, e);
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

}
