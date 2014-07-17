package edu.tn.xds.metadata.editor.client.editor.widgets.CodedTermWidgets;

import com.google.gwt.core.client.GWT;
import edu.tn.xds.metadata.editor.client.generics.GridModelFactory;
import edu.tn.xds.metadata.editor.shared.model.CodedTerm;

public enum CodedTermFactory implements GridModelFactory<CodedTerm> {
    instance;

    @Override
    public CodedTerm newInstance() {
        return GWT.create(CodedTerm.class);
    }
}
