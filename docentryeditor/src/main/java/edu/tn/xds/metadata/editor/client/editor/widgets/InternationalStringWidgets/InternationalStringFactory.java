package edu.tn.xds.metadata.editor.client.editor.widgets.InternationalStringWidgets;

import com.google.gwt.core.client.GWT;
import edu.tn.xds.metadata.editor.client.generics.GridModelFactory;
import edu.tn.xds.metadata.editor.shared.model.InternationalString;

public enum InternationalStringFactory implements GridModelFactory<InternationalString> {
    instance;

    @Override
    public InternationalString newInstance() {
        return GWT.create(InternationalString.class);
    }
}
