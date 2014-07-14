package edu.tn.xds.metadata.editor.client.editor.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import edu.tn.xds.metadata.editor.client.editor.EditionMode;
import edu.tn.xds.metadata.editor.shared.model.CodedTerm;

/**
 * <p>
 * <b>This class represents the widget which matches CodedTerm model type</b>
 * </p>
 */
public class CodedTermEditorWidget extends Composite implements Editor<CodedTerm> {

    @Ignore
    CodedTermEditorDriver codedTermEditorDriver = GWT.create(CodedTermEditorDriver.class);
    @Ignore
    EditionMode editionMode = EditionMode.NODATA;
    @Ignore
	CodedTerm model;
	VerticalLayoutContainer vcontainer = new VerticalLayoutContainer();
	String256EditorWidget displayName = new String256EditorWidget();
	String256EditorWidget code = new String256EditorWidget();
	CodingSchemeEditorWidget codingScheme = new CodingSchemeEditorWidget();

    public CodedTermEditorWidget() {
        initWidget(vcontainer);
        FieldLabel displayNameLabel = new FieldLabel(displayName, "Display Name");
        displayNameLabel.setLabelWidth(125);

        FieldLabel codeLabel = new FieldLabel(code, "Code");
        codeLabel.setLabelWidth(125);

        FieldLabel codingSchemeLabel = new FieldLabel(codingScheme, "Coding Scheme");
        codingSchemeLabel.setLabelWidth(125);

        // setAllowBlanks(false, false, false);
        setEmptyTexts("ex: General Medicine", "ex: Outpatient", "ex: urn:uuid:vvvh6285-8b07-6s88-f36d-df545z654jgf");
        setToolTipConfigs(new ToolTipConfig("Display Name is a string", "it should contain less than 256 characters"), new ToolTipConfig(
                "Code is a string", "it should contain less than 256 characters"), new ToolTipConfig("Coding Scheme is a string",
                "it should contain less than 256 characters"));
        vcontainer.add(displayNameLabel, new VerticalLayoutData(1, -1));
        vcontainer.add(codeLabel, new VerticalLayoutData(1, -1));
        vcontainer.add(codingSchemeLabel, new VerticalLayoutData(1, -1));
    }

    public void initEditorDriver() {
        codedTermEditorDriver.initialize(this);
    }

    public void edit(CodedTerm codedTerm) {
        resetWidgets();
        setModel(codedTerm);
        codedTermEditorDriver.edit(codedTerm);
    }

    public void editNew() {
        model = new CodedTerm();
        edit(model);
        resetWidgets();
	}

    private void resetWidgets() {
        displayName.string.clear();
        code.string.clear();
        codingScheme.codingScheme.string.clear();
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
        codedTermEditorDriver.edit(model);
        // editNew();
    }

    public CodedTerm save() {
        model = codedTermEditorDriver.flush();
        if (codedTermEditorDriver.hasErrors()) {
            Dialog d = new Dialog();
            d.setHeadingText("Errors dialog");
            d.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
            d.setBodyStyleName("pad-text");
            d.add(new Label(codedTermEditorDriver.getErrors().toString()));
            d.getBody().addClassName("pad-text");
            d.setHideOnButtonClick(true);
            d.setWidth(300);
        }
        return model;
    }

    protected void setWidgetEnable(boolean enabled) {
        displayName.string.setEnabled(enabled);
        code.string.setEnabled(enabled);
        codingScheme.codingScheme.string.setEnabled(enabled);
    }

    /**
     * This method sets the default text to display in an empty field (defaults
     * to null). It is done to help and guide the user during his input.
     *
     * @param dispNameEmptytext Default text displayed in an empty name field
     * @param codeEmptyText     Default text displayed in an empty code field
     * @param codingEmptyText   Default text displayed in an empty coding scheme text field
     */
    public void setEmptyTexts(String dispNameEmptytext, String codeEmptyText, String codingEmptyText) {
        displayName.setEmptyText(dispNameEmptytext);
        code.setEmptyText(codeEmptyText);
        codingScheme.setEmptyText(codingEmptyText);
    }

	/**
     * Sets the widget's tool tip with the given config
     *
     * @param configDisplayName
     * @param configCode
     * @param configCodingScheme
     */
    public void setToolTipConfigs(ToolTipConfig configDisplayName, ToolTipConfig configCode, ToolTipConfig configCodingScheme) {
        displayName.setToolTipConfig(configDisplayName);
        code.setToolTipConfig(configCode);
        codingScheme.setToolTipConfig(configCodingScheme);
    }

    /**
     * Sets whether a field is valid when its value length = 0 (default to
     * true). This will warn the user through the editor widget if he didn't
     * input anything in field which does not allow blank.
     *
     * @param b_displayName
     *            true to allow blank to the name field, false otherwise
     * @param b_code
     *            true to allow blank to the code field, false otherwise
     * @param b_codingScheme
     *            true to allow blank to the coding scheme field, false
     *            otherwise
     */
    public void setAllowBlanks(boolean b_displayName, boolean b_code, boolean b_codingScheme) {
        displayName.setAllowBlank(b_displayName);
        code.setAllowBlank(b_code);
        codingScheme.setAllowBlank(b_codingScheme);
    }

    public EditionMode getEditionMode() {
        return editionMode;
    }

    public void setEditionMode(EditionMode editionMode) {
        this.editionMode = editionMode;
        if (editionMode.equals(EditionMode.DISPLAY) || editionMode.equals(EditionMode.NODATA)) {
            setWidgetEnable(false);
        } else if (editionMode.equals(EditionMode.NEW) || editionMode.equals(EditionMode.EDIT)) {
            setWidgetEnable(true);
        }
    }

    public CodedTerm getModel() {
        return save();
    }

    public void setModel(CodedTerm codedTerm) {
        this.model = codedTerm;
    }

    interface CodedTermEditorDriver extends SimpleBeanEditorDriver<CodedTerm, CodedTermEditorWidget> {
    }

}
