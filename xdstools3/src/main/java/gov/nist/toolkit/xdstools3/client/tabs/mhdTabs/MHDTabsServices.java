package gov.nist.toolkit.xdstools3.client.tabs.mhdTabs;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * RPC services for mhd validation
 */
@RemoteServiceRelativePath("mhd-tabs")
public interface MHDTabsServices extends RemoteService  {

    public String validateMHDMessage(String messageType);

    public String convertMHDToXDS();
}
