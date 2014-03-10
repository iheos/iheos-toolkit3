package gov.nist.toolkit.xdstest2;

import gov.nist.toolkit.actorfactory.SiteServiceManager;
import gov.nist.toolkit.installation.Installation;
import gov.nist.toolkit.sitemanagement.Sites;
import gov.nist.toolkit.soap.axis2.Soap;
import gov.nist.toolkit.testengine.TestConfig;
import gov.nist.toolkit.testengine.TransactionSettings;
import gov.nist.toolkit.testengine.Xdstest2;
import gov.nist.toolkit.testengine.logrepository.LogRepositoryFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by bmajur on 3/5/14.
 */
public class RunTest {


    public void runTest() throws Exception {
        String testName = "simpleSubmit";
        List<String> sections = new ArrayList<String>();
        String[] areas = { "test" };
        String siteName = "pub";
        String sessionId = "sessionid";

        assertTrue(new File("xdstest2/src/test/resources/toolkit/admin").exists());

        Xdstest2 xt = new Xdstest2(new File("xdstest2/src/test/resources/toolkit"), null);
        TransactionSettings ts = new TransactionSettings();
        TestConfig tc = new TestConfig();

        URL url = getClass().getClassLoader().getResource("xdstest/");
        if (url == null) {
            throw new RuntimeException("getResource(\"xdstest/\") failed");
        }
        tc.testmgmt_dir = new File(url.toURI());

        ts.logRepository =
                new LogRepositoryFactory().getRepository(
                        Installation.installation().sessionCache(),
                        sessionId,
                        LogRepositoryFactory.IO_format.JAVA_SERIALIZATION,
                        LogRepositoryFactory.Id_type.TIME_ID,
                        null);
        xt.setLogRepository(ts.logRepository);
        if (testName.startsWith("tc:")) {
            String collectionName = testName.split(":")[1];
            xt.setTestCollection(collectionName);
        } else {
            xt.setTest(testName, sections, areas);
        }
        xt.setSite(new Sites(SiteServiceManager.getSiteServiceManager().getAllSites(sessionId)).getSite(siteName));
        xt.setSecure(false);
        xt.setWssec(false);

        tc.soap = new Soap();
        xt.setTestConfig(tc);

//        xt.run(
//                null,
//                null,
//                true,
//                ts
//                );
    }
}
