package edu.tn.xds.metadata.editor.client.widgets.uploader;

import javax.inject.Inject;

import com.google.gwt.place.shared.PlaceController;

import edu.tn.xds.metadata.editor.client.editor.EditorPlace;
import edu.tn.xds.metadata.editor.client.event.NewFileLoadedEvent;
import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractPresenter;
import edu.tn.xds.metadata.editor.client.parse.Parse;
import edu.tn.xds.metadata.editor.shared.model.DocumentModel;

public class FileUploadPresenter extends AbstractPresenter<FileUploadView> {

	// @Inject
	// private final Parse myParse = new Parse();
	private Parse myParse;

	@Inject
	PlaceController placeController;

	public void fileUploaded(String results) {
		myParse = Parse.getInstance();

		// remove xml file first line (xml doctype => <?xml...>)
		logger.info("... file loaded, parsing metadata...");

		results = results.replaceAll("&lt;", "<");
		results = results.replaceAll("&gt;", ">");
		results = results.replaceAll("&amp;", "&");

		DocumentModel model = myParse.doParse(results);

		logger.info("Metadata file: " + model.toXML());

		// logger.info(myModel.toXML());
		placeController.goTo(new EditorPlace());
		getEventBus().fireEvent(new NewFileLoadedEvent(model));
	}
}
