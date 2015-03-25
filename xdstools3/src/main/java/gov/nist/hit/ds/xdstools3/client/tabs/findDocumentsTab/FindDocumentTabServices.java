package gov.nist.hit.ds.xdstools3.client.tabs.findDocumentsTab;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.xdstools3.client.exceptions.ToolkitServerError;

/**
 * RPC services for Find Document validation
 */
@RemoteServiceRelativePath("finddocument-tab")
public interface FindDocumentTabServices extends RemoteService  {

    public AssetNode findDocuments(String pid, boolean tls, boolean saml, boolean onDemand, String endpointID) throws ToolkitServerError;

}
