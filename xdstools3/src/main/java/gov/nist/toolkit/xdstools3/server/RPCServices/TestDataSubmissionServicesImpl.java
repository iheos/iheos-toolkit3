package gov.nist.toolkit.xdstools3.server.RPCServices;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.toolkit.xdstools3.client.tabs.mhdTabs.MHDTabsServices;
import gov.nist.toolkit.xdstools3.client.tabs.testDataSubmissionTab.TestDataSubmissionServices;
import gov.nist.toolkit.xdstools3.server.Caller;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * RPC Service implementation for test data submission
 */
@SuppressWarnings("serial")
public class TestDataSubmissionServicesImpl extends RemoteServiceServlet implements TestDataSubmissionServices {
    static Logger logger = Logger.getLogger(PreConnectathonTabServiceImpl.class);

    /**
     * Default constructor
     */
    public TestDataSubmissionServicesImpl() {
    }

    /**
     * Method to submit new test data
     *
     * @param selectedTestDataType
     * @param selectedDataTestSet
     * @param pidValue
     * @param repository
     * @param tls
     * @param saml
     * @return
     */
    @Override
    public String submitTestData(String selectedTestDataType, String selectedDataTestSet, String pidValue, String repository,boolean tls,String saml) {
        return null;
    }

    /**
     * Method to retrieve test data sets from test data type
     *
     * @param testDataType
     * @return
     */
    @Override
    public Map<String,String> retrieveTestDataSet(String testDataType) {
        return Caller.getInstance().retrieveTestDataSet(testDataType);
    }


}
