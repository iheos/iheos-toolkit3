package gov.nist.hit.ds.xdstools3.server.RPCServices;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.hit.ds.xdstools3.client.exceptions.NoServletSessionException;
import gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab.Test;
import gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab.TestCollection;
import gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab.TestStatusTabService;
import gov.nist.hit.ds.xdstools3.server.Caller;
import gov.nist.toolkit.results.client.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Diane Azais local on 8/2/2015.
 */
public class TestStatusTabImpl extends RemoteServiceServlet implements TestStatusTabService {

    private static final long serialVersionUID = 1L;
/*
    @Override
    public Map<String, Result> retrieveAllTests() throws NoServletSessionException {
        return new HashMap<>();
       // return Caller.getInstance().getTestResults(new ArrayList<String>(),"");
    }*/

}
