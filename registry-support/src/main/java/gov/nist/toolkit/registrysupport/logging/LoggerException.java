package gov.nist.toolkit.registrysupport.logging;

import gov.nist.hit.ds.xdsException.XdsInternalException;

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
