package gov.nist.hit.ds.actorTransaction.exceptions;

public class InvalidTransactionTypeDefinitionException extends InvalidActorTransactionTypeDefinition {
	
	private static final long serialVersionUID = 2353483267204536645L;

	public InvalidTransactionTypeDefinitionException(String msg) {
		super(msg);
	}
	public InvalidTransactionTypeDefinitionException(String msg, Exception e) {
		super(msg, e);
	}
}
