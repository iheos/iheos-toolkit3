package edu.tn.xds.metadata.editor.client.editor.widgets;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
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

import edu.tn.xds.metadata.editor.client.editor.EditionMode;
import edu.tn.xds.metadata.editor.client.editor.properties.String256Properties;
import edu.tn.xds.metadata.editor.shared.model.Author;
import edu.tn.xds.metadata.editor.shared.model.String256;

/**
 * <p>
 * <b>This class represents the widget which can edit the Author model type</b>
 * It does an automatic mapping between the Author class's attributes name and
 * the widget's fields name.
 * </p>
 *
 * @see Author
 * @see AuthorEditorDriver
 * @see EditionMode
 */
public class AuthorEditorWidget extends Composite implements Editor<Author> {
	private static Logger logger = Logger.getLogger(AuthorEditorWidget.class.getName());

	/**
	 * The AuthorEditorDriver is the editr driver interface which handle the
	 * edition of an Author through AuthorEditorWidget.
	 *
	 * @see Author
	 * @see AuthorEditorWidget
	 *
	 */
	interface AuthorEditorDriver extends SimpleBeanEditorDriver<Author, AuthorEditorWidget> {
	}

	// instance of the driver for the edition of an author (used for the edition
	// the selected author in authors list
	private final AuthorEditorDriver authorEditorDriver = GWT.create(AuthorEditorDriver.class);

	@Ignore
	private Author model;

	/*
	 * editionMode is an instance of EditionMode. It is used to know the state
	 * of the editor, to know how to display the different fields
	 * (enable/disable especially)
	 */
	@Ignore
	private EditionMode editionMode = EditionMode.DISPLAY;

	/*
	 * field to input the authorPerson attribute it is directly mapped by its
	 * name on "authorPerson" in Author's class.
	 */
	String256EditorWidget authorPerson = new String256EditorWidget();

	// /////////////////////////////////
	// --- List of institutions
	// /////////////////////////////////
	/*
	 * List store to handle the editor on "List<String> authorInstitutions" in
	 * Author's class. It is used in a listView widget and handle its content.
	 */
	ListStoreEditor<String256> authorInstitutions;
	@Ignore
	ListView<String256, String> listViewAuthInstitutions;

	@Ignore
	String256EditorWidget authorInstitution = new String256EditorWidget();
	@Ignore
	TextButton addInstitutionButton = new TextButton("Add");
	@Ignore
	TextButton deleteInstitutionButton = new TextButton("Delete entry");

	// /////////////////////////////////
	// --- List of Roles
	// /////////////////////////////////
	ListStoreEditor<String256> authorRoles;
	@Ignore
	ListView<String256, String> listViewAuthRoles;

	@Ignore
	String256EditorWidget authorRole = new String256EditorWidget();
	@Ignore
	TextButton addRoleButton = new TextButton("Add");
	@Ignore
	TextButton deleteRoleButton = new TextButton("Delete entry");

	// /////////////////////////////////
	// --- List of Specialties
	// /////////////////////////////////
	ListStoreEditor<String256> authorSpecialities;
	@Ignore
	ListView<String256, String> listViewAuthSpecialties;

	@Ignore
	String256EditorWidget authorSpeciality = new String256EditorWidget();
	@Ignore
	TextButton addSpecialtyButton = new TextButton("Add");
	@Ignore
	TextButton deleteSpecialtyButton = new TextButton("Delete entry");

	public AuthorEditorWidget() {
		final String256Properties props = GWT.create(String256Properties.class);

		// Author Institutions init listWidget
		listViewAuthInstitutions = new ListView<String256, String>(new ListStore<String256>(props.key()), props.string());
		listViewAuthInstitutions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		// init institutions store
		authorInstitutions = new ListStoreEditor<String256>(listViewAuthInstitutions.getStore());

		// Author Roles init listWidget
		listViewAuthRoles = new ListView<String256, String>(new ListStore<String256>(props.key()), props.string());
		listViewAuthRoles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		// init roles store
		authorRoles = new ListStoreEditor<String256>(listViewAuthRoles.getStore());

		// Author Specialties init listWidget
		listViewAuthSpecialties = new ListView<String256, String>(new ListStore<String256>(props.key()), props.string());
		listViewAuthSpecialties.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		// init specialties store
		authorSpecialities = new ListStoreEditor<String256>(listViewAuthSpecialties.getStore());

		// /////////////////////
		// --BuildUI
		// /////////////////////
		VerticalLayoutContainer vcontainer = new VerticalLayoutContainer();
		HorizontalLayoutContainer listsContainer = new HorizontalLayoutContainer();
		VerticalLayoutContainer authorInstitutionsContainer = new VerticalLayoutContainer();
		VerticalLayoutContainer authorRolesContainer = new VerticalLayoutContainer();
		VerticalLayoutContainer authorSpecialtiesContainer = new VerticalLayoutContainer();

		FieldLabel authorPersonLabel = new FieldLabel(authorPerson, "Author Person");
		authorPersonLabel.setLabelWidth(125);

		// ///////////////////////////////////////////////////
		// --- Author Institutions Widget field+listview
		// ///////////////////////////////////////////////////
		HorizontalLayoutContainer hcontainerInstitution = new HorizontalLayoutContainer();

		FieldLabel institutionLabel = new FieldLabel();
		institutionLabel.setText("Institution");
		// institutionLabel.setLabelWidth(125);
		authorInstitution.setWidth("auto");

		hcontainerInstitution.add(authorInstitution, new HorizontalLayoutData(1, 30, new Margins(0, 10, 10, 0)));
		authorInstitution.setWidth("auto");
		authorInstitution.setWidth("auto");
		hcontainerInstitution.add(addInstitutionButton);
		hcontainerInstitution.setWidth("auto");

		deleteInstitutionButton.disable();

		authorInstitutionsContainer.add(institutionLabel, new VerticalLayoutData(1, -1));
		authorInstitutionsContainer.add(hcontainerInstitution, new VerticalLayoutData(1, 30));
		authorInstitutionsContainer.add(listViewAuthInstitutions, new VerticalLayoutData(1, 150));
		authorInstitutionsContainer.add(deleteInstitutionButton);

		// ///////////////////////////////////////////////////
		// --- Author Roles Widget field+listview
		// ///////////////////////////////////////////////////
		HorizontalLayoutContainer hcontainerRole = new HorizontalLayoutContainer();

		FieldLabel roleLabel = new FieldLabel();
		roleLabel.setText("Role");
		// roleLabel.setLabelWidth(125);
		authorRole.setWidth("auto");

		hcontainerRole.add(authorRole, new HorizontalLayoutData(1, 30, new Margins(0, 10, 10, 0)));
		authorRole.setWidth("auto");
		addRoleButton.setWidth("auto");
		hcontainerRole.add(addRoleButton);
		hcontainerRole.setWidth("auto");

		deleteRoleButton.disable();

		authorRolesContainer.add(roleLabel, new VerticalLayoutData(1, -1));
		authorRolesContainer.add(hcontainerRole, new VerticalLayoutData(1, 30));
		authorRolesContainer.add(listViewAuthRoles, new VerticalLayoutData(1, 150));
		authorRolesContainer.add(deleteRoleButton);

		// ///////////////////////////////////////////////////
		// --- Author Specialties Widget field+listview
		// ///////////////////////////////////////////////////
		HorizontalLayoutContainer hcontainerSpecialty = new HorizontalLayoutContainer();

		FieldLabel specialtyLabel = new FieldLabel();
		// new FieldLabel(hcontainerSpecialty, "Specialty");
		specialtyLabel.setText("Specialty");
		// specialtyLabel.setLabelWidth(125);
		authorSpeciality.setWidth("auto");

		hcontainerSpecialty.add(authorSpeciality, new HorizontalLayoutData(1, 30, new Margins(0, 10, 10, 0)));
		authorSpeciality.setWidth("auto");
		addSpecialtyButton.setWidth("auto");
		hcontainerSpecialty.add(addSpecialtyButton);
		hcontainerSpecialty.setWidth("auto");

		deleteSpecialtyButton.disable();

		authorSpecialtiesContainer.add(specialtyLabel, new VerticalLayoutData(1, -1));
		authorSpecialtiesContainer.add(hcontainerSpecialty, new VerticalLayoutData(1, 30));
		authorSpecialtiesContainer.add(listViewAuthSpecialties, new VerticalLayoutData(1, 150));
		authorSpecialtiesContainer.add(deleteSpecialtyButton);

		// Add each widget to the main container
		vcontainer.add(authorPersonLabel, new VerticalLayoutData(1, -1));
		listsContainer.add(authorInstitutionsContainer, new HorizontalLayoutData(0.33, -1, new Margins(0, 10, 0, 0)));
		listsContainer.add(authorRolesContainer, new HorizontalLayoutData(0.33, -1, new Margins(0, 10, 0, 0)));
		listsContainer.add(authorSpecialtiesContainer, new HorizontalLayoutData(0.34, -1, new Margins()));
		vcontainer.add(listsContainer, new VerticalLayoutData(1, 230));

		// init namevaluestring256 with its widgets container
		initWidget(vcontainer);

		bindUI();
	}

	private void bindUI() {
		// /////////////////////////////
		// --- List selection binding
		// /////////////////////////////
		// institution selection handler
		listViewAuthInstitutions.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<String256>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<String256> event) {
				if (listViewAuthInstitutions.getSelectionModel().getSelectedItem() != null) {
					deleteInstitutionButton.enable();
				} else {
					deleteInstitutionButton.disable();
				}
			}
		});
		// role selection handler
		listViewAuthRoles.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<String256>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<String256> event) {
				if (listViewAuthRoles.getSelectionModel().getSelectedItem() != null) {
					deleteRoleButton.enable();
				} else {
					deleteRoleButton.disable();
				}
			}
		});
		// specialty selection handler
		listViewAuthSpecialties.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<String256>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<String256> event) {
				if (listViewAuthSpecialties.getSelectionModel().getSelectedItem() != null) {
					deleteSpecialtyButton.enable();
				} else {
					deleteSpecialtyButton.disable();
				}
			}
		});

		// ////////////////////////////////////////////////////
		// --- Add and delete value from lists handlers
		// ////////////////////////////////////////////////////

		// Add Institution value handlers
		addInstitutionButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				addAuthorInstitutionToListStore();
			}
		});
		authorInstitution.string.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					logger.info("ENTER KEY PRESSED");
					addAuthorInstitutionToListStore();
				}
			}
		});
		// Delete Institution Value handler
		deleteInstitutionButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				final ConfirmDeleteDialog cdd = new ConfirmDeleteDialog(listViewAuthInstitutions.getSelectionModel().getSelectedItem().getString());
				cdd.show();
				cdd.addHideHandler(new HideHandler() {

					@Override
					public void onHide(HideEvent event) {
						if (cdd.getHideButton() == cdd.getButtonById(PredefinedButton.YES.name())) {
							// perform YES action
							authorInstitutions.getStore().remove(listViewAuthInstitutions.getSelectionModel().getSelectedItem());
						}
					}
				});
			}

		});

		// Add Role value handlers
		addRoleButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				addAuthorRoleToListStore();
			}
		});
		authorRole.string.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					logger.info("ENTER KEY PRESSED");
					addAuthorRoleToListStore();
				}
			}
		});
		// Delete Role Value handler
		deleteRoleButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				final ConfirmDeleteDialog cdd = new ConfirmDeleteDialog(listViewAuthRoles.getSelectionModel().getSelectedItem().getString());
				cdd.show();
				cdd.addHideHandler(new HideHandler() {

					@Override
					public void onHide(HideEvent event) {
						if (cdd.getHideButton() == cdd.getButtonById(PredefinedButton.YES.name())) {
							// perform YES action
							authorRoles.getStore().remove(listViewAuthRoles.getSelectionModel().getSelectedItem());
						}
					}
				});
			}

		});

		// Add Specialty value handlers
		addSpecialtyButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				addAuthorSpecialtyToListStore();
			}
		});
		authorSpeciality.string.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					logger.info("ENTER KEY PRESSED");
					addAuthorSpecialtyToListStore();
				}
			}
		});
		// Delete Institution Value handler
		deleteSpecialtyButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				final ConfirmDeleteDialog cdd = new ConfirmDeleteDialog(listViewAuthSpecialties.getSelectionModel().getSelectedItem().getString());
				cdd.show();
				cdd.addHideHandler(new HideHandler() {

					@Override
					public void onHide(HideEvent event) {
						if (cdd.getHideButton() == cdd.getButtonById(PredefinedButton.YES.name())) {
							// perform YES action
							authorSpecialities.getStore().remove(listViewAuthSpecialties.getSelectionModel().getSelectedItem());
						}
					}
				});
			}

		});
	}

	/**
	 * Method to change the editor layout regarding an edition Mode. It actually
	 * enable/disable the various widgets of the editor regarding the state of
	 * edition.
	 *
	 * @see EditionMode
	 *
	 * @param editionMode
	 *            EditionMode (NODATA,DISPLAY,NEW,EDIT)
	 */
	public void setEditionMode(EditionMode editionMode) {
		this.editionMode = editionMode;
		if (editionMode.equals(EditionMode.DISPLAY) || editionMode.equals(EditionMode.NODATA)) {
			setWidgetEnable(false);
		} else if (editionMode.equals(EditionMode.NEW) || editionMode.equals(EditionMode.EDIT)) {
			setWidgetEnable(true);
		}
	}

	/**
	 * This method init the author editor driver. It must be called when the
	 * view is created.
	 */
	public void initEditorDriver() {
		authorEditorDriver.initialize(this);
	}

	/**
	 * This method handle the edition of a given author. It fill the editor with
	 * the author's details and put it in edition.
	 *
	 * @param author
	 *            The Author which will be edited.
	 */
	public void edit(Author author) {
		resetWidgets();
		setModel(author);
		authorEditorDriver.edit(author);
	}

	/**
	 * Method to start the edition of a new Author.
	 */
	public void editNew() {
		model = new Author();
		edit(model);
		resetWidgets();
	}

	private void resetWidgets() {
		authorInstitution.string.clear();
		authorRole.string.clear();
		authorSpeciality.string.clear();
	}

	/**
	 * Method that saves the changes in the Editor GUI in editor model.
	 */
	public void save() {
		model = authorEditorDriver.flush();
		if (authorEditorDriver.hasErrors()) {
			Dialog d = new Dialog();
			d.setHeadingText("Errors dialog");
			d.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
			d.setBodyStyleName("pad-text");
			d.add(new Label(authorEditorDriver.getErrors().toString()));
			d.getBody().addClassName("pad-text");
			d.setHideOnButtonClick(true);
			d.setWidth(300);
		}
	}

	private void setWidgetEnable(boolean enabled) {
		authorPerson.string.setEnabled(enabled);
		authorInstitution.string.setEnabled(enabled);
		listViewAuthInstitutions.setEnabled(enabled);
		authorRole.string.setEnabled(enabled);
		listViewAuthRoles.setEnabled(enabled);
		authorSpeciality.string.setEnabled(enabled);
		listViewAuthSpecialties.setEnabled(enabled);
		addInstitutionButton.setEnabled(enabled);
		addRoleButton.setEnabled(enabled);
		addSpecialtyButton.setEnabled(enabled);
		deleteInstitutionButton.setEnabled(enabled);
		deleteRoleButton.setEnabled(enabled);
		deleteSpecialtyButton.setEnabled(enabled);
	}

	private void addAuthorInstitutionToListStore() {
		String s = authorInstitution.string.getText();
		if (s != null && !s.isEmpty()) {
			logger.info("adding new value (" + s + ") to list store");
			String256 v = new String256().setString(s);
			if (!contains(authorInstitutions.getStore(), v)) {
				authorInstitutions.getStore().add(new String256().setString(s));
				authorInstitution.string.clear();
			} else {
				Info.display("Impossible to add value", "It is impossible to add this value. It already is in the list.");
			}
		}
		authorInstitution.string.focus();
	}

	private void addAuthorRoleToListStore() {
		String s = authorRole.string.getText();
		if (s != null && !s.isEmpty()) {
			logger.info("adding new value (" + s + ") to list store");
			String256 v = new String256().setString(s);
			if (!contains(authorRoles.getStore(), v)) {
				authorRoles.getStore().add(new String256().setString(s));
				authorRole.string.clear();
			} else {
				Info.display("Impossible to add value", "It is impossible to add this value. It already is in the list.");
			}
		}
		authorRole.string.focus();
	}

	private void addAuthorSpecialtyToListStore() {
		String s = authorSpeciality.string.getText();
		if (s != null && !s.isEmpty()) {
			logger.info("adding new value (" + s + ") to list store");
			String256 v = new String256().setString(s);
			if (!contains(authorSpecialities.getStore(), v)) {
				authorSpecialities.getStore().add(new String256().setString(s));
				authorSpeciality.string.clear();
			} else {
				Info.display("Impossible to add value", "It is impossible to add this value. It already is in the list.");
			}
		}
		authorSpeciality.string.focus();
	}

	private boolean contains(ListStore<String256> l, String256 str) {
		for (String256 s : l.getAll()) {
			if (s.getString().equals(str.getString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to get the edited author. Should be called after save().
	 *
	 * @return edited Author
	 */
	public Author getModel() {
		return model;
	}

	private void setModel(Author model) {
		this.model = model;
	}

	/**
	 * @return Current editionMode (state of the editor)
	 * @see EditionMode
	 */
	public EditionMode getEditionMode() {
		return editionMode;
	}

}
