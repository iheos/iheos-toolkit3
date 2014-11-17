package edu.tn.xds.metadata.editor.client.editor.widgets.NameValueWidgets;

import com.google.gwt.core.client.GWT;
import edu.tn.xds.metadata.editor.client.generics.GridModelFactory;
import edu.tn.xds.metadata.editor.shared.model.DTM;

/**
 * DTM implementation of {@link edu.tn.xds.metadata.editor.client.generics.GridModelFactory}.
 */
public enum DTMFactory implements GridModelFactory<DTM> {
    instance;

    /**
     * DTM implementation for {@link edu.tn.xds.metadata.editor.client.generics.GridModelFactory#newInstance()}
     *
     * @return instance of DTM
     */
    @Override
    public DTM newInstance() {
        return GWT.create(DTM.class);
    }
}
