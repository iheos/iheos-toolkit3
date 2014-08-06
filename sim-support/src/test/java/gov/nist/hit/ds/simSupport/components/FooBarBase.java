package gov.nist.hit.ds.simSupport.components;

import gov.nist.hit.ds.soapSupport.core.SoapEnvironment;

public class FooBarBase extends SoapEnvironment {

	public Foo getFoo() { return new Foo("Created in FooBarBase"); }
	public Bar getBar() { return new Bar("Created in FooBarBase"); }
	
}
