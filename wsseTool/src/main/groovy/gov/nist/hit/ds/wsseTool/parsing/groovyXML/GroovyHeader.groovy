package gov.nist.hit.ds.wsseTool.parsing.groovyXML

import groovy.util.slurpersupport.GPathResult

import javax.xml.namespace.NamespaceContext

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
