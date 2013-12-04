package gov.nist.hit.ds.wsseTool.validation.tests.run

import gov.nist.hit.ds.wsseTool.parsing.opensaml.OpenSamlSecurityHeader
import gov.nist.hit.ds.wsseTool.signature.api.Verifier
import gov.nist.hit.ds.wsseTool.validation.engine.ValRunnerWithOrder;
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.Validation
import gov.nist.hit.ds.wsseTool.validation.tests.BaseVal

import org.junit.Before
import org.junit.runner.RunWith;
import org.opensaml.saml2.core.SubjectConfirmationData
import org.opensaml.xml.XMLObject
import org.opensaml.xml.signature.KeyInfo
import org.opensaml.xml.signature.KeyValue
import org.opensaml.xml.validation.ValidationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import static org.junit.Assert.*


@RunWith(ValRunnerWithOrder.class)
public class SignatureVerificationVal extends BaseVal {

	private OpenSamlSecurityHeader openSamlHeader;

	@Before
	public void getOpenSamlContext(){
		this.openSamlHeader = context.opensamlHeader
	}

	@Validation(id="1036-1056", rtm=["26","27","28","59","141","142","144","147","148","149","150","152","153","154","155","156","209","213"])
	public void verifySamlAssertionSignature(){
		
		Boolean isValid = new Verifier().verifySamlAssertionSignature(openSamlHeader)
		
		assertTrue("cannot verify saml assertion signature.", isValid) 
	}

	@Validation(id="1104-1024", rtm=["11","12","13","14","15","16","17","18","19","20","21","22","23","24","29","56","57","58","207","208","210","217"])
	public void verifyTimestampSignature(){
		Boolean isValid = new Verifier().verifyTimestampSignature(openSamlHeader)
		assertTrue("cannot verify timestamp signature.", isValid)
	}

	@Validation(id="1069-1070")
	public void verifySubjectConfirmationDataKeyInfo(){
		SubjectConfirmationData subConfData = openSamlHeader.getSubject().getSubjectConfirmations().get(0).getSubjectConfirmationData()
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
		
		assertTrue("The public key MUST be represented in one of the following formats: ds:KeyValue/ds:DSAKeyValue, ds:KeyValue/ds:RSAKeyValue, ds:RetrievalMethod, ds:X509Data, ds:PGPDatads:SPKIData",publicKeyPresent)

		try{
			subConfData.validate(true)
		}
		catch(ValidationException e){
			fail(e.getMessage())
		}
	}
	
}
