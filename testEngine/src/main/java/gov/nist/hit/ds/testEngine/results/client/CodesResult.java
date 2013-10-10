package gov.nist.hit.ds.testEngine.results.client;



import com.google.gwt.user.client.rpc.IsSerializable;

public class CodesResult implements IsSerializable {
	public Result result;
	public CodesConfiguration codesConfiguration;
	
	public CodesResult() {} // For GWT
}
