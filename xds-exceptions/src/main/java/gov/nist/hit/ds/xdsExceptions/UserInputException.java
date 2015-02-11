package gov.nist.hit.ds.xdsExceptions;

/**
 * Created by dazais on 10/17/2014.
 */
public class UserInputException extends ToolkitRuntimeException {

    private static final long serialVersionUID = 1L;


    public UserInputException(String msg) {
        super(msg);
    }


    public UserInputException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserInputException(Throwable cause) {
        super(cause);
    }
}
