package gov.nist.hit.ds.utilities.xml;

public class XmlText {
	String xml;

	public String getXml() {
		return xml;
	}

	public XmlText setXml(String xml) {
		this.xml = xml;
		return this;
	}
	
	public XmlText setXml(byte[] xml) {
		this.xml = new String(xml);
		return this;
	}
	
}
