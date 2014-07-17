package edu.tn.xds.metadata.editor.client.editor.widgets.CodedTermWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
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
 */
public class CodedTermsEditableGridWidget extends GenericEditableGrid<CodedTerm> {
    private final static CodedTermProperties isprops = GWT
            .create(CodedTermProperties.class);

    private static ColumnConfig<CodedTerm, String> displayNameColumnConfig;
    private static ColumnConfig<CodedTerm, String> codeColumnConfig;
    private static ColumnConfig<CodedTerm, String> codingSchemeColumnConfig;

    private PredefinedCodedTermComboBox cb;

    public CodedTermsEditableGridWidget(String gridTitle, PredefinedCodes predefinedCode) {
        super(gridTitle, new ListStore<CodedTerm>(isprops.key()), buildColumnModel());

        ((TextButton) getToolbar().getWidget(0)).setToolTip("Add a custom value");

        cb = new PredefinedCodedTermComboBox(predefinedCode);
        cb.getStore().remove(0);

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

        bindUI();
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

    private void bindUI() {
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

    @Override
    public Widget asWidget() {
        VerticalLayoutContainer vc = new VerticalLayoutContainer();
        FieldLabel codedTermFL = new FieldLabel(cb, "Select a coded term to add");
        codedTermFL.setLabelWidth(200);
        vc.add(super.asWidget(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        vc.add(codedTermFL, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(3, 3, 15, 5)));
        return vc;
    }

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
