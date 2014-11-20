package gov.nist.toolkit.xdstools3.server.RPCServices;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.toolkit.xdstools3.client.tabs.adminSettingsTab.AdminSettingsService;
import gov.nist.toolkit.xdstools3.server.Caller;

/**
 * Created by dazais on 10/29/2014.
 */
public class AdminSettingsImpl extends RemoteServiceServlet implements AdminSettingsService {

    private static final long serialVersionUID = 1L;

    @Override
    public void saveAdminSettings(String[] settings) {Caller.getInstance().saveAdminSettings(settings);}
    public void saveAdminPassword(String oldPassword, String newPassword) {Caller.getInstance().saveAdminPassword(oldPassword, newPassword);}
    public String[] retrieveAdminSettings() {return Caller.getInstance().retrieveAdminSettings();}

}
