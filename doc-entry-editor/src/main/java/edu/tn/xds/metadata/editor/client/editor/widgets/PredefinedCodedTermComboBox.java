package edu.tn.xds.metadata.editor.client.editor.widgets;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import edu.tn.xds.metadata.editor.client.editor.EditionMode;
import edu.tn.xds.metadata.editor.client.editor.widgets.CodedTermWidgets.CodedTermEditorWidget;
import edu.tn.xds.metadata.editor.client.parse.PredefinedCodesParser;
import edu.tn.xds.metadata.editor.shared.model.CodedTerm;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Custom ComboBox for CodedTerms with pre-loaded possible values depending on the type of CodedTerm.
 *
 * @see edu.tn.xds.metadata.editor.client.editor.widgets.PredefinedCodes
 */
public class PredefinedCodedTermComboBox extends ComboBox<CodedTerm> {

    private TextButton editCustomedValueBtn = new TextButton("Edit");
    private List<Integer> customedValuesIndexes = new ArrayList<Integer>();
    private int selectedIndex = -1;

    public PredefinedCodedTermComboBox(PredefinedCodes predefinedCodes) {
        super(new ListStore<CodedTerm>(new ModelKeyProvider<CodedTerm>() {

            @Override
            public String getKey(CodedTerm item) {
                if (item == null) {
                    return "NULL";
                }
                return item.toString();
            }
        }), new LabelProvider<CodedTerm>() {

            @Override
            public String getLabel(CodedTerm item) {
                String s = new String();
                if (item.getDisplayName() != null)
                    if (!item.getDisplayName().toString().equals(""))
                        s += item.getDisplayName().toString();
                if (item.getDisplayName() != null)
                    if (!item.getCode().toString().equals("") && item.getDisplayName() != null) {
                        if (item.getDisplayName().toString() != null && !item.getDisplayName().toString().equals(""))
                            s += "  -  ";
                        s += item.getCode().toString();
                    }
                if (item.getCodingScheme() != null)
                    if (!item.getCodingScheme().toString().equals("")) {
                        if (!s.isEmpty())
                            s += "  -  ";
                        s += item.getCodingScheme().toString();
                    }
                return s;
            }
        });

        getStore().clear();

        this.setEmptyText("Select a code...");

        List<CodedTerm> l = PredefinedCodesParser.INSTANCE.getCodes(predefinedCodes);

        getStore().add(new CodedTerm("", "Custom Value", ""));
        getStore().addAll(l);

        setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        setForceSelection(true);
        setTypeAhead(true);

        editCustomedValueBtn.disable();

        setText("");
        clear();

        bind();
    }

    public Widget getDisplay() {
        HorizontalLayoutContainer hcon = new HorizontalLayoutContainer();
        hcon.add(super.asWidget(), new HorizontalLayoutContainer.HorizontalLayoutData(1, 0.1, new Margins(0, 2, 0, 0)));
        hcon.add(editCustomedValueBtn, new HorizontalLayoutContainer.HorizontalLayoutData(-1, -1, new Margins(0, 1, 0, 2)));
        return hcon;
    }

    private void bind() {
        this.addBeforeSelectionHandler(new BeforeSelectionHandler<CodedTerm>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<CodedTerm> event) {
                if (event.getItem().getDisplayName().toString().equals("Custom Value")) {
                    final CodedTermPopUpEditor codedTermPopUpEditor = new CodedTermPopUpEditor();

                    codedTermPopUpEditor.getOkButton().addSelectHandler(new SelectEvent.SelectHandler() {
                        @Override
                        public void onSelect(SelectEvent event) {
                            CodedTerm added = codedTermPopUpEditor.getEditor().save();
                            if (added.getCode().toString() != null && added.getCodingScheme().toString() != null
                                    && added.getDisplayName().toString() != null) {
                                getStore().add(added);
                                select(added);
                                setValue(added);
                                customedValuesIndexes.add(getStore().indexOf(added));
                                redraw(true);
                                editCustomedValueBtn.enable();
                                codedTermPopUpEditor.hide();
                            }
                        }
                    });
                    codedTermPopUpEditor.getCancelButton().addSelectHandler(new SelectEvent.SelectHandler() {
                        @Override
                        public void onSelect(SelectEvent event) {
                            resetSelection();
                        }
                    });
                }
                selectedIndex = getStore().indexOf(event.getItem());
                if (customedValuesIndexes.contains(selectedIndex)) {
                    Logger.getLogger(this.getClass().getName()).info("Index: " + customedValuesIndexes.indexOf(selectedIndex));
                    editCustomedValueBtn.enable();
                } else {
                    editCustomedValueBtn.disable();
                }
            }
        });
        editCustomedValueBtn.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                final CodedTermPopUpEditor codedTermPopUpEditor = new CodedTermPopUpEditor();
                codedTermPopUpEditor.getOkButton().setText("Save changes");
                final CodedTerm currentValue = getCurrentValue();
                codedTermPopUpEditor.getEditor().edit(currentValue);
                codedTermPopUpEditor.getOkButton().addSelectHandler(new SelectEvent.SelectHandler() {
                    @Override
                    public void onSelect(SelectEvent event) {
                        CodedTerm updated = codedTermPopUpEditor.getEditor().save();
                        if (updated.getCode().toString() != null && updated.getCodingScheme().toString() != null
                                && updated.getDisplayName().toString() != null) {
                            customedValuesIndexes.remove(customedValuesIndexes.indexOf(getStore().indexOf(currentValue)));
                            getStore().remove(currentValue);
                            getStore().add(updated);
                            select(updated);
                            setValue(updated);
                            customedValuesIndexes.add(getStore().indexOf(updated));
                            redraw(true);
                            editCustomedValueBtn.enable();
                            codedTermPopUpEditor.hide();
                        }
                    }
                });
            }
        });
    }

    private void resetSelection() {
        setValue(null);
        redraw(true);
    }

    /**
     * Popup editor for CodedTerm custom values
     */
    private class CodedTermPopUpEditor extends Dialog {

        private final TextButton cancelButton;
        private final TextButton okButton;
        private final CodedTermEditorWidget editor;

        public CodedTermPopUpEditor() {
            super();
            setBodyBorder(false);
            setHeadingText("Custom Coded Term");
            setHideOnButtonClick(true);
            setWidth(600);
            setHeight(180);

            FramedPanel fpanel = new FramedPanel();
            fpanel.setHeaderVisible(false);
            VerticalLayoutContainer vcon = new VerticalLayoutContainer();
            fpanel.add(vcon);

            editor = new CodedTermEditorWidget();
            editor.initEditorDriver();
            editor.editNew();
            editor.setAllowBlanks(false, false, false);

            okButton = new TextButton("Add ");
            okButton.setHeight(25);
            okButton.setWidth(80);
            cancelButton = new TextButton("Cancel");
            cancelButton.setHeight(25);
            cancelButton.setWidth(80);
            fpanel.addButton(okButton);
            fpanel.addButton(cancelButton);
            fpanel.setButtonAlign(BoxLayoutContainer.BoxLayoutPack.CENTER);

            vcon.add(editor, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10)));

            add(fpanel);

            // delete the default button
            getButtonBar().remove(0);
            setModal(true);
            show();

            getEditor().setFocus();

            bind();
        }

        private void bind() {
            cancelButton.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    editor.setEditionMode(EditionMode.NODATA);
                    hide();
                }
            });
        }

        public TextButton getCancelButton() {
            return cancelButton;
        }

        public CodedTermEditorWidget getEditor() {
            return editor;
        }

        public TextButton getOkButton() {
            return okButton;
        }
    }

}
