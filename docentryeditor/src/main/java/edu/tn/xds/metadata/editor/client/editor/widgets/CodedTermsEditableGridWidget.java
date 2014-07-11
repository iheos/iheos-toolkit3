package edu.tn.xds.metadata.editor.client.editor.widgets;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import edu.tn.xds.metadata.editor.client.editor.properties.CodedTermProperties;
import edu.tn.xds.metadata.editor.client.generics.GenericEditableGrid;
import edu.tn.xds.metadata.editor.shared.model.CodedTerm;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CodedTermsEditableGridWidget extends GenericEditableGrid<CodedTerm> {
    private final static CodedTermProperties isprops = GWT
            .create(CodedTermProperties.class);

    private static ColumnConfig<CodedTerm, String> displayNameColumnConfig;
    private static ColumnConfig<CodedTerm, String> codeColumnConfig;
    private static ColumnConfig<CodedTerm, String> codingSchemeColumnConfig;

    public CodedTermsEditableGridWidget(String gridTitle) {
        super(CodedTerm.class, gridTitle, new ListStore<CodedTerm>(isprops.key()), buildColumnModel());

        setCheckBoxSelectionModel();

        // Editing widgets
        TextField displayNameTF = new TextField();
        displayNameTF.setAllowBlank(false);
//        displayNameTF.setToolTip("This fields is required.");
        TextField codeTF = new TextField();
        codeTF.setAllowBlank(false);
        codeTF.setToolTip("This field is required.");
        TextField codingSchemeTF = new TextField();
        codingSchemeTF.setAllowBlank(false);
//        codingSchemeTF.setToolTip("This is required.");
        addColumnEditorConfig(displayNameColumnConfig, displayNameTF);
        addColumnEditorConfig(codeColumnConfig, codeTF);
        addColumnEditorConfig(codingSchemeColumnConfig, codingSchemeTF);

    }

    private static ColumnModel<CodedTerm> buildColumnModel() {
        List<ColumnConfig<CodedTerm, ?>> columnsConfigs = new ArrayList<ColumnConfig<CodedTerm, ?>>();

        displayNameColumnConfig = new ColumnConfig<CodedTerm, String>(
                isprops.displayName(), 20, "Display Name");
        codeColumnConfig = new ColumnConfig<CodedTerm, String>(
                isprops.code(), 30, "Code");
        codingSchemeColumnConfig = new ColumnConfig<CodedTerm, String>(
                isprops.codingScheme(), 50, "CodingScheme");

        columnsConfigs.add(displayNameColumnConfig);
        columnsConfigs.add(codeColumnConfig);
        columnsConfigs.add(codingSchemeColumnConfig);

        return new ColumnModel<CodedTerm>(columnsConfigs);
    }

}
