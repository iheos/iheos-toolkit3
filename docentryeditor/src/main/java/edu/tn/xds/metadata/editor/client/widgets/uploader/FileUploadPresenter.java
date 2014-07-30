package edu.tn.xds.metadata.editor.client.widgets.uploader;

import com.google.gwt.place.shared.PlaceController;
import edu.tn.xds.metadata.editor.client.editor.EditorPlace;
import edu.tn.xds.metadata.editor.client.event.NewFileLoadedEvent;
import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractPresenter;
import edu.tn.xds.metadata.editor.client.parse.XdsParser;
import edu.tn.xds.metadata.editor.shared.model.String256;
import edu.tn.xds.metadata.editor.shared.model.XdsDocumentEntry;

import javax.inject.Inject;

public class FileUploadPresenter extends AbstractPresenter<FileUploadView> {

    @Inject
    PlaceController placeController;
    @Inject
    XdsParser xdsParser;

    public void fileUploaded(String results) {
        // remove xml file first line (xml doctype => <?xml...>)
        logger.info("... file loaded, parsing metadata...");

        results = results.replaceAll("&lt;", "<");
        results = results.replaceAll("&gt;", ">");
        results = results.replaceAll("&amp;", "&");
        XdsDocumentEntry model = xdsParser.parse(results.split(";\\^;\\^;")[1]);
        model.setFileName(new String256().setString(results.split(";\\^;\\^;")[0]));

        logger.info("... file parsed.");
        // logger.info("Metadata file: " + model.toXML());

        if (!(placeController.getWhere() instanceof EditorPlace)) {
            placeController.goTo(new EditorPlace());
        }
        getEventBus().fireEvent(new NewFileLoadedEvent(model));
    }

    @Override
    public void init() {
    }
}
