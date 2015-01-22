package gov.nist.hit.ds.simSupport.config

import gov.nist.hit.ds.actorTransaction.TlsType

/**
 * Created by bmajur on 1/16/15.
 */
class Tls {
    List<TlsType> tlsTypes;
    Tls(List<TlsType> types) { tlsTypes = types; }
    boolean has(TlsType type) {
        if (tlsTypes == null) return false;
        for (int i=0; i<tlsTypes.size(); i++)
            if (type == tlsTypes.get(i)) return true;
        return false;
    }
}
