package gov.nist.hit.ds.wsseTool.api.config;

import gov.nist.hit.ds.wsseTool.validation.engine.TestData;

import java.util.HashMap;
import java.util.Map;

public class GenContext implements Context, TestData {

	protected Map<String,Object> params = new HashMap<String,Object>();
	
	protected KeystoreAccess keystore;

	@Override
	public Map<String,Object> getParams() {
		return params;
	}

	@Override
	public KeystoreAccess getKeystore() {
		return keystore;
	}

	@Override
	public void setKeystore(KeystoreAccess keystore) {
		this.keystore = keystore;
	}

	@Override
	public void setParam(String key, Object value) {
		params.put(key, value);
	}
}
