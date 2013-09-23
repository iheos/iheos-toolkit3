package gov.nist.hit.ds.wsseTool.parsing;

import gov.nist.hit.ds.wsseTool.api.config.Context;
import gov.nist.hit.ds.wsseTool.api.config.GenContext;
import gov.nist.hit.ds.wsseTool.api.config.KeystoreAccess;
import gov.nist.hit.ds.wsseTool.parsing.groovyXML.GroovyHeader;
import gov.nist.hit.ds.wsseTool.parsing.opensaml.OpenSamlSecurityHeader;
import gov.nist.hit.ds.wsseTool.validation.engine.TestData;

import java.util.Map;

import org.w3c.dom.Element;

public class Message implements Context, TestData {

	protected GroovyHeader groovyHeader;

	protected Element domHeader;
	
	protected OpenSamlSecurityHeader opensamlHeader;

	protected Context context;

	public Message(Element domHeader, Context context){
		this.domHeader = domHeader;
		this.context = (context != null) ? context : new GenContext();
	}
	
	
	@Override
	public Map<String,Object> getParams() {
		return context.getParams();
	}

	@Override
	public KeystoreAccess getKeystore() {
		return context.getKeystore();
	}

	@Override
	public void setKeystore(KeystoreAccess keystore) {
		context.setKeystore(keystore);
	}

	@Override
	public void setParam(String key, Object value) {
		context.setParam(key, value);
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
