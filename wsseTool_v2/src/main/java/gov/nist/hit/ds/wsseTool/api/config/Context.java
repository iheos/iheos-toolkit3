package gov.nist.hit.ds.wsseTool.api.config;


import java.util.Map;

public interface Context {

	public void setParam(String key, Object value);
	
	public Map<String,Object> getParams();
	
	public Object getParam(String param);

}
