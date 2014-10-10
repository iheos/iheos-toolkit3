package edu.tn.xds.metadata.editor.client.editor.widgets.CodedTermWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import edu.tn.xds.metadata.editor.client.editor.properties.CodedTermProperties;
import edu.tn.xds.metadata.editor.client.editor.widgets.PredefinedCodedTermComboBox;
import edu.tn.xds.metadata.editor.client.editor.widgets.PredefinedCodes;
import edu.tn.xds.metadata.editor.client.generics.GenericEditableGrid;
import edu.tn.xds.metadata.editor.client.generics.GridModelFactory;
import edu.tn.xds.metadata.editor.shared.model.CodedTerm;

import java.util.ArrayList;
import java.util.List;

/**
 * Editable Grid handling CodedTerm elements.
 */
public class CodedTermsEditableGridWidget extends GenericEditableGrid<CodedTerm> {
    private final static CodedTermProperties isprops = GWT
            .create(CodedTermProperties.class);

    private static ColumnConfig<CodedTerm, String> displayNameColumnConfig;
    private static ColumnConfig<CodedTerm, String> codeColumnConfig;
    private static ColumnConfig<CodedTerm, String> codingSchemeColumnConfig;

    private PredefinedCodedTermComboBox cb;

    public CodedTermsEditableGridWidget(String gridTitle, PredefinedCodes predefinedCode) {
        super(gridTitle, new ListStore<CodedTerm>(isprops.key()));

        ((TextButton) getToolbar().getWidget(0)).setToolTip("Add a custom value");

        // initiate grid's extra widget combobox
        cb = new PredefinedCodedTermComboBox(predefinedCode);
        cb.getStore().remove(0); // remove "Add custom value" combobox's entry

        bindUI();
    }

    @Override
    protected ColumnModel<CodedTerm> buildColumnModel() {
        List<ColumnConfig<CodedTerm, ?>> columnsConfigs = new ArrayList<ColumnConfig<CodedTerm, ?>>();
        // define grid's display name column
        displayNameColumnConfig = new ColumnConfig<CodedTerm, String>(
                isprops.displayName(), 20, "Display Name");
        // define grid's code column
        codeColumnConfig = new ColumnConfig<CodedTerm, String>(
                isprops.code(), 30, "Code");
        // define grid's coding scheme column
        codingSchemeColumnConfig = new ColumnConfig<CodedTerm, String>(
                isprops.codingScheme(), 50, "CodingScheme");

        columnsConfigs.add(displayNameColumnConfig);
        columnsConfigs.add(codeColumnConfig);
        columnsConfigs.add(codingSchemeColumnConfig);

        return new ColumnModel<CodedTerm>(columnsConfigs);
    }

    @Override
    protected void buildEditingFields() {
        TextField displayNameTF = new TextField();
        displayNameTF.setAllowBlank(false);

        TextField codeTF = new TextField();
        codeTF.setAllowBlank(false);
        codeTF.setToolTip("This field is required.");
        TextField codingSchemeTF = new TextField();
        codingSchemeTF.setAllowBlank(false);

        addColumnEditorConfig(displayNameColumnConfig, displayNameTF);
        addColumnEditorConfig(codeColumnConfig, codeTF);
        addColumnEditorConfig(codingSchemeColumnConfig, codingSchemeTF);
    }

    /**
     * Binds UI with right actions
     */
    private void bindUI() {
        // set handler for extra widget combobox selection change
        cb.addSelectionHandler(new SelectionHandler<CodedTerm>() {
            @Override
            public void onSelection(SelectionEvent<CodedTerm> event) {
                if (!getStore().getAll().contains(event.getSelectedItem()))
                    getStore().add(event.getSelectedItem());
                cb.getStore().remove(event.getSelectedItem());
            }
        });
    }

    @Override
    protected GridModelFactory<CodedTerm> getModelFactory() {
        return CodedTermFactory.instance;
    }

    /**
     * Method that return the entire EditableGrid widget's UI with extra widget. It should be used
     * as asWidget() method and have been added to avoid an behavior error due to asWidget() use elsewhere.
     *
     * @return entire EditableGrid widget's UI
     */
    @Override
    public Widget getDisplay() {
        FieldLabel codedTermFL = new FieldLabel(cb, "Select a coded term to add");
        codedTermFL.setLabelWidth(200);
        addWidget(codedTermFL);
        return super.getDisplay();
    }

    /**
     * Custom implementation of {@link edu.tn.xds.metadata.editor.client.generics.GenericEditableGrid#clearStoreAction()}
     */
    @Override
    protected void clearStoreAction() {
        for (CodedTerm c : getStore().getAll()) {
            if (!cb.getStore().getAll().contains(c)) {
                cb.getStore().add(c);
            }
        }
        super.clearStoreAction();
        view.refresh(false);
    }

    /**
     * Custom implementation of {@link edu.tn.xds.metadata.editor.client.generics.GenericEditableGrid#deleteItemAction()}
     */
    @Override
    protected void deleteItemAction() {
        for (CodedTerm e : getSelectionModel().getSelectedItems()) {
            getStore().remove(e);
            getStore().commitChanges();
            if (!cb.getStore().getAll().contains(e)) {
                cb.getStore().add(e);
            }
        }
        view.refresh(false);
    }

}
