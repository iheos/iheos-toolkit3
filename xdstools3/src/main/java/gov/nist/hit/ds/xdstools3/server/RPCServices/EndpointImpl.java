package gov.nist.hit.ds.xdstools3.server.RPCServices;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.hit.ds.xdstools3.shared.Site;
import gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure.EndpointService;
import gov.nist.hit.ds.xdstools3.server.Caller;

/**
 * Server side of the GWT RPC mechanism.
 *
 * @author dazais
 * @see gov.nist.hit.ds.xdstools3.client.customWidgets.toolbar.ToolbarService
 *
 */
public class EndpointImpl extends RemoteServiceServlet implements EndpointService {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


   /* @Override
    public Sites retrieveSites() {
        return Caller.getInstance().retrieveSites();
    }*/

    @Override
    public Site retrieveSiteAttributes() {
        return Caller.getInstance().retrieveSiteAttributes();
    }
}
