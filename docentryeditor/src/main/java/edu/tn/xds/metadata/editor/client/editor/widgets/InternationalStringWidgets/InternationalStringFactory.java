package edu.tn.xds.metadata.editor.client.editor.widgets.InternationalStringWidgets;

import com.google.gwt.core.client.GWT;
import edu.tn.xds.metadata.editor.client.generics.GridModelFactory;
import edu.tn.xds.metadata.editor.shared.model.InternationalString;
import edu.tn.xds.metadata.editor.shared.model.LanguageCode;
import edu.tn.xds.metadata.editor.shared.model.String256;

public enum InternationalStringFactory implements GridModelFactory<InternationalString> {
    instance;

    @Override
    public InternationalString newInstance() {
        InternationalString is = GWT.create(InternationalString.class);
        is.setLangCode(LanguageCode.ENGLISH_UNITED_STATES);
        is.setValue(new String256("New translation"));
        return is;
    }
}
