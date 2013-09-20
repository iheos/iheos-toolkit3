package gov.nist.toolkit.wsseTool.validation

import java.util.List
import javax.xml.namespace.QName
import org.opensaml.xml.validation.Validator
import org.opensaml.xml.validation.ValidatorSuite

import org.opensaml.saml2.core.SubjectConfirmation
import org.opensaml.saml2.core.SubjectConfirmationData
import org.opensaml.xml.XMLObject
import org.opensaml.xml.signature.KeyInfo
import org.opensaml.xml.signature.KeyValue
import org.opensaml.xml.validation.ValidationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Element

import gov.nist.toolkit.wsseTool.context.SecurityContextImpl
import gov.nist.toolkit.wsseTool.engine.annotations.Validation;
import gov.nist.toolkit.wsseTool.generation.opensaml.OpenSamlFacade
import gov.nist.toolkit.wsseTool.parsing.opensaml.OpenSamlSecurityHeader
import gov.nist.toolkit.wsseTool.signature.api.Verifier

class SignatureVerificationVal {


	private static final Logger log = LoggerFactory.getLogger(AuthzDecisionStatementVal.class)

	private SecurityContextImpl context
	private OpenSamlSecurityHeader header

	public SignatureVerificationVal(SecurityContextImpl context){
		this.context = context
		this.header = context.opensamlHeader
	}

	@Validation(id="1036-1056", rtm=["26","27","28","59","141","142","144","147","148","149","150","152","153","154","155","156","209","213"])
	public void verifySamlAssertionSignature(){
		Boolean isValid = new Verifier().verifySamlAssertionSignature(header)
		
		if(!isValid) log.error("cannot verify saml assertion signature.")
	}

	@Validation(id="1104-1024", rtm=["11","12","13","14","15","16","17","18","19","20","21","22","23","24","29","56","57","58","207","208","210","217"])
	public void verifyTimestampSignature(){
		Boolean isValid = new Verifier().verifyTimestampSignature(header)
		if(!isValid) log.error("cannot verify timestamp signature.")
	}

	@Validation(id="1069-1070")
	public void verifySubjectConfirmationDataKeyInfo(){
		SubjectConfirmationData subConfData = header.getSubject().getSubjectConfirmations().get(0).getSubjectConfirmationData()
		List<XMLObject> keyInfos = subConfData.getUnknownXMLObjects()

		boolean publicKeyPresent = false;

		for(XMLObject keyInfo : keyInfos){
			KeyInfo ki = (KeyInfo)keyInfo
			for (KeyValue kv : ki.getKeyValues()){
				if(kv.getDSAKeyValue() != null || kv.getRSAKeyValue() != null ){
					publicKeyPresent = true
				}
			}
			
		
		if(!ki.getRetrievalMethods().isEmpty() || !ki.getPGPDatas().isEmpty() || ! ki.getX509Datas().isEmpty() || ! ki.getSPKIDatas().isEmpty() )
			publicKeyPresent = true
		}
		
		if(! publicKeyPresent) {
			log.error("The public key MUST be represented in one of the following formats: ds:KeyValue/ds:DSAKeyValue, ds:KeyValue/ds:RSAKeyValue, ds:RetrievalMethod, ds:X509Data, ds:PGPDatads:SPKIData")
		}

		try{
			subConfData.validate(true)
		}
		catch(ValidationException e){
			log.error(e.getMessage())
		}
	}
	
}
