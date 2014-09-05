package gov.nist.toolkit.xdstools3.server;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.toolkit.xdstools3.client.tabs.preConnectathonTestsTab.ToolkitService;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("serial")
public class ToolkitServiceImpl extends RemoteServiceServlet implements ToolkitService {

    static Logger logger = Logger.getLogger(ToolkitServiceImpl.class);

    public ToolkitServiceImpl() {

    }

    public Map<String, String> getCollectionNames(String collectionSetName) throws Exception {
        return Caller.getInstance().getCollectionNames(collectionSetName);
    }

    public Map<String, String> getCollection(String collectionSetName, String collectionName) throws Exception {
        return Caller.getInstance().getCollection(collectionSetName, collectionName);
    }

    public String getTestReadme(String test){
        return Caller.getInstance().getTestReadme(test);
    }

}
