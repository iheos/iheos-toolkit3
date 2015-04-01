package gov.nist.hit.ds.xdstools3.client.tabs.findDocumentsTab;

import com.google.gwt.user.client.rpc.AsyncCallback;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.xdstools3.client.exceptions.ToolkitServerError;

/**
 * Asynchronous interface to call the RPC Services for mhd validation.
 */
public interface FindDocumentTabServicesAsync {

    void findDocuments(String pid, boolean tls, boolean saml, boolean onDemand, String endpointID, AsyncCallback<AssetNode> async) throws ToolkitServerError;

}
