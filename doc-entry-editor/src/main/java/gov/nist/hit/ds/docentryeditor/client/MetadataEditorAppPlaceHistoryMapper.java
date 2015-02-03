package gov.nist.hit.ds.docentryeditor.client;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

import gov.nist.hit.ds.docentryeditor.client.editor.EditorPlace;
import gov.nist.hit.ds.docentryeditor.client.home.WelcomePlace;

@WithTokenizers({ WelcomePlace.Tokenizer.class, EditorPlace.Tokenizer.class })
public interface MetadataEditorAppPlaceHistoryMapper extends PlaceHistoryMapper {

}
