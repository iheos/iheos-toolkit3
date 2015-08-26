package gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import gov.nist.hit.ds.xdstools3.client.exceptions.NoServletSessionException;
import gov.nist.toolkit.results.client.Result;

import java.util.Map;

/**
 * Created by Diane Azais local on 8/2/2015.
 */

    @RemoteServiceRelativePath("testStatus")
    public interface TestStatusTabService extends RemoteService {
    Map<String, Result> retrieveAllTests() throws NoServletSessionException;
    }


