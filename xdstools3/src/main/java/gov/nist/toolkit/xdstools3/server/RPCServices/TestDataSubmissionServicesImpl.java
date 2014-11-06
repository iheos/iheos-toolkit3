package gov.nist.toolkit.xdstools3.server.RPCServices;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.toolkit.xdstools3.client.tabs.mhdTabs.MHDTabsServices;
import gov.nist.toolkit.xdstools3.client.tabs.testDataSubmissionTab.TestDataSubmissionServices;
import gov.nist.toolkit.xdstools3.server.Caller;
import org.apache.log4j.Logger;

import java.util.Map;


@SuppressWarnings("serial")
public class TestDataSubmissionServicesImpl extends RemoteServiceServlet implements TestDataSubmissionServices {

    static Logger logger = Logger.getLogger(PreConnectathonTabServiceImpl.class);


    public TestDataSubmissionServicesImpl() {
    }

    @Override
    public String submitTestData(String selectedTestDataType, String selectedDataTestSet, String pidValue, String repository,boolean tls,String saml) {
        return null;
    }

    @Override
    public Map<String,String> retrieveTestDataSet(String testDataType) {
        return Caller.getInstance().retrieveTestDataSet(testDataType);
    }


}
