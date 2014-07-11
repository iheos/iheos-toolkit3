package edu.tn.xds.metadata.editor.client.editor.widgets;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import edu.tn.xds.metadata.editor.client.parse.PredefinedCodesParser;
import edu.tn.xds.metadata.editor.shared.model.CodedTerm;

import java.util.List;

public class PredefinedCodesComboBox extends ComboBox<CodedTerm> {

    public PredefinedCodesComboBox(PredefinedCodes predefinedCodes) {
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
                return item.getCode() + " - " + item.getDisplayName().toString() + " - " + item.getCodingScheme();
            }
        });

        getStore().clear();

        this.setEmptyText("Select a code...");

        List<CodedTerm> l = PredefinedCodesParser.INSTANCE
                .getCodes(predefinedCodes);

        getStore().addAll(l);

        setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        setForceSelection(true);
        setTypeAhead(true);

        setText("");
        clear();
    }

    public enum PredefinedCodes {
        CLASS_CODES, CONFIDENTIALITY_CODES, FORMAT_CODES, HEALTHCARE_FACILITY_TYPE_CODES, PRACTICE_SETTING_CODES, EVENT_CODES, TYPE_CODES;
    }

}
