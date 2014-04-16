package gov.nist.toolkit.ebrimIntegrationTest;

import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.XdsInternalException;
import gov.nist.toolkit.errorrecording.ErrorRecorder;
import gov.nist.toolkit.errorrecording.TextErrorRecorder;
import gov.nist.toolkit.registrymetadata.Metadata;
import gov.nist.toolkit.registrymetadata.MetadataParser;
import gov.nist.toolkit.utilities.xml.Util;
import gov.nist.toolkit.valregmetadata.field.MetadataValidator;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import org.apache.axiom.om.OMElement;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.fail;

/**
 * Created by bmajur on 2/5/14.
 */
public class ConvertToV3IT {

    @Test
    public void v3ConvertTest() throws XdsInternalException, MetadataException {
        // Codes to validate against
        System.setProperty("XDSCodesFile", "ebrimIntegrationTest/src/test/resources/codes.xml");
        // get testdata
        File submissionFile = new File("ebrimIntegrationTest/src/test/resources/register_metadata.xml");
        OMElement submissionEle = Util.parse_xml(submissionFile);
        Metadata v2Metadata = MetadataParser.parseNonSubmission(submissionEle);

        // convert to v3
        OMElement v3Ele = v2Metadata.getV3SubmitObjectsRequest();
//        System.out.println(new OMFormatter(v3Ele).toString());
        Metadata v3Metadata = MetadataParser.parse(v3Ele);

        // validate correctness of conversion
        ValidationContext vc = new ValidationContext();
        vc.isR = true;
        vc.isRequest = true;
        MetadataValidator validator = new MetadataValidator(v3Metadata, vc, null);
        ErrorRecorder er = new TextErrorRecorder();
        validator.run(er);
//        System.out.println(er.toString());
        if (er.hasErrors())
            fail();
    }
}
