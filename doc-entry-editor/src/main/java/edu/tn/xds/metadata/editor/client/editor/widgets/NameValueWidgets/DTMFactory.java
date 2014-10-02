package edu.tn.xds.metadata.editor.client.editor.widgets.NameValueWidgets;

import com.google.gwt.core.client.GWT;
import edu.tn.xds.metadata.editor.client.generics.GridModelFactory;
import edu.tn.xds.metadata.editor.shared.model.DTM;

public enum DTMFactory implements GridModelFactory<DTM> {
    instance;

    @Override
    public DTM newInstance() {
        return GWT.create(DTM.class);
    }
}
