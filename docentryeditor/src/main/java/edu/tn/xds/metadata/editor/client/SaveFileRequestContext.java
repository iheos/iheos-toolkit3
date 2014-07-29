package edu.tn.xds.metadata.editor.client;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import edu.tn.xds.metadata.editor.server.GenericServiceLocator;
import edu.tn.xds.metadata.editor.server.SaveFileService;

@Service(value = SaveFileService.class, locator = GenericServiceLocator.class)
public interface SaveFileRequestContext extends RequestContext {
    public Request<String> saveAsXMLFile(String name);

    public Request<String> saveAsXMLFile(String filename, String filecontent);
}
