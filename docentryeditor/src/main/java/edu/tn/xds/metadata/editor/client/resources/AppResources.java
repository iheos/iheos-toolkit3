package edu.tn.xds.metadata.editor.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface AppResources extends ClientBundle {
    AppResources INSTANCE = GWT.create(AppResources.class);

    @Source("codes.xml")
    TextResource codes();

    @Source("xds-prefill.xml")
    TextResource xdsPrefill();
}
