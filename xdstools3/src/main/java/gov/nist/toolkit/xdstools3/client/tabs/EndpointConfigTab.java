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
        // Check if logged in or not and display View or Edit version of the widget accordingly
//        if (!Util.getInstance().getLoggedAsAdminStatus()) {
//            // ask user to log in
//            LoginDialogWidget dialog = new LoginDialogWidget("ENDPOINTS");
//            dialog.show();
//        }
        EndpointsConfigWidget viewEndpoints = new EndpointsConfigWidget();
        setContents(viewEndpoints);
	}


}
