package gov.nist.toolkit.wsseTool.api.config;

import gov.nist.toolkit.wsseTool.context.SecurityContextImpl;

public class SecurityContextFactory {

	public static SecurityContext getInstance(){
		return new SecurityContextImpl();
	}

}
