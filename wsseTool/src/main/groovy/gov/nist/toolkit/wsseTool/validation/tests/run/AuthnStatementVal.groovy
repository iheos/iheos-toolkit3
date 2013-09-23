package gov.nist.toolkit.wsseTool.validation.tests.run


import gov.nist.toolkit.wsseTool.api.*
import gov.nist.toolkit.wsseTool.api.config.ValConfig
import gov.nist.toolkit.wsseTool.parsing.groovyXML.GroovyHeader
import gov.nist.toolkit.wsseTool.time.TimeUtil
import gov.nist.toolkit.wsseTool.validation.data.*
import gov.nist.toolkit.wsseTool.validation.engine.annotations.Validation
import gov.nist.toolkit.wsseTool.validation.tests.BaseVal;
import groovy.util.slurpersupport.GPathResult

import org.apache.commons.validator.routines.DomainValidator
import org.apache.commons.validator.routines.InetAddressValidator
import org.joda.time.DateTime
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestName
import org.slf4j.Logger

class AuthnStatementVal extends BaseVal {

	@Rule
	public TestName name = new TestName();

	/*
	 * Test initialization
	 */
	@Before
	public final void start() {
		System.out.println("------------ Test start : " + name.getMethodName()
				+ " -------------------");
	}

	@After
	public final void end() {
		System.out.println("------------ Test end : " + name.getMethodName()
				+ " -------------------");
	}

	@Validation(id="1075", rtm=["63"])
	public void authnStatementParsing(){
		GPathResult children = header.map.authnStatement.children()

		if(children.size() == 2){
			//order enforce only if the optional subjectLocality is present
			if(children[0].name() != "SubjectLocality") log.error("authn statement first child must be a subject locality but was ", children[0])
			if(children[1].name() != "AuthnContext") log.error("authn statement second child must be an authn context but was ", children[1])

			assert children[0].name() == "SubjectLocality"
			assert children[1].name() == "AuthnContext"
		}

		else {
			//otherwise we check only the authnContext
			if(children[0].name() != "AuthnContext") log.error("authn statement first child must be an authn context but was ", children[0])
			assert children[0].name() == "AuthnContext"
		}
	}

	@Validation(id="1076", rtm=["82"])
	public void authnInstant(){
		String authnInstant = header.map.authnStatement.@AuthnInstant.text()
		if( !TimeUtil.isDateInUTCFormat(authnInstant) )log.error("authnInstant not in UTC format : {}", authnInstant)


		DateTime ai = TimeUtil.parseDateString(authnInstant)
		String creationDate = header.map.timestamp.Created[0].text().trim()
		DateTime creationTime = TimeUtil.parseDateString(creationDate)

		if(ai.isAfter(creationTime)){
			log.error("saml2:AuthnStatement@authnInstant {} should be earlier than wsu:Timestamp/wsu:Created {}", ai ,creationTime)
		}

		assert(!ai.isAfter(creationTime))
	}

	@Validation(id="1077", rtm=["79"], category="optional", status=ValConfig.Status.not_implemented)
	public void SessionIndex(){
		log.info("session index is optional")
	}

	@Validation(id="1078", rtm=["78"], category="optional")
	public void SubjectLocality(){


		GPathResult loc = header.map.authnStatement.children.findAll{ it.@Name == "SubjectLocality"}

		if(loc.isEmpty()){
			log.info("subject locality not present but is optional."); return
		}

		if(loc.@DNSName == null) log.error("DNSName should be present")
		assert loc.@DNSName != null

		if(loc.@Address == null) log.error("Address should be present")
		assert loc.@Address != null

		DomainValidator dnsVal = DomainValidator.getInstance()
		boolean validDNSName = dnsVal.isValid(loc.@DNSName.text())

		if(!validDNSName) log.error("invalid DNSName. Found : {} ",loc.@DNSName.text() )
		assert validDNSName

		InetAddressValidator ipVal = InetAddressValidator.getInstance()
		boolean validIP = dnsVal.isValid(loc.@Address.text())

		if(! validIP) log.error("invalid InetAddress. Found : {} ", loc.@Address.text() )
		assert validIP
	}

	@Validation(id="1079", rtm=["80"])
	public void authnContextRequired(){
		def authnContext = header.map.authnStatement.children().findAll{it.name() == 'AuthnContext'}
		if( authnContext.size() != 1 ) log.error("AuthnContext is required")

		assert(authnContext.size() == 1 )
	}

	@Validation(id="1080", rtm=["77","85","169"])
	public void authnContextClassRef() {
		String authnContextClassRef = header.map.authnStatement.AuthnContext[0].AuthnContextClassRef[0].text().trim()

		if(!(authnContextClassRef in AuthnContextClassRef.values)) log.error("authnContextClassRef not allowed found : {}, but expected one of : {}", authnContextClassRef, AuthnContextClassRef.values)
		assert (authnContextClassRef in AuthnContextClassRef.values)
	}
}
