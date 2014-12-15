package gov.nist.toolkit.xdstools3.client.tabs;


import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.smartgwt.configure.EndpointsConfigWidget;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;

public class EndpointConfigTab extends GenericCloseableTab {
	static String header = "View / Configure Endpoints";

	public EndpointConfigTab() {
		super(header);
	}

	protected EndpointsConfigWidget createContents(){
        return new EndpointsConfigWidget();
	}

    @Override
    protected String setTabName() {
        return TabNamesUtil.getInstance().getEndpointsTabCode();
    }


}
