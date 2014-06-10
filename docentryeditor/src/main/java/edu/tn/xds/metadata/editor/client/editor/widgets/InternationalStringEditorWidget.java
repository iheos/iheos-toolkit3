package edu.tn.xds.metadata.editor.client.editor.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

import edu.tn.xds.metadata.editor.client.editor.EditionMode;
import edu.tn.xds.metadata.editor.shared.model.InternationalString;

/**
 * This is a widget which enables to edit a single international String. It might be a title or comment for instance.
 * To use it you must call for call for it's initEditorDriver method at a higher level (in the presenter for example).
 */
public class InternationalStringEditorWidget extends Composite implements Editor<InternationalString> {
    // private static Logger logger =
    // Logger.getLogger(InternationalStringEditorWidget.class.getName());

    interface InternationalStringEditorDriver extends SimpleBeanEditorDriver<InternationalString, InternationalStringEditorWidget> {
    }

    // Driver for the edition of each author of authors list
    private final InternationalStringEditorDriver internationalStringEditorDriver = GWT.create(InternationalStringEditorDriver.class);

    private final VerticalLayoutContainer vcontainer = new VerticalLayoutContainer();

    LanguageCodeComboBox langCode = new LanguageCodeComboBox();
    String256EditorWidget value = new String256EditorWidget();

    @Ignore
    private InternationalString model;

    @Ignore
    private EditionMode editionMode = EditionMode.NODATA;

    public InternationalStringEditorWidget() {
        initWidget(vcontainer);
        FieldLabel langCodeFL = new FieldLabel(langCode, "Language Code");
        FieldLabel valueFL = new FieldLabel(value, "Value");
        langCode.setWidth("auto");
        value.setWidth("auto");
        vcontainer.add(langCodeFL);
        vcontainer.add(valueFL);

        bindUI();
    }

    @Override
    public boolean isAttached() {
        return super.isAttached();
    }

    private void bindUI() {

    }

    public void initEditorDriver() {
        internationalStringEditorDriver.initialize(this);
    }

    public void edit(InternationalString internationalString) {
        resetWidgets();
        setModel(internationalString);
        internationalStringEditorDriver.edit(internationalString);
    }

    private void resetWidgets() {
        langCode.clear();
        value.string.clear();
    }

    public void rollbackChanges() {
        switch (editionMode) {
            case NEW:
                resetEditor();
                break;
            case EDIT:
                resetEditor();
                break;
            default:
                break;
        }

    }

    private void resetEditor() {
        setEditionMode(EditionMode.NODATA);
        model = null;
        internationalStringEditorDriver.edit(model);
        // editNew();
    }

    public void save() {
        model = internationalStringEditorDriver.flush();
        if (internationalStringEditorDriver.hasErrors()) {
            Dialog d = new Dialog();
            d.setHeadingText("Errors dialog");
            d.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
            d.setBodyStyleName("pad-text");
            d.add(new Label(internationalStringEditorDriver.getErrors().toString()));
            d.getBody().addClassName("pad-text");
            d.setHideOnButtonClick(true);
            d.setWidth(300);
        }
    }

    protected void setWidgetEnable(boolean enabled) {
        langCode.setEnabled(enabled);
        value.string.setEnabled(enabled);
    }

    /**
     * This method sets the default text to display in an empty field (defaults
     * to null). It is done to help and guide the user during his input.
     *
     * @param langCodeEmptyText
     * @param valueEmptyText
     */
    public void setEmptyText(String langCodeEmptyText, String valueEmptyText) {
        langCode.setEmptyText(langCodeEmptyText);
        value.setEmptyText(valueEmptyText);
    }

    /**
     * Sets the widget's tool tip with the given config
     *
     * @param langCodeConfig
     * @param valueConfig
     */
    public void setToolTipConfig(ToolTipConfig langCodeConfig, ToolTipConfig valueConfig) {
        langCode.setToolTipConfig(langCodeConfig);
        value.setToolTipConfig(valueConfig);
    }

    /**
     * Sets whether a field is valid when its value length = 0 (default to
     * true). This will warn the user through the editor widget if he didn't
     * input anything in field which does not allow blank.
     *
     * @param langCodeBlankAllowed
     */
    public void setAllowBlank(boolean langCodeBlankAllowed) {
        langCode.setAllowBlank(langCodeBlankAllowed);
    }

    public void setEditionMode(EditionMode editionMode) {
        this.editionMode = editionMode;
        if (editionMode.equals(EditionMode.DISPLAY) || editionMode.equals(EditionMode.NODATA)) {
            setWidgetEnable(false);
        } else if (editionMode.equals(EditionMode.NEW) || editionMode.equals(EditionMode.EDIT)) {
            setWidgetEnable(true);
        }
    }

    private void setModel(InternationalString internationalString) {
        this.model = internationalString;
    }

    public void editNew() {
        model = new InternationalString();
        edit(model);
        resetWidgets();
    }

    public EditionMode getEditionMode() {
        return editionMode;
    }

    public InternationalString getModel() {
        return model;
    }
}
