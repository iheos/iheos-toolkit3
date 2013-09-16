package gov.nist.toolkit.wsseTool.namespace.dom;

import java.util.HashMap;
import java.util.Map;

import gov.nist.toolkit.wsseTool.util.NamespaceContextMap;

import javax.xml.namespace.NamespaceContext;


/**
 * This static factory provides an implementation of a
 * javax.xml.namespace.NamespaceContext.
 * 
 * When browsing XML (by evaluating XPath expressions or using the DOM API), it
 * is important to keep document-agnostic definition of namespaces.
 * 
 * This factory builds 2 types of context : a message context and a default context.
 * 
 * The message context is obtained when parsing an XML message or template.
 * 
 * The default context is used to provide a unique definition of a namespace
 * uri and prefix if it is not present in the message context.
 * 
 * @author gerardin
 * 
 */

public class NhwinNamespaceContextFactory {
	
	
	public static NamespaceContext getDefaultContext() {
		return defaultContext;
	}
	
	public static NamespaceContext getMessageContext(Map<String,String> prefixMappings){
		return new NamespaceContextMap(prefixMappings);
	}
	
	
	private final static NamespaceContext defaultContext = initDefaultContext();
	
	private static NamespaceContext initDefaultContext() {
		
		Map<String,String> prefixMappings = new HashMap<String,String>();
		
		for(NwhinNamespace n : NwhinNamespace.values()){
			prefixMappings.put(n.prefix(), n.uri());
		}
		
		return new NamespaceContextMap(prefixMappings);
	}

}
