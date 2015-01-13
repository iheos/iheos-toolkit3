package edu.tn.xds.metadata.editor.client;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import edu.tn.xds.metadata.editor.client.editor.EditorPlace;
import edu.tn.xds.metadata.editor.client.home.WelcomePlace;

import javax.inject.Inject;

public class MetadataEditorActivityMapper implements ActivityMapper {

    MetadataEditorGinInjector injector = MetadataEditorGinInjector.instance;

    @Inject
    public MetadataEditorActivityMapper() {
        super();
    }

    @Override
    public Activity getActivity(Place place) {
        AbstractActivity activity = null;

        // WELCOME
        if (place instanceof WelcomePlace) {
            activity = injector.getWelcomeActivity();
        } else if (place instanceof EditorPlace) {
            activity = injector.getEditorActivity();
        }
        return activity;
    }

}
