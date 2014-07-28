package gov.nist.hit.ds.dsSims.metadataValidator.object.test.direct

import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.dsSims.metadataValidator.object.DocumentEntry
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.TextErrorRecorder
import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.metadata.MetadataParser
import gov.nist.toolkit.utilities.xml.Util
import org.apache.axiom.om.OMElement
import org.junit.Test

import static org.junit.Assert.assertFalse

class DocumentEntryTest {
	def goodde = '''
   <foo>
        <ExtrinsicObject id="Document01" mimeType="text/xml"
            objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1">
            <ExternalIdentifier id="ei01" registryObject="Document01" identificationScheme="urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab"
                value="1.43.456.45889.23451.1.2.234.2">
                <Name>
                    <LocalizedString value="XDSDocumentEntry.uniqueId"/>
                </Name>
            </ExternalIdentifier>
        </ExtrinsicObject>
	</foo>
	'''

	@Test
	public void testValid() {
		OMElement xml = Util.parse_xml(goodde)
		Metadata m = MetadataParser.parseNonSubmission(xml)
		OMElement o = m.getExtrinsicObject(0)
		assertFalse (o == null)
		DocumentEntry de = new DocumentEntry(m, o)
		ValidationContext vc = new ValidationContext()
		vc.isXDRMinimal = true
		ErrorRecorder er = new TextErrorRecorder();
		er.sectionHeading("testValid()")
		de.validate(er, vc, new HashSet<String>());
		System.out.println(er.toString());
		assertFalse er.hasErrors()
	}

}
