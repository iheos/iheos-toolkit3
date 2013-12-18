package gov.nist.hit.ds.wsseTool.validation.tests

import gov.nist.hit.ds.wsseTool.parsing.groovyXML.GroovyHeader

import java.util.regex.Pattern

import javax.naming.InvalidNameException
import javax.naming.ldap.LdapName

import org.apache.commons.validator.routines.EmailValidator
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class CommonVal {

	private static final Logger log = LoggerFactory.getLogger(CommonVal.class)

	/*
	 * TODO review how we handle namespace and what unicity of ID mean? Should ID, Id , wsu:Id be unique?
	 */
	public static boolean uniqueId(GroovyHeader header, String id) {
		def nodes = header.map.wsse.'**'.grep { it.@Id.text()} //get all the nodes with attribute "Id"
		boolean isUnique = nodes.findAll{ it.@Id == id}.size() <= 1 //only one has Id="id"
		return isUnique
	}

	public static boolean validEmail(String email){
		// Get an EmailValidator
		EmailValidator validator = EmailValidator.getInstance()
		// Validate an email address
		return validator.isValid(email)
	}
	
	public static boolean validDistinguishedName(String name) {
		try{
			LdapName n = new LdapName(name)
		}
		catch (InvalidNameException e){
			return false
		}
		return true
	}

	public static boolean validURN(String urn) {
		Pattern URN_PATTERN = Pattern.compile('^urn:[a-z0-9][a-z0-9-]{0,31}:([a-z0-9()+,\\-.:=@;$_!*\']|%[0-9a-f]{2})+$',Pattern.CASE_INSENSITIVE)
		return URN_PATTERN.matcher(urn).matches()
	}
	
	public static boolean validURNoid(String urn) {
		Pattern URN_PATTERN = Pattern.compile('^urn:oid:([a-z0-9()+,\\-.:=@;$_!*\']|%[0-9a-f]{2})+$',Pattern.CASE_INSENSITIVE)
		return URN_PATTERN.matcher(urn).matches()
	}


}
