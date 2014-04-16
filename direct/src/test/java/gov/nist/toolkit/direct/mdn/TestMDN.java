/**
 This software was developed at the National Institute of Standards and Technology by employees
of the Federal Government in the course of their official duties. Pursuant to title 17 Section 105 of the
United States Code this software is not subject to copyright protection and is in the public domain.
This is an experimental system. NIST assumes no responsibility whatsoever for its use by other parties,
and makes no guarantees, expressed or implied, about its quality, reliability, or any other characteristic.
We would appreciate acknowledgement if the software is used. This software can be redistributed and/or
modified freely provided that any derivative works bear some notice that they are derived from it, and any
modified versions bear some notice that they have been modified.

Project: NWHIN-DIRECT
Authors: William Majurski
		 Frederic de Vaulx
		 Diane Azais
		 Julien Perugini
		 Antoine Gerardin

 */


package gov.nist.toolkit.direct.mdn;

import gov.nist.direct.messageProcessor.mdn.mdnImpl.MDNMessageProcessor;
import gov.nist.direct.utils.Utils;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.TextErrorRecorder;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import gov.nist.toolkit.valsupport.errrec.GwtErrorRecorder;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestMDN {

    IAssertionGroup er = new TextErrorRecorder();


	/**
	 * Checks that an MDN acknowledgment can be successfully generated and is detected as being an MDN.
	 */
	@Test
	public void testMDNGeneration(){

		String signingCert = "direct/src/gov/nist/direct/test/resources/certificates/mhunter.p12";
		byte[] signCert = null;
		String encryptionCert = "direct/src/gov/nist/direct/test/resources/certificates/mhunter.cer";
		byte[] encCert = null;
		String password = "mhunter";

		signCert = Utils.getByteFile(signingCert);
		encCert = Utils.getByteFile(encryptionCert);

		String unwrappedMDNMessage = "direct/src/gov/nist/direct/test/resources/mdnMessages/RIexamples/MDNMessage.txt";
		String mdn = Utils.readFile(unwrappedMDNMessage);
		byte[] mdnMessage = mdn.getBytes();

		//Properties props = System.getProperties();
		//Session session = Session.getDefaultInstance(props, null);
		//Utils.printToFile(mdn, "MDNFile.txt"); -- ok

		MDNMessageProcessor processor = new MDNMessageProcessor();
        IAssertionGroup er = new GwtErrorRecorder();

		//byte[] mdnMessage  = mdn.toString().getBytes();
		//byte[] mdnMessage = Utils.getByteFile("MDNFile.txt");
		processor.processMDNMessage(er, mdnMessage, signCert, password, new ValidationContext());

		System.out.println(er);

		assertTrue(!er.hasErrors());
	}





	/**
	 * Checks that an MDN message know to be correct is successfully validated. No encryption!
	 */
	//	@Test
	//	public void testMDNValidation(){
	//			UnwrappedMessageValidationTest testClass = new UnwrappedMessageValidationTest();
	//			 MimeMessage mdn = testClass.createTestMDN();
	//			System.out.println("TEST: created MDN message.");
	//			
	//			
	//			
	//			MDNMessageProcessor processor = new MDNMessageProcessor();
	//			ErrorRecorder er = new GwtErrorRecorder();
	//			processor.processMDNMessage(er, mdn, mdn, "", new ValidationContext());
	//	//
	////			System.out.println(er);
	//	//
	////			assertTrue(!er.hasErrors());
	//		
	//	}
	//	

	/**
	 * Checks that an MDN acknowledgment can be successfully generated and validated.
	 */
	@Test
	public void testMDNGenerationValidationCycle(){

	}





}