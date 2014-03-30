package gov.nist.toolkit.validatorsccda;

import gov.nist.hit.ds.errorRecording.TextErrorRecorder;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import gov.nist.toolkit.utilities.io.Io;
import gov.nist.toolkit.valccda.CcdaValidator;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

import static org.junit.Assert.fail;

public class CdaContentTest {

    File badCcdaFile = new File("validators-ccda/src/gov/nist/toolkit/valccda/test/resources/CCDA_CCD_Ambulatory_blank_lines.xml");


    @Test
    public void badCcdaTest() {
        try {
            InputStream is = Io.getInputStreamFromFile(badCcdaFile);
            String validationType = "Transitions Of Care Ambulatory Summary";
            AssertionGroup ag = new AssertionGroup();
            TextErrorRecorder er = new TextErrorRecorder();
            CcdaValidator.validateCDA(
                    is,
                    validationType,
                    ag);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getName());
            System.out.println(ExceptionUtil.exception_details(e));
            fail();
        }
    }
}
