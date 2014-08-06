package gov.nist.hit.ds.wsseTool.api.exceptions;

/**
 * Exception throw for any problem occuring during the headers generation.
 * In any case, it seems that we cannot do much for all of the underlying exceptions
 * touching either IO, marhsalling or Security...
 * 
 * @author gerardin
 *
 */

public class ValidationException extends Exception {

	public ValidationException(Exception e) {
		super(e);
	}
	
	public ValidationException(String message) {
		super(message);
	}
	
	public ValidationException(String message, Exception e) {
		super(message, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -221771437402541989L;

}
