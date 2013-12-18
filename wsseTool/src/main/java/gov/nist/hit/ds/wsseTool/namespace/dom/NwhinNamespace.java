package gov.nist.hit.ds.wsseTool.namespace.dom;

/**
 * Declare default namespaces prefixes and uris.
 * Programmer should default on these namespaces if they are not declare in the message context.
 * 
 * 
 * @author gerardin
 * 
 */

public enum NwhinNamespace { 
		XSI("xsi", "http://www.w3.org/2001/XMLSchema-instance"),
		XS("xs", "http://www.w3.org/2001/XMLSchema"), 
		SOAP_ENV("S", "http://www.w3.org/2003/05/soap-envelope"),
		WSSE("wsse","http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"),
		WSSE11("wsse11","http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd"),
		WSA("wsa","http://www.w3.org/2005/08/addressing"),
		WSU("wsu","http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"),
		DS("ds","http://www.w3.org/2000/09/xmldsig#"),
		SAML("saml", "urn:oasis:names:tc:SAML:1.0:assertion"),
		SAML2("saml2","urn:oasis:names:tc:SAML:2.0:assertion"),
		EXC14N("exc14n", "http://www.w3.org/2001/10/xml-exc-c14n#");
		
		
		private final String prefix;
		private final String uri;

		NwhinNamespace(String prefix , String uri) {
			this.prefix = prefix;
			this.uri = uri;
		}
		
		public String prefix(){
			return prefix;
		}
		
		public String uri(){
			return uri;
		}
		
		
}
