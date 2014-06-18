package gov.nist.hit.ds.actorTransaction.exceptions;

public class TransactionTypeNotDefinedException extends InvalidActorTransactionTypeDefinition {

	private static final long serialVersionUID = 5330486915476971099L;
	public TransactionTypeNotDefinedException(String msg) {
		super(msg);
	}
	public TransactionTypeNotDefinedException(String msg, Exception e) {
		super(msg, e);
	}
}
