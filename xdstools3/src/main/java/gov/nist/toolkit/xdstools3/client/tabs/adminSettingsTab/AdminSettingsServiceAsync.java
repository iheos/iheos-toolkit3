package gov.nist.toolkit.xdstools3.client.tabs.adminSettingsTab;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by dazais on 10/29/2014.
 */
public interface AdminSettingsServiceAsync {

     void saveAdminSettings(String[] settings, AsyncCallback<Void> async);
     void saveAdminPassword(String oldPassword, String newPassword, AsyncCallback<Void> async);
     void retrieveAdminSettings(AsyncCallback<String[]> async);
}
