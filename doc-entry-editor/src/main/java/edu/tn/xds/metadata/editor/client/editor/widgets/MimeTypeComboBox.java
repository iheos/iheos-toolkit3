package edu.tn.xds.metadata.editor.client.editor.widgets;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import edu.tn.xds.metadata.editor.client.parse.PredefinedCodesParser;
import edu.tn.xds.metadata.editor.shared.model.String256;

import java.util.List;

/**
 * ComboBox widget for MimeTypes with pre-loaded possible values.
 */
public class MimeTypeComboBox extends ComboBox<String256> {
    public MimeTypeComboBox(){
        super(new ListStore<String256>(
                new ModelKeyProvider<String256>() {

                    @Override
                    public String getKey(String256 item) {
                        if (item == null) {
                            return "NULL";
                        }
                        return item.toString();
                    }
                }), new LabelProvider<String256>() {

            @Override
            public String getLabel(String256 item) {
                return item.toString();
            }
        });
        getStore().clear();

        List<String256> l = PredefinedCodesParser.INSTANCE
                .getMimeTypes();

        this.setEmptyText("Select a type...");

        getStore().addAll(l);

        setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        setForceSelection(true);
        setTypeAhead(true);
    }
}
