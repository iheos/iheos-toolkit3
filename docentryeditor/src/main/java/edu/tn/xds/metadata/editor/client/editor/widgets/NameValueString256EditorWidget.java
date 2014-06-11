package edu.tn.xds.metadata.editor.client.editor.widgets;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
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
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

import com.sun.org.glassfish.gmbal.NameValue;
import edu.tn.xds.metadata.editor.client.editor.properties.String256Properties;
import edu.tn.xds.metadata.editor.client.generics.GenericEditableListView;
import edu.tn.xds.metadata.editor.shared.model.NameValueString256;
import edu.tn.xds.metadata.editor.shared.model.String256;

/**
 * <p>
 * <b>This class represents the widget which matches NameValueString256 model
 * type</b>
 * </p>
 */
public class NameValueString256EditorWidget extends Composite implements Editor<NameValueString256> {
	private static Logger logger = Logger.getLogger(NameValueString256EditorWidget.class.getName());

//	String256EditorWidget name = new String256EditorWidget();
//
//	ListStoreEditor<String256> values;
//	@Ignore
//	ListView<String256, String> listView;
//
//	@Ignore
//	String256EditorWidget value = new String256EditorWidget();
//	@Ignore
//	TextButton addValueButton = new TextButton("Add");
//	@Ignore
//	TextButton deleteValueButton = new TextButton("Delete entry");
    ListStoreEditor<String256> values;
    @Ignore
    GenericEditableListView listView;

	public NameValueString256EditorWidget(String widgetTitle) {

		final String256Properties props = GWT.create(String256Properties.class);

        listView=new GenericEditableListView(String256.class,widgetTitle,new ListStore<String256>(props.key()),props.string());

        TextField tf=new TextField();
        tf.setAllowBlank(false);
        tf.setToolTip("This value is required and must unique. (ex: 58642j65s^^^5.8.4)");
        tf.setEmptyText("ex: 58642j65s^^^5.8.4");
        listView.addEditorConfig(tf);
        values=new ListStoreEditor<String256>(listView.getStore());
		// init list tools
//		listView = new ListView<String256, String>(new ListStore<String256>(props.key()), props.string());
//		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//
//		values = new ListStoreEditor<String256>(listView.getStore());
//
//		// /////////////////////
//		// --BuildUI
//		// /////////////////////
//		VerticalLayoutContainer vcontainer = new VerticalLayoutContainer();
//
//		HBoxLayoutContainer hcontainer = new HBoxLayoutContainer();
//
//		FieldLabel nameLabel = new FieldLabel(name, "Name");
//		nameLabel.setLabelWidth(125);
//		name.string.disable();
//
//		// Wiget field+listview
//		FieldLabel valueLabel = new FieldLabel(hcontainer, "Values");
//		valueLabel.setLabelWidth(125);
//		value.setWidth("auto");
//
//		hcontainer.add(value, new BoxLayoutData(new Margins(0, 15, 0, 0)));
//		value.setWidth("auto");
//		hcontainer.add(addValueButton);
//
//		hcontainer.setWidth("auto");
//
//		deleteValueButton.disable();
//
//		// Add each widget to the main container
//		vcontainer.add(nameLabel, new VerticalLayoutData(1, -1));
//		vcontainer.add(valueLabel, new VerticalLayoutData(1, -1));
//		vcontainer.add(listView, new VerticalLayoutData(1, 150));
//		vcontainer.add(deleteValueButton);

		// init namevaluestring256 with its widgets container
		initWidget(listView.asWidget());

		bindUI();
	}

	private void bindUI() {
//		listView.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<String256>() {
//			@Override
//			public void onSelectionChanged(SelectionChangedEvent<String256> event) {
//				if (listView.getSelectionModel().getSelectedItem() != null) {
//					deleteValueButton.enable();
//				} else {
//					deleteValueButton.disable();
//				}
//			}
//		});
//
//		// Add value handler
//		addValueButton.addSelectHandler(new SelectHandler() {
//
//			@Override
//			public void onSelect(SelectEvent event) {
//				addValueToListStore();
//			}
//		});
//		value.string.addKeyDownHandler(new KeyDownHandler() {
//
//			@Override
//			public void onKeyDown(KeyDownEvent event) {
//				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//					logger.info("ENTER KEY PRESSED");
//					addValueToListStore();
//				}
//			}
//		});
//
//		// Delete Value handler
//		deleteValueButton.addSelectHandler(new SelectHandler() {
//
//			@Override
//			public void onSelect(SelectEvent event) {
//				final ConfirmDeleteDialog cdd = new ConfirmDeleteDialog(listView.getSelectionModel().getSelectedItem().getString());
//				cdd.show();
//				cdd.addHideHandler(new HideHandler() {
//
//					@Override
//					public void onHide(HideEvent event) {
//						if (cdd.getHideButton() == cdd.getButtonById(PredefinedButton.YES.name())) {
//							// perform YES action
//							values.getStore().remove(listView.getSelectionModel().getSelectedItem());
//						}
//					}
//				});
//			}
//
//		});
	}

//	private void addValueToListStore() {
//		String s = value.string.getText();
//		if (s != null && !s.isEmpty()) {
//			logger.info("adding new value (" + value.string.getText() + ") to list store");
//			String256 v = new String256().setString(s);
//			if (!contains(v)) {
//				values.getStore().add(new String256().setString(s));
//				// value.string.setText("");
//				// value.string.setValue("");
//				value.string.clear();
//			} else {
//				Info.display("Impossible to add value", "It is impossible to add this value. It already is in the list.");
//			}
//		}
//		value.string.focus();
//	}

//	private boolean contains(String256 str) {
//		for (String256 s : values.getStore().getAll()) {
//			if (s.getString().equals(str.getString())) {
//				return true;
//			}
//		}
//		return false;
//	}

	/**
	 * This method sets the default text to display in an empty field (defaults
	 * to null). It is done to help and guide the user during his input.
	 *
	 * @param nameEmptyText
	 *            Default text displayed in an empty name field
	 * @param valueTypeEmptyText
	 *            Default text displayed in an empty value field
	 */
//	public void setEmptyTexts(String nameEmptyText, String valueTypeEmptyText) {
//		name.setEmptyText(nameEmptyText);
//		value.setEmptyText(valueTypeEmptyText);
//	}

	/**
	 * Sets the widget's tool tip with the given config
	 *
	 * @param nameConfig
	 * @param valueConfig
	 */
//	public void setToolTipConfigs(ToolTipConfig nameConfig, ToolTipConfig valueConfig) {
//		name.setToolTipConfig(nameConfig);
//		value.setToolTipConfig(valueConfig);
//	}

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
//	public void setAllowBlanks(boolean nameAllowsBlank, boolean valueAllowsBlank) {
//		name.setAllowBlank(nameAllowsBlank);
//		value.setAllowBlank(valueAllowsBlank);
//	}
}
