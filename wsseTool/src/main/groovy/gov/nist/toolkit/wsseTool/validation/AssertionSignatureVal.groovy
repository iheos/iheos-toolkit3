package gov.nist.toolkit.wsseTool.validation

import gov.nist.toolkit.wsseTool.validation.Validation;
import groovy.util.slurpersupport.GPathResult
import java.lang.reflect.Method

import gov.nist.toolkit.wsseTool.validation.data.IssuerFormat
import gov.nist.toolkit.wsseTool.context.SecurityContextImpl;
import gov.nist.toolkit.wsseTool.generation.opensaml.OpenSamlFacade;
import gov.nist.toolkit.wsseTool.parsing.groovyXML.GroovyHeader;
import gov.nist.toolkit.wsseTool.parsing.opensaml.OpenSamlSecurityHeader;
import gov.nist.toolkit.wsseTool.signature.api.Verifier;
import gov.nist.toolkit.wsseTool.time.TimeUtil;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.joda.time.DateTime
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AssertionSignatureVal {

	private static final Logger log = LoggerFactory.getLogger(AssertionSignatureVal.class);

	private GroovyHeader header
	private SecurityContextImpl context

	public AssertionSignatureVal(SecurityContextImpl context){
		this.context = context
		this.header = context.groovyHeader
	}
	
	@Validation(id="1036", rtm=["59"])
	public void uniqueId(){
		if(! header.map.a_signature.@Id.isEmpty())
			assert CommonVal.uniqueId(header.map.a_signature.@Id)
	}
	
	@Validation(id="1038", rtm=["141"])
	public void signInfoUniqueId(){
		if(! header.map.as_signedInfo.@Id.isEmpty())
			assert CommonVal.uniqueId(header.map.a_signature.@Id)
	}
	
	@Validation(id="1043", rtm=["147"])
	public void referenceUriIsAssertionId(){
		String id = header.map.as_signedInfo.'*'.findAll { it.name() == "Reference" }.@URI.text()
		assert id.stripIndent(1) == header.map.assertion.@ID.text(); //we remove the starting # in the ref
	}
}
