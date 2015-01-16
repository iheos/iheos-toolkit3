package gov.nist.hit.ds.simSupport.config

import gov.nist.hit.ds.actorTransaction.AsyncType

/**
 * Created by bmajur on 1/16/15.
 */
class Async {
    List<AsyncType> asyncTypes;
    Async(List<AsyncType> types) { asyncTypes = types; }
    boolean has(AsyncType type) {
        if (asyncTypes == null) return false;
        for (int i=0; i<asyncTypes.size(); i++)
            if (type == asyncTypes.get(i)) return true;
        return false;
    }
}
