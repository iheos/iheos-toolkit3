package gov.nist.hit.ds.docentryeditor.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsMetadata;

public interface XdsParserServicesAsync {
    void parseXdsMetadata(String fileContent, AsyncCallback<XdsMetadata> async);
}
