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

	public KeystoreAccess getKeystore() {
		return keystore;
	}

	public void setKeystore(KeystoreAccess keystore) {
		this.keystore = keystore;
	}

	@Override
	public void setParam(String key, Object value) {
		params.put(key, value);
	}

	@Override
	public Object getParam(String param) {
		return params.get(param);
	}
}
