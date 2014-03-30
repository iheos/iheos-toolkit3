package gov.nist.hit.ds.actorTransaction.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Created by bmajur on 3/29/14.
 */
public enum ParamType implements IsSerializable, Serializable {
    OID,
    ENDPOINT,
    TEXT,
    BOOLEAN,
    TIME,
    SELECTION;

    ParamType() {} // for GWT
}
