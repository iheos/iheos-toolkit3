package gov.nist.hit.ds.simSupport.config

public class BooleanSimConfigElement extends AbstractConfigElement {
    Boolean value;

    def BooleanSimConfigElement() { }
	
	def BooleanSimConfigElement(String name, boolean value) {
        setName(name);
        setValue(value);
	}

    String toString() { "Boolean: ${name}=${value}"}

}
