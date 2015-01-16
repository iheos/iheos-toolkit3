package gov.nist.hit.ds.simSupport.config

import gov.nist.hit.ds.actorTransaction.TransactionType

/**
 * Created by bmajur on 1/16/15.
 */
class TTypes {
    List<TransactionType> transTypes;
    TTypes(List<TransactionType> types) { transTypes = types; }
    boolean has(TransactionType type) {
        if (transTypes == null) return false;
        for (int i=0; i<transTypes.size(); i++)
            if (type == transTypes.get(i)) return true;
        return false;
    }
}
