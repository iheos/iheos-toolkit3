package gov.nist.hit.ds.simSupport.test.datatypes;

public class Foo {
	public String name = "I am Foo";
	String value;
	
	public Foo(String value) {
		this.value = value;
	}
	
	public String toString() { return name; }

	public String getValue() { return value; }
}
