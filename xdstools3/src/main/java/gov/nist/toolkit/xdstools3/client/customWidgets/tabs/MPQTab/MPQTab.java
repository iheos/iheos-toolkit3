package gov.nist.toolkit.xdstools3.client.customWidgets.tabs.MPQTab;

import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.CloseableTabWidget;

/**
 * Created by dazais on 5/14/2014.
 */
public class MPQTab extends CloseableTabWidget {
    static String header = "Multi-Patient Find Documents (MPQ)";

    public MPQTab() {
        super(header);

        // Set tab header
        setHeader(header);

        createContents();
    }

    public void createContents(){
       // EndpointWidget endpoints = new EndpointWidget();
       // MPQForm form = new  MPQForm();
        MPQCanvas canvas = new MPQCanvas();

        // calls a custom function that sets the contents and keeps the titles
        setContents(canvas);
    }

}
