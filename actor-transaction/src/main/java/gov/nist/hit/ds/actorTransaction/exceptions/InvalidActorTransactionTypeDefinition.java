package gov.nist.hit.ds.actorTransaction.exceptions;

import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException;

public class InvalidActorTransactionTypeDefinition extends ToolkitRuntimeException {
	private static final long serialVersionUID = -1594140646987298373L;

	public InvalidActorTransactionTypeDefinition(String msg) {
		super(msg);
	}
	public InvalidActorTransactionTypeDefinition(String msg, Exception e) {
		super(msg, e);
	}
}
