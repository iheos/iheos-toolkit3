package edu.tn.xds.metadata.editor.client.editor.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import edu.tn.xds.metadata.editor.client.editor.properties.CodedTermProperties;
import edu.tn.xds.metadata.editor.client.generics.GenericEditableGrid;
import edu.tn.xds.metadata.editor.shared.model.CodedTerm;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CodedTermsEditableGridWidget extends Composite {
    private final CodedTermProperties isprops = GWT
            .create(CodedTermProperties.class);

    GenericEditableGrid<CodedTerm> grid;

    public CodedTermsEditableGridWidget(String gridTitle) {
        super();

        List<ColumnConfig<CodedTerm, ?>> columnsConfigs = new ArrayList<ColumnConfig<CodedTerm, ?>>();

        ColumnConfig<CodedTerm, String> cc1 = new ColumnConfig<CodedTerm,String>(
                isprops.displayName(), 20, "Display Name");
        ColumnConfig<CodedTerm, String> cc2 = new ColumnConfig<CodedTerm, String>(
                isprops.code(), 30, "Code");
        ColumnConfig<CodedTerm, String> cc3 = new ColumnConfig<CodedTerm, String>(
                isprops.codingScheme(), 50, "CodingScheme");

        columnsConfigs.add(cc1);
        columnsConfigs.add(cc2);
        columnsConfigs.add(cc3);

        ColumnModel<CodedTerm> cm = new ColumnModel<CodedTerm>(
                columnsConfigs);

        grid = new GenericEditableGrid<CodedTerm>(CodedTerm.class,gridTitle,
                new ListStore<CodedTerm>(isprops.key()), cm);

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
        grid.addColumnEditorConfig(cc1, displayNameTF);
        grid.addColumnEditorConfig(cc2, codeTF);
        grid.addColumnEditorConfig(cc3,codingSchemeTF);

    }

    @Override
    public Widget asWidget() {
        return grid.asWidget();
    }

    public Grid<CodedTerm> getGrid() {
        return grid;
    }
}
