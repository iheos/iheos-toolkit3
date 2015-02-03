package gov.nist.hit.ds.docentryeditor.client.widgets.uploader;

import gov.nist.hit.ds.docentryeditor.client.event.NewFileLoadedEvent;
import gov.nist.hit.ds.docentryeditor.client.event.StartEditXdsDocumentEvent;
import gov.nist.hit.ds.docentryeditor.client.generics.abstracts.AbstractPresenter;
import gov.nist.hit.ds.docentryeditor.client.parse.XdsParser;
import gov.nist.hit.ds.docentryeditor.shared.model.String256;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsDocumentEntry;

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
