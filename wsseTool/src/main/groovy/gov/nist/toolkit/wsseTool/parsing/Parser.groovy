package gov.nist.toolkit.wsseTool.parsing

import gov.nist.toolkit.wsseTool.generation.opensaml.OpenSamlFacade
import gov.nist.toolkit.wsseTool.namespace.dom.NhwinNamespaceContextFactory
import gov.nist.toolkit.wsseTool.parsing.groovyXML.GroovyHeader;
import gov.nist.toolkit.wsseTool.parsing.opensaml.OpenSamlSecurityHeader
import gov.nist.toolkit.wsseTool.util.MyXmlUtils
import groovy.util.slurpersupport.GPathResult

import java.lang.reflect.Field

import javax.xml.namespace.NamespaceContext

import org.w3c.dom.Element

class Parser {

	public static OpenSamlSecurityHeader parseToOpenSaml(Element element){
		OpenSamlFacade saml = new OpenSamlFacade()
		return new OpenSamlSecurityHeader(saml, element)
	}
	
	public static GroovyHeader parseToGPath(Element element){
		GroovyHeader header = new GroovyHeader()
		header.map.wsse = new XmlSlurper().parseText(MyXmlUtils.DomToString(element))
		header.namespaces = extractNamespaces(header.map.wsse)
		return header
	}

	public static GroovyHeader parseToGPath(Reader reader){
		GroovyHeader header = new GroovyHeader()
		header.map.wsse = new XmlSlurper().parse(reader)
		header.namespaces = extractNamespaces(header.map.wsse)
		return header
	}
	
	public static GroovyHeader parseToGPath(String filename){
		return parseToGPath(new FileReader(filename))
	}
	
	private static NamespaceContext extractNamespaces(GPathResult header){
		Field[] fields2 = GPathResult.class.getDeclaredFields()
		Field field = GPathResult.class.getDeclaredField("namespaceTagHints")
		field.setAccessible(true)
		java.util.Map namespaces = (java.util.Map)field.get(header)
		return NhwinNamespaceContextFactory.getMessageContext(namespaces)
	}
}
