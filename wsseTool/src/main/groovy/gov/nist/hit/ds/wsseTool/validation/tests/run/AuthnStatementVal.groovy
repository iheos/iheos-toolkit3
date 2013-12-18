package gov.nist.hit.ds.wsseTool.validation.tests.run


import static org.junit.Assert.*
import static org.junit.Assume.*
import gov.nist.hit.ds.wsseTool.api.config.*
import gov.nist.hit.ds.wsseTool.time.TimeUtil
import gov.nist.hit.ds.wsseTool.validation.data.AuthnContextClassRef
import gov.nist.hit.ds.wsseTool.validation.engine.ValRunnerWithOrder
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.Optional
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.Validation
import gov.nist.hit.ds.wsseTool.validation.tests.BaseVal
import groovy.util.slurpersupport.GPathResult

import java.text.MessageFormat

import org.apache.commons.validator.routines.DomainValidator
import org.apache.commons.validator.routines.InetAddressValidator
import org.joda.time.DateTime
import org.junit.runner.RunWith


@RunWith(ValRunnerWithOrder.class)
public class AuthnStatementVal extends BaseVal {

	@Validation(id="1075", rtm=["63"])
	public void authnStatementParsing(){
		GPathResult children = header.map.authnStatement.children()

		if(children.size() == 2){
			//order enforce only if the optional subjectLocality is present
			assertEquals("authn statement first child must be a subject locality", "SubjectLocality", children[0].name())
			assertEquals("authn statement second child must be an authn context ", "AuthnContext" , children[1].name() )

			assertEquals("authn statement first child must be a subject locality", "SubjectLocality", children[0].name())
			assertEquals( "authn statement second child must be an authn context", "AuthnContext", children[1].name())
		}

		else {
			//otherwise we check only the authnContext
			assertEquals("authn statement first child must be an authn context", "AuthnContext" , children[0].name() )
		}
	}

	@Validation(id="1076", rtm=["82"])
	public void authnInstant(){
		String authnInstant = header.map.authnStatement.@AuthnInstant.text()
		assertTrue("authnInstant not in UTC format", TimeUtil.isDateInUTCFormat(authnInstant))

		DateTime ai = TimeUtil.parseDateString(authnInstant)
		String creationDate = header.map.timestamp.Created[0].text().trim()
		DateTime creationTime = TimeUtil.parseDateString(creationDate)

		assertTrue(MessageFormat.format("saml2:AuthnStatement@authnInstant {0} should be earlier than wsu:Timestamp/wsu:Created {1}", ai, creationTime), !ai.isAfter(creationTime))
	}

	@Optional
	@Validation(id="1077", rtm=["79"], status=ValConfig.Status.not_implemented)
	public void SessionIndex(){
		GPathResult loc = header.map.authnStatement.children.findAll{ it.@Name == "SessionIndex"}
		assumeTrue("session index not present but is optional.", !loc.isEmpty());
		
		//TODO check. No test is implemented : no constraints? @Antoine
	}

	@Optional
	@Validation(id="1078", rtm=["78"])
	public void SubjectLocality(){
		GPathResult loc = header.map.authnStatement.children.findAll{ it.@Name == "SubjectLocality"}

		assumeTrue("subject locality not present but is optional.", !loc.isEmpty());

		assertTrue("DNSName should be present", loc.@DNSName != null)

		assertTrue("Address should be present", loc.@Address != null)

		DomainValidator dnsVal = DomainValidator.getInstance()
		boolean validDNSName = dnsVal.isValid(loc.@DNSName.text())

		assertTrue( MessageFormat.format("invalid DNSName. Found : {0} ",loc.@DNSName.text()),  validDNSName)

		InetAddressValidator ipVal = InetAddressValidator.getInstance()
		boolean validIP = dnsVal.isValid(loc.@Address.text())


		assertTrue( MessageFormat.format("invalid InetAddress. Found : {0} ",loc.@Address.text()), validIP)
	}

	@Validation(id="1079", rtm=["80"])
	public void authnContextRequired(){
		def authnContext = header.map.authnStatement.children().findAll{it.name() == 'AuthnContext'}

		assertTrue("AuthnContext is required", authnContext.size() == 1 )
	}

	@Validation(id="1080", rtm=["77","85","169"])
	public void authnContextClassRef() {
		String authnContextClassRef = header.map.authnStatement.AuthnContext[0].AuthnContextClassRef[0].text().trim()

		assertTrue (MessageFormat.format("authnContextClassRef not allowed found : {0}, but expected one of : {1}", authnContextClassRef, AuthnContextClassRef.values.toString()),  authnContextClassRef in AuthnContextClassRef.values)
	}
}
