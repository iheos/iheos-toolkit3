package gov.nist.hit.ds.xdstools3.client.tabs.mhdTabs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.xdstools3.client.exceptions.ToolkitServerError;

/**
 * Asynchronous interface to call the RPC Services for mhd validation.
 */
public interface MHDTabsServicesAsync {
    void validateMHDMessage(String messageType, AsyncCallback<AssetNode> async) throws ToolkitServerError;

    void convertMHDToXDS(AsyncCallback<AssetNode> convertedDocument);
}