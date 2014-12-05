package gov.nist.toolkit.xdstools3.client.tabs.submitTestDataTab;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Map;

/**
 * RPC Services client interface to call upon test data submission distant methods
 */
@RemoteServiceRelativePath("test-data-submission")
public interface TestDataSubmissionServices extends  RemoteService{
    String submitTestData(String selectedTestDataType, String selectedDataTestSet, String pidValue, String repository,boolean tls,String saml);
    Map<String,String> retrieveTestDataSet(String testDataType);
}
