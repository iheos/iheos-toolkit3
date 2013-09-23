package gov.nist.toolkit.wsseTool.api.config;


public class ContextFactory {

	public static Context getInstance(){
		return new GenContext();
	}

}
