package gov.nist.hit.ds.simSupport.sims;

import gov.nist.hit.ds.simSupport.datatypes.Bar;
import gov.nist.hit.ds.simSupport.datatypes.Foo;

public class Base  {

	public Foo getFoo() { return new Foo("Created in Base"); }
	public Bar getBar() { return new Bar("Created in Base"); }
	
}
