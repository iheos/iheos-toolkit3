package edu.tn.xds.metadata.editor.client.editor.widgets;

import java.util.logging.Logger;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SpinnerField;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

import edu.tn.xds.metadata.editor.client.generics.GenericEditableListView;
import edu.tn.xds.metadata.editor.shared.model.NameValueInteger;

/**
 * <p>
 * <b>This class represents the widget which matches NameValueInteger model
 * type</b> <br>
 * </p>
 */
public class NameValueIntegerEditorWidget extends Composite implements Editor<NameValueInteger> {

	private static Logger logger = Logger.getLogger(NameValueIntegerEditorWidget.class.getName());

	// Custom KeyProvider to deal with Integer
	public class IntegerKeyProvider implements ModelKeyProvider<Integer> {

		@Override
		public String getKey(Integer item) {
			return item.toString();
		}

	}

	// Custom ValueProvider to deal with Integer
	public class IntegerValueProvider implements ValueProvider<Integer, String> {

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

	ListStoreEditor<Integer> values;
    @Ignore
    GenericEditableListView<Integer, String> listView;

	@Ignore
	SpinnerField<Integer> value = new SpinnerField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());

	public NameValueIntegerEditorWidget(String widgetTitle) {

		// init list tools
		listView = new GenericEditableListView<Integer, String>(Integer.class,widgetTitle,new ListStore<Integer>(new IntegerKeyProvider()), new IntegerValueProvider());

        value.setAllowBlank(false);
		value.setWidth("auto");
		value.setMinValue(1);
		value.setIncrement(5);

        Converter<String, Integer> converter = new Converter<String, Integer>(){
            @Override public String convertFieldValue(Integer object) {
                String value = "";
                if(object != null) {
                    value = object.toString();
                }
                return value;
            }
            @Override public Integer convertModelValue(String object) {
                Integer value = 0;
                if(object != null && object.trim().length() > 0) {
                    value = Integer.parseInt(object);
                }
                return value;
            }
        };

        listView.addEditorConfig(converter, (Field) value);

		values = new ListStoreEditor<Integer>(listView.getStore());


		initWidget(listView.asWidget());

	}

}
