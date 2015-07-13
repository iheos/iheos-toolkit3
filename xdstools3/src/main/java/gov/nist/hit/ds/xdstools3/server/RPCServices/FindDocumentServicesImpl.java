package gov.nist.hit.ds.xdstools3.server.RPCServices;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.xdstools3.client.exceptions.ToolkitServerError;
import gov.nist.hit.ds.xdstools3.client.tabs.findDocumentsTab.FindDocumentTabServices;
import gov.nist.hit.ds.xdstools3.server.Caller;
import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class FindDocumentServicesImpl extends RemoteServiceServlet implements FindDocumentTabServices {
    static Logger logger = Logger.getLogger(FindDocumentServicesImpl.class);

    /**
     * Default constructor
     */
    public FindDocumentServicesImpl() {
    }

    public AssetNode findDocuments(String pid, boolean tls, boolean saml, boolean onDemand, String endpointID) throws ToolkitServerError {
        return Caller.getInstance().findDocuments(pid, tls, saml, onDemand, endpointID);
    }

}
