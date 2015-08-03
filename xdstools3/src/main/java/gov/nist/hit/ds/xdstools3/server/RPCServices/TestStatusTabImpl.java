package gov.nist.hit.ds.xdstools3.server.RPCServices;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.hit.ds.xdstools3.client.tabs.adminSettingsTab.AdminSettingsService;
import gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab.Test;
import gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab.TestCollection;
import gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab.TestStatusTabService;
import gov.nist.hit.ds.xdstools3.server.Caller;

/**
 * Created by Diane Azais local on 8/2/2015.
 */
public class TestStatusTabImpl extends RemoteServiceServlet implements TestStatusTabService {

    private static final long serialVersionUID = 1L;

    @Override
    public TestCollection<Test> retrieveAllTests() {
        return Caller.getInstance().retrieveAllTests();
    }

}
