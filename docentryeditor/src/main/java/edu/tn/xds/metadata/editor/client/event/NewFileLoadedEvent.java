package edu.tn.xds.metadata.editor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.tn.xds.metadata.editor.client.event.NewFileLoadedEvent.NewFileLoadedHandler;
import edu.tn.xds.metadata.editor.shared.model.DocumentModel;

public class NewFileLoadedEvent extends GwtEvent<NewFileLoadedHandler> {

	public interface NewFileLoadedHandler extends EventHandler {
		public void onNewFileLoaded(NewFileLoadedEvent event);
	}

	public static Type<NewFileLoadedHandler> TYPE = new Type<NewFileLoadedHandler>();

	private final DocumentModel fileContent;

	public NewFileLoadedEvent(DocumentModel fileContent) {
		this.fileContent = fileContent;
	}

	public DocumentModel getDocument() {
		return fileContent;
	}

	@Override
	public Type<NewFileLoadedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NewFileLoadedHandler handler) {
		handler.onNewFileLoaded(this);
	}

}
