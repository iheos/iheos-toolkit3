package edu.tn.xds.metadata.editor.client.editor.widgets.NameValueWidgets;

import com.google.gwt.core.client.GWT;
import edu.tn.xds.metadata.editor.client.generics.GridModelFactory;
import edu.tn.xds.metadata.editor.shared.model.String256;

public enum String256Factory implements GridModelFactory<String256> {
    instance;

    @Override
    public String256 newInstance() {
        return GWT.create(String256.class);
    }
}
