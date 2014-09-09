package gov.nist.toolkit.xdstools3.client.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

public class NoServletSessionException extends Exception  implements IsSerializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public NoServletSessionException(String msg) {
		super(msg);
	}

	public NoServletSessionException() {}

    public NoServletSessionException(Throwable caught) {
        super(caught.getMessage());
    }

}
