package gov.nist.hit.ds.actorTransaction.exceptions;

public class ActorTypeNotDefinedException extends InvalidActorTransactionTypeDefinition {

	private static final long serialVersionUID = -3484922773035076707L;
	public ActorTypeNotDefinedException(String msg) {
		super(msg);
	}
	public ActorTypeNotDefinedException(String msg, Exception e) {
		super(msg, e);
	}
}
