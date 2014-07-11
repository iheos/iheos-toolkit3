package edu.tn.xds.metadata.editor.client.editor.widgets;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import edu.tn.xds.metadata.editor.shared.model.IdentifierOID;

/**
 * <p>
 * <b>This class represents the widget which matches LanguageCode model type</b>
 * </p>
 */
public class IdentifierOIDEditorWidget extends Composite implements
        Editor<IdentifierOID> {
    VerticalLayoutContainer vcontainer = new VerticalLayoutContainer();
    OIDEditorWidget value = new OIDEditorWidget(false);
    String256EditorWidget idType = new String256EditorWidget();

    public IdentifierOIDEditorWidget() {
        initWidget(vcontainer);
        FieldLabel valueLabel = new FieldLabel(value, "Value");
        valueLabel.setLabelWidth(125);

        FieldLabel idTypeLabel = new FieldLabel(idType, "ID Type (OID)");
        idTypeLabel.setLabelWidth(125);
        idTypeLabel.setVisible(false);

//        vcontainer.add(valueLabel, new VerticalLayoutData(1, -1));
//        vcontainer.add(idTypeLabel, new VerticalLayoutData(1, -1));
        vcontainer.add(value);
    }

    public void addValueFieldValidator(Validator validator) {
        value.addValidator(validator);
    }

    /**
     * This method sets the default text to display in an empty field (defaults
     * to null). It is done to help and guide the user during his input.
     *
     * @param valueEmptyText  Default text displayed in an empty value field
     * @param idTypeEmptyText Default text displayed in an empty id type field
     */
    public void setEmptyTexts(String valueEmptyText, String idTypeEmptyText) {
        value.setEmptyText(valueEmptyText);
        idType.setEmptyText(idTypeEmptyText);
    }

    /**
     * Sets the widget's tool tip with the given config
     *
     * @param valueConfig
     * @param idTypeConfig
     */
    public void setToolTipConfigs(ToolTipConfig valueConfig,
                                  ToolTipConfig idTypeConfig) {
        value.setToolTipConfig(valueConfig);
        idType.setToolTipConfig(idTypeConfig);
    }

    /**
     * Sets whether a field is valid when its value length = 0 (default to
     * true). This will warn the user through the editor widget if he didn't
     * input anything in field which does not allow blank.
     *
     * @param b_value  true to allow blank to the value field, false otherwise
     * @param b_idType true to allow blank to the idType field, false otherwise
     */
    public void setAllowBlanks(boolean b_value, boolean b_idType) {
        value.setAllowBlank(b_value);
        idType.setAllowBlank(b_idType);
    }
}
