package gov.nist.toolkit.wsseTool.parsing.groovyXML

import groovy.util.slurpersupport.GPathResult

import java.util.Map;
import javax.xml.namespace.NamespaceContext
import gov.nist.toolkit.wsseTool.namespace.dom.NhwinNamespaceContextFactory

class GroovyHeader {

	public Map<String,GPathResult> map
	public NamespaceContext namespaces
	
	public GroovyHeader(){
		this.map = new HashMap();
		this.namespaces = NhwinNamespaceContextFactory.getDefaultContext();
	}
	
	public GroovyHeader(Map map, NamespaceContext namespaces){
		this.map = map
		this.namespaces = namespaces
	}
}
