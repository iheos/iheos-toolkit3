package edu.tn.xds.metadata.editor.client.editor.widgets.NameValueWidgets;

import edu.tn.xds.metadata.editor.client.generics.GridModelFactory;

/**
 * Integer implementation of {@link edu.tn.xds.metadata.editor.client.generics.GridModelFactory}.
 */
public enum IntegerFactory implements GridModelFactory<Integer> {
    instance;

    /**
     * Integer implementation for {@link edu.tn.xds.metadata.editor.client.generics.GridModelFactory#newInstance()}
     *
     * @return instance of Integer
     */
    @Override
    public Integer newInstance() {
        return new Integer(0);
    }
}
