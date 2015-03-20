package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import gov.nist.hit.ds.xdstools3.shared.Site;

/**
 * Client side of the GWT RPC mechanism. This is the service class.
 *
 * @author dazais
 * @see EndpointServiceAsync
 *
 */
@RemoteServiceRelativePath("sites")
public interface EndpointService extends RemoteService {

   // public Sites retrieveSites();
    public Site retrieveSiteAttributes();

}
