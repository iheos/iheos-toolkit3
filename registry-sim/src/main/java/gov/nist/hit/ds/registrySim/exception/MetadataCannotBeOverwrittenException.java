package gov.nist.hit.ds.registrySim.exception;

import gov.nist.hit.ds.xdsException.ToolkitException;

/**
 * Created by bmajur on 1/2/14.
 */
public class MetadataCannotBeOverwrittenException extends ToolkitException {
    public MetadataCannotBeOverwrittenException(String msg) {
        super(msg);
    }
}
