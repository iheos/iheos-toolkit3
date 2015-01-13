package gov.nist.hit.ds.xdsExceptions;

/**
 * Created by bmajur on 1/2/14.
 */
public class ToolkitException extends Exception {
    public ToolkitException(String msg) {
        super(msg);
    }
    public ToolkitException(String msg, Exception e) {
        super(msg, e);
    }
}
