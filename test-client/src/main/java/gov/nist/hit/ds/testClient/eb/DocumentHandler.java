package gov.nist.hit.ds.testClient.eb;

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.io.File;

/**
 * Created by bmajur on 1/13/15.
 */
// File
//     or
// content and mimeType
//     or
// dataHandler
// must be filled in
public class DocumentHandler {
    public File file = null;

    public String content = null;
    public String mimeType = null;

    public  DataHandler dataHandler = null;

    public DocumentHandler(File _file) { file = _file; }
    public DocumentHandler(DataHandler _dataHandler) { dataHandler = _dataHandler; }
    public DocumentHandler(String _content, String _mimeType) { content = _content; mimeType = _mimeType; }

    public DataHandler getDataHandler() {
        if (dataHandler != null) return dataHandler;
        if (file != null) return new DataHandler(new FileDataSource(file));
        if (content != null && mimeType != null) {
            return new DataHandler(content, mimeType);
        }
        throw new ToolkitRuntimeException("DocumentHandler not initialized.");
    }
}
