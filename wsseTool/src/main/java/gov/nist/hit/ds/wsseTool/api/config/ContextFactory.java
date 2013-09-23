package gov.nist.hit.ds.wsseTool.api.config;


public class ContextFactory {

	public static Context getInstance(){
		return new GenContext();
	}

}
