package gov.nist.hit.ds.docentryeditor.client.parser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsMetadata;

public interface XdsParserServicesAsync {
    void parseXdsMetadata(String fileContent, AsyncCallback<XdsMetadata> async);
}
