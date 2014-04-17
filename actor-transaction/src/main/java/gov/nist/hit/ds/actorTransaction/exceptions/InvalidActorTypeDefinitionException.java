package gov.nist.hit.ds.actorTransaction.exceptions;

public class InvalidActorTypeDefinitionException extends InvalidActorTransactionTypeDefinition {

	private static final long serialVersionUID = 7901005128507897242L;
	public InvalidActorTypeDefinitionException(String msg) {
		super(msg);
	}
	public InvalidActorTypeDefinitionException(String msg, Exception e) {
		super(msg, e);
	}
}
