package gov.nist.hit.ds.wsseTool.validation.tests;

public class ValDescriptor {

	public static final String NOT_IMPLEMENTABLE = "not implementable : ";
	public static final String NOT_IMPLEMENTED = "not implemented : ";

	public static final String ISSUER_IS_SECURITY_OFFICER = "Verify: saml:Issuer MUST identify the individual responsible for issuing the Assertions carried in the message. This is normally the system security officer for the sending NHIO, but may also be a system.";

	public static final String LATER_THAN_CAPTURED_TIME = "Expires is later than <time when the message was captured, if available>, within VALUE_SYSTEM_CLOCK_ALLOWABLE_DIFFERENCE allowance for differences in system times (see Test Data tab for more information).";
	public static final String EARLIER_THAN_CAPTURED_TIME = "Created is earlier than <time when the message was captured, if available>, within VALUE_SYSTEM_CLOCK_ALLOWABLE_DIFFERENCE allowance for differences in system times (see Test Data tab for more information).";

	public static final String SUBJECT_NAMED_ID = "Verify: MUST identify the person making the request at the initiating HIO.  May be a system.";

	public static final String MA1059 = "@Format is one of the following: urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddressurn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";
}
