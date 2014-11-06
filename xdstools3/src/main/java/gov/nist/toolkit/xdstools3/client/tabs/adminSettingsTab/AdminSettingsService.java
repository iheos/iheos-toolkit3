package gov.nist.toolkit.xdstools3.client.tabs.adminSettingsTab;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Created by dazais on 10/29/2014.
 */
    @RemoteServiceRelativePath("adminSettings")
    public interface AdminSettingsService extends RemoteService {

        public void saveAdminSettings(String[] settings);
        public void saveAdminPassword(String oldPassword, String newPassword);
        public String[] retrieveAdminSettings();
    }

