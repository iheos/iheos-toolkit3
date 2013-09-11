package gov.nist.toolkit.wsseTool.context;

import gov.nist.toolkit.wsseTool.api.config.KeystoreAccess;
import gov.nist.toolkit.wsseTool.api.config.SecurityContext;
import gov.nist.toolkit.wsseTool.parsing.groovyXML.GroovyHeader;
import gov.nist.toolkit.wsseTool.parsing.opensaml.OpenSamlSecurityHeader;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

public class SecurityContextImpl implements SecurityContext {

	private Map<String,Object> params = new HashMap<String,Object>();
	
	private KeystoreAccess keystore;

	private GroovyHeader groovyHeader;

	private Element domHeader;
	
	private OpenSamlSecurityHeader opensamlHeader;

	@Override
	public Map<String,Object> getParams() {
		return params;
	}

	@Override
	public KeystoreAccess getKeystore() {
		return keystore;
	}

	@Override
	public void setKeystore(KeystoreAccess keystore) {
		this.keystore = keystore;
	}

	@Override
	public void setParam(String key, Object value) {
		params.put(key, value);
	}
	
	public GroovyHeader getGroovyHeader() {
		return groovyHeader;
	}

	public Element getDomHeader() {
		return domHeader;
	}

	public void setGroovyHeader(GroovyHeader header){
		this.groovyHeader = header;
	}
	
	public void setDomHeader(Element header){
		this.domHeader = header;
	}

	public OpenSamlSecurityHeader getOpensamlHeader() {
		return opensamlHeader;
	}

	public void setOpensamlHeader(OpenSamlSecurityHeader opensamlHeader) {
		this.opensamlHeader = opensamlHeader;
	}
}
