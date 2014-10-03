package edu.tn.xds.metadata.editor.client.editor.widgets.NameValueWidgets;

import edu.tn.xds.metadata.editor.client.generics.GridModelFactory;

public enum IntegerFactory implements GridModelFactory<Integer> {
    instance;

    @Override
    public Integer newInstance() {
        return new Integer(0);
    }
}
