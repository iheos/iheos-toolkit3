package gov.nist.hit.ds.wsseTool.parsing;

public class ParseException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5467957056311402151L;

	public ParseException(String message) {
		super(message);
	}

	public ParseException(String message, Exception e) {
		super(message , e);
	}


}
