package edu.tn.xds.metadata.editor.client.editor.widgets.InternationalStringWidgets;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import edu.tn.xds.metadata.editor.client.editor.properties.InternationalStringProperties;
import edu.tn.xds.metadata.editor.client.editor.widgets.LanguageCodeComboBox;
import edu.tn.xds.metadata.editor.client.generics.GenericEditableGrid;
import edu.tn.xds.metadata.editor.client.generics.GridModelFactory;
import edu.tn.xds.metadata.editor.client.widgets.BoundedTextField;
import edu.tn.xds.metadata.editor.shared.model.InternationalString;
import edu.tn.xds.metadata.editor.shared.model.LanguageCode;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class InternationalStringEditableGrid extends GenericEditableGrid<InternationalString> {
    private final static InternationalStringProperties isprops
            = GWT.create(InternationalStringProperties.class);

    private static ColumnConfig<InternationalString, LanguageCode> languageCodeColumnConfig;
    private static ColumnConfig<InternationalString, String> titleColumnConfig;

    public InternationalStringEditableGrid(String gridTitle) {
        super(gridTitle, new ListStore<InternationalString>(isprops.key()), buildColumnModel());

//        setCheckBoxSelectionModel();

        LanguageCodeComboBox languageCodeComboBox = new LanguageCodeComboBox();
        languageCodeComboBox.setAllowBlank(false);
        languageCodeComboBox.setToolTip("The translation language's code is required. It can not be null. Please select one or delete the row.");
        BoundedTextField tf = new BoundedTextField();
        tf.setMaxLength(256);
        tf.setToolTip("This is the translation corresponding to the selected language. This field is required. It can not be null.");
        tf.setAllowBlank(false);
        addColumnEditorConfig(languageCodeColumnConfig, languageCodeComboBox);
        addColumnEditorConfig(titleColumnConfig, tf);

    }

    private static ColumnModel<InternationalString> buildColumnModel() {
        List<ColumnConfig<InternationalString, ?>> columnsConfigs = new ArrayList<ColumnConfig<InternationalString, ?>>();

        languageCodeColumnConfig = new ColumnConfig<InternationalString, LanguageCode>(
                isprops.langCode(), 15, "Language Code");
        titleColumnConfig = new ColumnConfig<InternationalString, String>(
                isprops.value(), 85, "Title");
        columnsConfigs.add(languageCodeColumnConfig);
        columnsConfigs.add(titleColumnConfig);
        return new ColumnModel<InternationalString>(
                columnsConfigs);
    }


    @Override
    protected GridModelFactory<InternationalString> getModelFactory() {
        return InternationalStringFactory.instance;
    }
}
