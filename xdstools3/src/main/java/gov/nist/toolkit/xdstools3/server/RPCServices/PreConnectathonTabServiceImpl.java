package gov.nist.toolkit.xdstools3.server.RPCServices;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;


import gov.nist.toolkit.xdstools3.client.tabs.preConnectathonTestsTab.PreConnectathonTabService;
import gov.nist.toolkit.xdstools3.server.Caller;
import org.apache.log4j.Logger;



import java.util.Map;


@SuppressWarnings("serial")
public class PreConnectathonTabServiceImpl extends RemoteServiceServlet implements PreConnectathonTabService {

    static Logger logger = Logger.getLogger(PreConnectathonTabServiceImpl.class);


    public PreConnectathonTabServiceImpl() {


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
