package gov.nist.hit.ds.actorTransaction

/**
 * Used by ActorType. Transactions are only
 * directional from the point of view of Actors
 * Created by bmajur on 1/21/15.
 */
class DirectionalTransactionType {
    TransactionType transactionType
    boolean send  // send|receive

    DirectionalTransactionType() {}

    DirectionalTransactionType(TransactionType _transactionType, boolean _send) { transactionType = _transactionType; send = _send }

}
