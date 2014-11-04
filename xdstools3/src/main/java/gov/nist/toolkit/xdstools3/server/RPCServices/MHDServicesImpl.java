package gov.nist.toolkit.xdstools3.server.RPCServices;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.toolkit.xdstools3.client.tabs.mhdTabs.MHDTabsServices;
import gov.nist.toolkit.xdstools3.server.Caller;
import org.apache.log4j.Logger;


@SuppressWarnings("serial")
public class MHDServicesImpl extends RemoteServiceServlet implements MHDTabsServices {

    static Logger logger = Logger.getLogger(PreConnectathonTabServiceImpl.class);


    public MHDServicesImpl() {
    }

    public String validateMHDMessage(String messageType){
        return Caller.getInstance().validateMHDMessage(messageType);
    }

}
