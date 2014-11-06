package gov.nist.toolkit.xdstools3.client.tabs.testDataSubmissionTab;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Map;

public interface TestDataSubmissionServicesAsync {
    void submitTestData(String selectedTestDataType, String selectedDataTestSet, String pidValue, String repository, boolean tls, String saml, AsyncCallback<String> async);

    void retrieveTestDataSet(String testDataType, AsyncCallback<Map<String,String>> async);
}
