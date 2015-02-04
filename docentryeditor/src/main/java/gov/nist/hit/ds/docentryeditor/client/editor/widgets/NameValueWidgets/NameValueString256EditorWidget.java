package gov.nist.hit.ds.docentryeditor.client.editor.widgets.NameValueWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.Validator;
import gov.nist.hit.ds.docentryeditor.client.editor.properties.String256Properties;
import gov.nist.hit.ds.docentryeditor.client.generics.GenericEditableListView;
import gov.nist.hit.ds.docentryeditor.client.generics.GridModelFactory;
import gov.nist.hit.ds.docentryeditor.client.widgets.BoundedTextField;
import gov.nist.hit.ds.docentryeditor.shared.model.NameValueString256;
import gov.nist.hit.ds.docentryeditor.shared.model.String256;

/**
 * <p>
 * <b>This class represents the widget which matches NameValueString256 model
 * type</b>
 * </p>
 */
public class NameValueString256EditorWidget extends GenericEditableListView<String256, String> implements Editor<NameValueString256> {
    private final static String256Properties props = GWT.create(String256Properties.class);

    ListStoreEditor<String256> values;
    @Ignore
    BoundedTextField tf;

    public NameValueString256EditorWidget(String widgetTitle) {
        super(widgetTitle, new ListStore<String256>(props.key()), props.string());
        values = new ListStoreEditor<String256>(getStore());
        bindUI();
    }

    public void addFieldValidator(Validator<String> validator) {
        tf.addValidator(validator);
    }

    private void bindUI() {
//        editing.addCompleteEditHandler(new CompleteEditEvent.CompleteEditHandler<String256>() {
//            @Override
//            public void onCompleteEdit(CompleteEditEvent<String256> event) {
//                tf.validate();
//                if (tf.getValue() == null || !tf.isValid()) {
//                    tf.clear();
//                    editing.startEditing(event.getEditCell());
////                    editing.cancelEditing();
////                    view.refresh(false);
//                }
//            }
//        });
    }

    /**
     * Sets the widget's tool tip with the given config
     *
     * @param toolTip
     */
    public void setEditingFieldToolTip(String toolTip) {
        tf.setToolTip(toolTip);
    }

    /**
     * This method sets the default text to display in an empty field (defaults
     * to null). It is done to help and guide the user during his input.
     *
     * @param emptyText Default text displayed in an empty value field when editing
     */
    public void setEmptyTexts(String emptyText) {
        tf.setEmptyText(emptyText);
    }

    public void setListMaxSize(int maxSize) {
        setStoreMaxLength(maxSize);
    }

    @Override
    protected void buildEditingFields() {
        tf = new BoundedTextField();
        tf.setAllowBlank(false);
        tf.setToolTip("This value is required and must unique.");
        tf.setEmptyText("ex: 58642j65s^^^5.8.4");
        addEditorConfig(tf);
    }

    @Override
    protected GridModelFactory<String256> getModelFactory() {
        return String256Factory.instance;
    }

    @Override
    protected ValueProvider<? super String256, String> getValueProvider() {
        return props.string();
    }


    //	/**
//	 * Sets whether a field is valid when its value length = 0 (default to
//	 * true). This will warn the user through the editor widget if he didn't
//	 * input anything in field which does not allow blank.
//	 *
//	 * @param nameAllowsBlank
//	 *            true to allow blank to the name field, false otherwise
//	 * @param valueAllowsBlank
//	 *            true to allow blank to the value field, false otherwise
//	 *
//	 */
//	public void setAllowBlanks(boolean nameAllowsBlank, boolean valueAllowsBlank) {
//		name.setAllowBlank(nameAllowsBlank);
//		value.setAllowBlank(valueAllowsBlank);
//	}


}
