package gov.nist.hit.ds.docentryeditor.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsMetadata;

/**
 * Created by onh2 on 2/19/2015.
 */
@RemoteServiceRelativePath("xdsparser")
public interface XdsParserServices extends RemoteService{
    public XdsMetadata parseXdsMetadata(String fileContent);
}
