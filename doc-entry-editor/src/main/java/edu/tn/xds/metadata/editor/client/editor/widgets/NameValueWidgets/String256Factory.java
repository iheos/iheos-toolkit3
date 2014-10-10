package edu.tn.xds.metadata.editor.client.editor.widgets.NameValueWidgets;

import com.google.gwt.core.client.GWT;
import edu.tn.xds.metadata.editor.client.generics.GridModelFactory;
import edu.tn.xds.metadata.editor.shared.model.String256;

/**
 * String256 implementation of {@link edu.tn.xds.metadata.editor.client.generics.GridModelFactory}.
 */
public enum String256Factory implements GridModelFactory<String256> {
    instance;

    /**
     * String256 implementation for {@link edu.tn.xds.metadata.editor.client.generics.GridModelFactory#newInstance()}
     *
     * @return instance of String256
     */
    @Override
    public String256 newInstance() {
        return GWT.create(String256.class);
    }
}
