package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import gov.nist.hit.ds.xdstools3.shared.Site;
import gov.nist.hit.ds.xdstools3.client.customWidgets.dialogs.PopupMessageV3;

/**Singleton that contains the data to populate each nested form of the Site Config Widget.
 * Created by dazais on 3/19/2015.
 */
public class SiteWidgetData {

    private static SiteWidgetData instance = null;
    private EndpointServiceAsync rpc = GWT.create(EndpointService.class);
   // static Logger logger = Logger.getLogger(SiteWidgetData.class);
    protected Site siteAttributesMap; // data for the current site


    public static SiteWidgetData getInstance(){
        if (instance == null){return new SiteWidgetData();}
        else return instance;
    }

    private SiteWidgetData(){
    }


    /** Retrieves over RPC and returns the nested data for one form of the Site widget.
     * @return
     */
    public Site getData(){
        AsyncCallback<Site> sitesCallback = new AsyncCallback<Site>() {

            @Override
            public void onFailure(Throwable caught) {
                //logger.error(caught.getMessage());
                new PopupMessageV3(caught.getMessage());
            }

            @Override
            public void onSuccess(Site result) {
                siteAttributesMap = result;
            }
        };

        rpc.retrieveSiteAttributes(sitesCallback);
        return siteAttributesMap;
    }



}
