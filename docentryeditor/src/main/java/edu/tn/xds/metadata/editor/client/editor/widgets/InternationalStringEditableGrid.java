package edu.tn.xds.metadata.editor.client.editor.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import edu.tn.xds.metadata.editor.client.editor.properties.InternationalStringProperties;
import edu.tn.xds.metadata.editor.client.generics.GenericEditableGrid;
import edu.tn.xds.metadata.editor.shared.model.InternationalString;
import edu.tn.xds.metadata.editor.shared.model.LanguageCode;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class InternationalStringEditableGrid extends Composite {
    private final InternationalStringProperties isprops = GWT
            .create(InternationalStringProperties.class);

    GenericEditableGrid<InternationalString> grid;

    public InternationalStringEditableGrid(String gridTitle) {
        super();

        List<ColumnConfig<InternationalString, ?>> columnsConfigs = new ArrayList<ColumnConfig<InternationalString, ?>>();

        ColumnConfig<InternationalString, LanguageCode> cc1 = new ColumnConfig<InternationalString, LanguageCode>(
                isprops.langCode(), 15, "Language Code");
        ColumnConfig<InternationalString, String> cc2 = new ColumnConfig<InternationalString, String>(
                isprops.value(), 85, "Title");
        columnsConfigs.add(cc1);
        columnsConfigs.add(cc2);
        ColumnModel<InternationalString> cm = new ColumnModel<InternationalString>(
                columnsConfigs);

        grid = new GenericEditableGrid<InternationalString>(InternationalString.class,gridTitle,
                new ListStore<InternationalString>(isprops.key()), cm);

        grid.setCheckBoxSelectionModel();

        LanguageCodeComboBox languageCodeComboBox = new LanguageCodeComboBox();
        languageCodeComboBox.setAllowBlank(false);
        languageCodeComboBox.setToolTip("The translation language's code is required. It can not be null. Please select one or delete the row.");
        TextField tf = new TextField();
        tf.setToolTip("This is the translation corresponding to the selected language. This field is required. It can not be null.");
        tf.setAllowBlank(false);
        grid.addColumnEditorConfig(cc1, languageCodeComboBox);
        grid.addColumnEditorConfig(cc2, tf);

    }


    @Override
    public Widget asWidget() {
        return grid.asWidget();
    }

    public Grid<InternationalString> getGrid() {
        return grid;
    }
}
