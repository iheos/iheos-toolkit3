package gov.nist.hit.ds.simSupport.test.datatypes;

public class Bar {
	public String name = "I am Bar";
	String value;
	
	public Bar(String value) {
		this.value = value;
	}

	public String toString() { return name; }
	
	public String getValue() { return value; }
}
