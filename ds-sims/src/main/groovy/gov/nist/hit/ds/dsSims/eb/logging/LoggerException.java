package gov.nist.hit.ds.dsSims.eb.logging;

import gov.nist.hit.ds.xdsExceptions.XdsInternalException;

public class LoggerException extends XdsInternalException
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
	
	/**TODO :
	 *   Define exception numbers 
	 */

}
