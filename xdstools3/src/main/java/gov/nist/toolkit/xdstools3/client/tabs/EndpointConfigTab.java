package gov.nist.toolkit.xdstools3.client.tabs;


import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.configure.EndpointsConfigWidget;

public class EndpointConfigTab extends GenericCloseableTab {
	static String header = "View / Configure Endpoints";

	public EndpointConfigTab() { 
		super(header);

		setHeader(header);
        createContents();
	}  
	
	private void createContents(){
        EndpointsConfigWidget viewEndpoints = new EndpointsConfigWidget();
        setContents(viewEndpoints);
	}


}
