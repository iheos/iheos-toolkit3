package gov.nist.hit.ds.xdstools3.client.tabs;


import gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure.EndpointConfigWidget;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;

public class EndpointConfigTab extends GenericCloseableTab {
	static String header = "View / Configure Endpoints";

	public EndpointConfigTab() {
		super(header);
		setPane(new EndpointConfigWidget());
	}

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getEndpointsTabCode();
    }


}
