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
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SpinnerField;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

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

	String256EditorWidget name = new String256EditorWidget();

	ListStoreEditor<Integer> values;

	@Ignore
	ListView<Integer, String> listView;

	@Ignore
	SpinnerField<Integer> value = new SpinnerField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
	@Ignore
	TextButton addValueButton = new TextButton("Add");
	@Ignore
	TextButton deleteValueButton = new TextButton("Delete entry");

	public NameValueIntegerEditorWidget() {

		// init list tools
		listView = new ListView<Integer, String>(new ListStore<Integer>(new IntegerKeyProvider()), new IntegerValueProvider());
		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		values = new ListStoreEditor<Integer>(listView.getStore());

		// /////////////////////
		// --BuildUI
		// /////////////////////
		VerticalLayoutContainer vcontainer = new VerticalLayoutContainer();

		HBoxLayoutContainer hcontainer = new HBoxLayoutContainer();

		FieldLabel nameLabel = new FieldLabel(name, "Name");
		nameLabel.setLabelWidth(125);
		name.string.disable();

		// Wiget field+listview
		FieldLabel valueLabel = new FieldLabel(hcontainer, "Integer Value");
		valueLabel.setLabelWidth(125);
		value.setWidth("auto");
		value.setMinValue(1);
		value.setIncrement(5);

		hcontainer.add(value, new BoxLayoutData(new Margins(0, 15, 0, 0)));
		value.setWidth("auto");
		hcontainer.add(addValueButton);

		hcontainer.setWidth("auto");

		deleteValueButton.disable();

		// Add each widget to the main container
		vcontainer.add(nameLabel, new VerticalLayoutData(1, -1));
		vcontainer.add(valueLabel, new VerticalLayoutData(1, -1));
		vcontainer.add(listView, new VerticalLayoutData(1, 75));
		vcontainer.add(deleteValueButton);

		// init namevalueInteger with its widgets container
		initWidget(vcontainer);

		bindUI();
	}

	private void bindUI() {
		listView.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Integer>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<Integer> event) {
				if (listView.getSelectionModel().getSelectedItem() != null) {
					deleteValueButton.enable();
				} else {
					deleteValueButton.disable();
				}
			}
		});

		// Add value handler
		addValueButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				addValueToListStore();
			}
		});
		value.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					logger.info("ENTER KEY PRESSED");
					addValueToListStore();
				}
			}
		});

		// Delete Value handler
		deleteValueButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				final ConfirmDeleteDialog cdd = new ConfirmDeleteDialog(listView.getSelectionModel().getSelectedItem().toString());
				cdd.show();
				cdd.addHideHandler(new HideHandler() {

					@Override
					public void onHide(HideEvent event) {
						if (cdd.getHideButton() == cdd.getButtonById(PredefinedButton.YES.name())) {
							// perform YES action
							values.getStore().remove(listView.getSelectionModel().getSelectedItem());
						}
					}
				});
			}

		});
	}

	private void addValueToListStore() {
		String s = value.getText();
		if (s != null && !s.isEmpty()) {
			logger.info("adding new value (" + value.getText() + ") to list store");
			Integer v = Integer.parseInt(s);
			values.getStore().add(v);
			value.clear();
		}
		value.focus();
	}

	/**
	 * This method sets the default text to display in an empty field (defaults
	 * to null). It is done to help and guide the user during its input.
	 * 
	 * @param nameEmptyText
	 *            Default text displayed in an empty name field
	 * @param valueTypeEmptyText
	 *            Default text displayed in an empty value field
	 */
	public void setEmptyTexts(String nameEmptyText, String valueTypeEmptyText) {
		name.setEmptyText(nameEmptyText);
		value.setEmptyText(valueTypeEmptyText);
	}

	/**
	 * Sets the widget's tool tip with the given config
	 * 
	 * @param nameConfig
	 * @param valueConfig
	 */
	public void setToolTipConfigs(ToolTipConfig nameConfig, ToolTipConfig valueConfig) {
		name.setToolTipConfig(nameConfig);
		value.setToolTipConfig(valueConfig);
	}

	/**
	 * Sets whether a field is valid when its value length = 0 (default to
	 * true). This will warn the user through the editor widget if he didn't
	 * input anything in field which does not allow blank.
	 * 
	 * @param nameAllowsBlank
	 *            true to allow blank to the name field, false otherwise
	 * @param valueAllowsBlank
	 *            true to allow blank to the value field, false otherwise
	 * 
	 */
	public void setAllowBlanks(boolean nameAllowsBlank, boolean valueAllowsBlank) {
		name.setAllowBlank(nameAllowsBlank);
		value.setAllowBlank(valueAllowsBlank);
	}
}
