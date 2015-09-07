package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import com.google.gwt.user.client.rpc.AsyncCallback;
import gov.nist.hit.ds.siteManagement.loader.Sites;

/**
 * Client side of the GWT RPC mechanism. This is the asynchronous interface.
 *
 * @author dazais
 * @see EndpointService
 *
 */

public interface EndpointServiceAsync {

   // void retrieveSites(AsyncCallback<Sites> async);
    void retrieveSiteAttributes(AsyncCallback<Sites> async);
}
