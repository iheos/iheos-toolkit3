package gov.nist.hit.ds.docentryeditor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsDocumentEntry;

public class SaveCurrentlyEditedDocumentEvent extends GwtEvent<SaveCurrentlyEditedDocumentEvent.SaveCurrentlyEditedDocumentEventHandler> {

	public interface SaveCurrentlyEditedDocumentEventHandler extends EventHandler {
		public void onSaveCurrentlyEditedDocumentEvent(SaveCurrentlyEditedDocumentEvent event);
	}

    private XdsDocumentEntry documentEntry;

	public static Type<SaveCurrentlyEditedDocumentEventHandler> TYPE = new Type<SaveCurrentlyEditedDocumentEventHandler>();

	public SaveCurrentlyEditedDocumentEvent(XdsDocumentEntry documentEntry) {
        this.documentEntry=documentEntry;
	}

	@Override
	public Type<SaveCurrentlyEditedDocumentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveCurrentlyEditedDocumentEventHandler handler) {
		handler.onSaveCurrentlyEditedDocumentEvent(this);
	}

    public XdsDocumentEntry getDocumentEntry(){
        return documentEntry;
    }
}
