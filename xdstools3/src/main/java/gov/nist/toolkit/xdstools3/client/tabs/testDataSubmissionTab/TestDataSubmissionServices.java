package gov.nist.toolkit.xdstools3.client.tabs.testDataSubmissionTab;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Created by onh2 on 11/4/2014.
 */
@RemoteServiceRelativePath("test-data-submission")
public interface TestDataSubmissionServices extends  RemoteService{
    String submitTestData(String selectedTestDataType, String selectedDataTestSet, String pidValue, String repository,boolean tls,String saml);

}
