package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String, String> retrieveSiteAttributes();

}
