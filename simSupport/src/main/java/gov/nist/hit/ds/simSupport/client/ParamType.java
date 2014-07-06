package gov.nist.hit.ds.simSupport.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum ParamType implements IsSerializable {
	OID,
	ENDPOINT,
	TEXT,
	BOOLEAN,
	TIME,
	SELECTION;
	
	ParamType() {} // for GWT
}