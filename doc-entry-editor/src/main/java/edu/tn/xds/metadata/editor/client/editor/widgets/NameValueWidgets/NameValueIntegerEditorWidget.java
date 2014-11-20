package edu.tn.xds.metadata.editor.client.editor.widgets.NameValueWidgets;


import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SpinnerField;
import edu.tn.xds.metadata.editor.client.generics.GenericEditableListView;
import edu.tn.xds.metadata.editor.client.generics.GridModelFactory;
import edu.tn.xds.metadata.editor.shared.model.NameValueInteger;

/**
 * <p>
 * <b>This class represents the widget which matches NameValueInteger model
 * type</b> <br>
 * </p>
 */
public class NameValueIntegerEditorWidget extends GenericEditableListView<Integer, String> implements Editor<NameValueInteger> {
    ListStoreEditor<Integer> values;

    @Ignore
    SpinnerField<Integer> value;

    public NameValueIntegerEditorWidget(String widgetTitle) {
        super(widgetTitle, new ListStore<Integer>(new IntegerKeyProvider()), new IntegerValueProvider());
        values = new ListStoreEditor<Integer>(getStore());
    }

    @Override
    protected void buildEditingFields() {
        value = new SpinnerField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
        value.setAllowBlank(false);
        value.setWidth("auto");
        value.setMinValue(1);
        value.setIncrement(5);

        Converter<String, Integer> converter = new Converter<String, Integer>() {
            @Override
            public String convertFieldValue(Integer object) {
                String value = "";
                if (object != null) {
                    value = object.toString();
                }
                return value;
            }

            @Override
            public Integer convertModelValue(String object) {
                Integer value = 0;
                if (object != null && object.trim().length() > 0) {
                    value = Integer.parseInt(object);
                }
                return value;
            }
        };

        addEditorConfig(converter, value);
    }

    @Override
    protected GridModelFactory<Integer> getModelFactory() {
        return IntegerFactory.instance;
    }

    public void setListMaxSize(int listMaxSize) {
        this.setStoreMaxLength(listMaxSize);
    }

    @Override
    protected ValueProvider<? super Integer, String> getValueProvider() {
        return new IntegerValueProvider();
    }

    // Custom KeyProvider to deal with Integer
    public static class IntegerKeyProvider implements ModelKeyProvider<Integer> {
        @Override
        public String getKey(Integer item) {
            return item.toString();
        }
    }

    // Custom ValueProvider to deal with Integer
    public static class IntegerValueProvider implements ValueProvider<Integer, String> {

        @Override
        public String getValue(Integer object) {
            return object.toString();
        }

        @Override
        public void setValue(Integer object, String value) {
            object = Integer.parseInt(value);
        }

        @Override
        public String getPath() {
            return null;
        }

    }
}
