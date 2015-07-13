package gov.nist.hit.ds.xdstools3.client.tabs.connectathonTabs;

import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;

public class FolderValidationTab extends AbstractRegistryAndRepositoryTab {

    @Override
    protected String setHeaderTitle() {
        return "Folder Validation";
    }

    @Override
    protected void configureEndpoint() {
        // TODO To change when we'll know how to configure an EndpointWidget
    }

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getFolderValidationTabCode();
    }
}
