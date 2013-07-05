package gov.nist.hit.ds.simSupport.test.sims;

import gov.nist.hit.ds.simSupport.test.datatypes.Bar;
import gov.nist.hit.ds.simSupport.test.datatypes.Foo;

public class Base  {

	public Foo getFoo() { return new Foo("Created in Base"); }
	public Bar getBar() { return new Bar("Created in Base"); }
	
}
