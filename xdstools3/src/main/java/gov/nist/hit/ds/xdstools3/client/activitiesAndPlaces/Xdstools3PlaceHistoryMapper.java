package gov.nist.hit.ds.xdstools3.client.activitiesAndPlaces;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

/**
 * Monitors PlaceChangeEvents and History events and keep them in sync.
 * It must know all the different Places used in the application.
 *
 * Created by onh2 on 9/22/2014.
 */
@WithTokenizers({TabPlace.Tokenizer.class})
public interface Xdstools3PlaceHistoryMapper extends PlaceHistoryMapper {
}
