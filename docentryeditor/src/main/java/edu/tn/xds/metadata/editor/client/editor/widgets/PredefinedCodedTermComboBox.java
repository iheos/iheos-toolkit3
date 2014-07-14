package edu.tn.xds.metadata.editor.client.editor.widgets;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import edu.tn.xds.metadata.editor.client.parse.PredefinedCodesParser;
import edu.tn.xds.metadata.editor.shared.model.CodedTerm;

import java.util.List;

public class PredefinedCodedTermComboBox extends ComboBox<CodedTerm> {

    public PredefinedCodedTermComboBox(PredefinedCodes predefinedCodes) {
        super(new ListStore<CodedTerm>(
                new ModelKeyProvider<CodedTerm>() {

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
                if (item.getCode() != null && !item.getCode().toString().equals(""))
                    s += item.getCode().toString();
                if (item.getDisplayName() != null && !item.getDisplayName().toString().equals("")) {
                    if (item.getCode() != null && !item.getCode().toString().equals(""))
                        s += "  -  ";
                    s += item.getDisplayName().toString();
                }
                if (item.getCodingScheme() != null && !item.getCodingScheme().toString().equals("")) {
                    if (!s.isEmpty())
                        s += "  -  ";
                    s += item.getCodingScheme().toString();
                }
                return s;
            }
        });

        getStore().clear();

        this.setEmptyText("Select a code...");

        List<CodedTerm> l = PredefinedCodesParser.INSTANCE
                .getCodes(predefinedCodes);

        getStore().add(new CodedTerm("", "Custom Value", ""));
        getStore().addAll(l);

        setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        setForceSelection(true);
        setTypeAhead(true);

        setText("");
        clear();

        bind();
    }

    private void bind() {
        this.addBeforeSelectionHandler(new BeforeSelectionHandler<CodedTerm>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<CodedTerm> event) {
                if (event.getItem().getDisplayName().toString().equals("Custom Value")) {
                    final Dialog dialog = new Dialog();
                    dialog.setBodyBorder(false);
                    dialog.setHeadingText("Custom Coded Term");
                    dialog.setHideOnButtonClick(true);
                    dialog.setWidth(600);
                    dialog.setHeight(180);

                    FramedPanel fpanel = new FramedPanel();
                    fpanel.setHeaderVisible(false);
                    VerticalLayoutContainer vcon = new VerticalLayoutContainer();
                    fpanel.add(vcon);

                    final CodedTermEditorWidget editor = new CodedTermEditorWidget();
                    editor.initEditorDriver();
                    editor.editNew();
                    editor.setAllowBlanks(false, false, false);


                    TextButton ok = new TextButton("Add ");
                    ok.setHeight(25);
                    ok.setWidth(80);
                    TextButton cancel = new TextButton("Cancel");
                    cancel.setHeight(25);
                    cancel.setWidth(80);
                    fpanel.addButton(ok);
                    fpanel.addButton(cancel);
                    fpanel.setButtonAlign(BoxLayoutContainer.BoxLayoutPack.CENTER);

                    ok.addSelectHandler(new SelectEvent.SelectHandler() {
                        @Override
                        public void onSelect(SelectEvent event) {
                            CodedTerm added = editor.save();
                            if (added != null) {
                                getStore().add(added);
                                // FIXME select does not work
                                select(added);
                                redraw(true);
                                dialog.hide();
                            }
                        }
                    });

                    cancel.addSelectHandler(new SelectEvent.SelectHandler() {
                        @Override
                        public void onSelect(SelectEvent event) {
                            setValue(null);
                            redraw(true);
                            dialog.hide();
                        }
                    });

                    vcon.add(editor, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10)));

                    dialog.add(fpanel);

                    // delete the default button
                    dialog.getButtonBar().remove(0);
                    dialog.setModal(true);
                    dialog.show();
                }
            }
        });
    }

    public enum PredefinedCodes {
        CLASS_CODES, CONFIDENTIALITY_CODES, FORMAT_CODES, HEALTHCARE_FACILITY_TYPE_CODES, PRACTICE_SETTING_CODES, EVENT_CODES, TYPE_CODES;
    }

}
