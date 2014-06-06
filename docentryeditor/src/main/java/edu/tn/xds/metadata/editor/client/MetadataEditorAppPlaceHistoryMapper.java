package edu.tn.xds.metadata.editor.client;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

import edu.tn.xds.metadata.editor.client.editor.EditorPlace;
import edu.tn.xds.metadata.editor.client.home.WelcomePlace;

@WithTokenizers({ WelcomePlace.Tokenizer.class, EditorPlace.Tokenizer.class })
public interface MetadataEditorAppPlaceHistoryMapper extends PlaceHistoryMapper {

}
