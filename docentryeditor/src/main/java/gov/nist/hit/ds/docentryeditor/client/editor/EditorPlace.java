package gov.nist.hit.ds.docentryeditor.client.editor;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Place for the Document Entry editor.
 * This enables the navigation within the application thanks to
 * the Activity-place design.
 */
public class EditorPlace extends Place {
	private String editorPlaceName;

	public EditorPlace(String token) {
		this.editorPlaceName = token;
	}

	public EditorPlace() {
		super();
	}

	public String getActivityEditorPlaceName() {
		return this.editorPlaceName;
	}

	public static class Tokenizer implements PlaceTokenizer<EditorPlace> {
		@Override
		public String getToken(EditorPlace place) {
			return place.getActivityEditorPlaceName();
		}

		@Override
		public EditorPlace getPlace(String token) {
			return new EditorPlace(token);
		}
	}

}
