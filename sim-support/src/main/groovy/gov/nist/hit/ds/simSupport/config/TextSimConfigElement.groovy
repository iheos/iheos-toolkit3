package gov.nist.hit.ds.simSupport.config

public class TextSimConfigElement extends AbstractConfigElement {
    String value;

	public TextSimConfigElement(String _name, String _value) { name = _name;  value = _value }

    String toString() { "Text: ${name}=${value}"}

}
