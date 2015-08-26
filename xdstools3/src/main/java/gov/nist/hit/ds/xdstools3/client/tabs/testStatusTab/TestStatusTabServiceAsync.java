package gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import gov.nist.hit.ds.xdstools3.client.exceptions.NoServletSessionException;
import gov.nist.toolkit.results.client.Result;

import java.util.Map;

public interface TestStatusTabServiceAsync {

    void retrieveAllTests(AsyncCallback<Map<String, Result>> async) throws NoServletSessionException;


}