package gov.nist.toolkit.xdstools3.client.tabs.connectathonTabs;

import gov.nist.toolkit.xdstools3.client.manager.TabNamesManager;

public class LifecycleValidationTab extends AbstractRegistryAndRepositoryTab {
    @Override
    protected String setHeaderTitle() {
        return "Lifecycle Validation";
    }

    @Override
    protected void configureEndpoint() {
        // TODO To change when we'll know how to configure an EndpointWidget
    }

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getLifecycleValidationTabCode();
    }
}
