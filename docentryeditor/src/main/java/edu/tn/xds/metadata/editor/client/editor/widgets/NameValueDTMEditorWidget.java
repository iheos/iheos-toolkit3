package edu.tn.xds.metadata.editor.client.editor.widgets;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
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
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

import edu.tn.xds.metadata.editor.client.editor.properties.DTMProperties;
import edu.tn.xds.metadata.editor.shared.model.DTM;
import edu.tn.xds.metadata.editor.shared.model.NameValueDTM;
import edu.tn.xds.metadata.editor.shared.model.String256;

/**
 * <p>
 * <b>This class represents the widget which matches NameValue model type</b> <br>
 * </p>
 */
public class NameValueDTMEditorWidget extends Composite implements Editor<NameValueDTM> {
	private static Logger logger = Logger.getLogger(NameValueDTMEditorWidget.class.getName());

	String256EditorWidget name = new String256EditorWidget();

	ListStoreEditor<DTM> values;
	@Ignore
	ListView<DTM, String> listView;

	@Ignore
	DTMEditorWidget value = new DTMEditorWidget();
	@Ignore
	TextButton addValueButton = new TextButton("Add");
	@Ignore
	TextButton deleteValueButton = new TextButton("Delete entry");

	public NameValueDTMEditorWidget() {

		final DTMProperties props = GWT.create(DTMProperties.class);

		// init list tools
		listView = new ListView<DTM, String>(new ListStore<DTM>(props.key()), props.dtm());
		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		values = new ListStoreEditor<DTM>(listView.getStore());

		// /////////////////////
		// --BuildUI
		// /////////////////////
		VerticalLayoutContainer vcontainer = new VerticalLayoutContainer();

		HBoxLayoutContainer hcontainer = new HBoxLayoutContainer();

		FieldLabel nameLabel = new FieldLabel(name, "Name");
		nameLabel.setLabelWidth(125);
		name.string.disable();

		// Wiget field+listview
		FieldLabel valueLabel = new FieldLabel(hcontainer, "DTM Value");
		valueLabel.setLabelWidth(125);
		value.setWidth("auto");

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

		// init namevalueDTM with its widgets container
		initWidget(vcontainer);

		bindUI();
	}

	private void bindUI() {
		listView.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<DTM>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<DTM> event) {
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
		value.dtm.string.addKeyDownHandler(new KeyDownHandler() {

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
				final ConfirmDeleteDialog cdd = new ConfirmDeleteDialog(listView.getSelectionModel().getSelectedItem().getDtm().getString());
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
		String s = value.dtm.string.getText();
		if (s != null && !s.isEmpty()) {
			logger.info("adding new value (" + value.dtm.string.getText() + ") to list store");
			DTM v = new DTM().setDtm(new String256().setString(s));
			if (!contains(v)) {
				values.getStore().add(v);
				// value.string.setText("");
				// value.string.setValue("");
				value.dtm.string.clear();
			} else {
				Info.display("Impossible to add value", "It is impossible to add this value. It already is in the list.");
			}
		}
		value.dtm.string.focus();
	}

	private boolean contains(DTM dtm) {
		for (DTM o : values.getStore().getAll()) {
			if (o.getString().equals(dtm.getString())) {
				return true;
			}
		}
		return false;
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
		value.dtm.setEmptyText(valueTypeEmptyText);
	}

	/**
	 * Sets the widget's tool tip with the given config
	 * 
	 * @param nameConfig
	 * @param valueConfig
	 */
	public void setToolTipConfigs(ToolTipConfig nameConfig, ToolTipConfig valueConfig) {
		name.setToolTipConfig(nameConfig);
		value.dtm.setToolTipConfig(valueConfig);
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
		value.dtm.setAllowBlank(valueAllowsBlank);
	}
}
