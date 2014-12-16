package gov.nist.toolkit.xdstools3.client.tabs.submitTestDataTab;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Map;

/**
 * Asynchronous interface for test data submission services to call upon distant method using RPC
 */
public interface TestDataSubmissionServicesAsync {
    void submitTestData(String selectedTestDataType, String selectedDataTestSet, String pidValue, String repository, boolean tls, String saml, AsyncCallback<String> async);

    void retrieveTestDataSet(String testDataType, AsyncCallback<Map<String,String>> async);
}
