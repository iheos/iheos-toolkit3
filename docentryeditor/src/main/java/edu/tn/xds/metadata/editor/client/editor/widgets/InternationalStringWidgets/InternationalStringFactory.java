package edu.tn.xds.metadata.editor.client.editor.widgets.InternationalStringWidgets;

import com.google.gwt.core.client.GWT;
import edu.tn.xds.metadata.editor.client.generics.GridModelFactory;
import edu.tn.xds.metadata.editor.shared.model.InternationalString;
import edu.tn.xds.metadata.editor.shared.model.LanguageCode;
import edu.tn.xds.metadata.editor.shared.model.String256;

/**
 * InternationalString implementation of {@link edu.tn.xds.metadata.editor.client.generics.GridModelFactory}.
 */
public enum InternationalStringFactory implements GridModelFactory<InternationalString> {
    instance;

    /**
     * InternationalString implementation for {@link edu.tn.xds.metadata.editor.client.generics.GridModelFactory#newInstance()}
     *
     * @return instance of InternationalString
     */
    @Override
    public InternationalString newInstance() {
        InternationalString is = GWT.create(InternationalString.class);
        is.setLangCode(LanguageCode.ENGLISH_UNITED_STATES);
        is.setValue(new String256("New translation"));
        return is;
    }
}
