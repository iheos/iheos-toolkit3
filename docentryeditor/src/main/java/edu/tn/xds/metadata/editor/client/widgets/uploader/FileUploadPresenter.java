package edu.tn.xds.metadata.editor.client.widgets.uploader;

import edu.tn.xds.metadata.editor.client.event.NewFileLoadedEvent;
import edu.tn.xds.metadata.editor.client.event.StartEditXdsDocumentEvent;
import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractPresenter;
import edu.tn.xds.metadata.editor.client.parse.XdsParser;
import edu.tn.xds.metadata.editor.shared.model.String256;
import edu.tn.xds.metadata.editor.shared.model.XdsDocumentEntry;

import javax.inject.Inject;

public class FileUploadPresenter extends AbstractPresenter<FileUploadView> {

    @Inject
    XdsParser xdsParser;

    public void fileUploaded(String results) {
        // remove xml file first line (xml doctype => <?xml...>)
        logger.info("... file loaded, parsing metadata...");


        XdsDocumentEntry model = xdsParser.parse(results.split(";\\^;\\^;")[1]);
        model.setFileName(new String256().setString(results.split(";\\^;\\^;")[0]));

        logger.info("... file parsed.");
        // logger.info("Metadata file: " + model.toXML());

        getEventBus().fireEvent(new NewFileLoadedEvent(model));
        //  This is the only way I found to make it work
        getEventBus().fireEvent(new StartEditXdsDocumentEvent(model));
    }

    @Override
    public void init() {
        // don't need to do anything in that particular case
    }
}
