package edu.tn.xds.metadata.editor.client.editor;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

import edu.tn.xds.metadata.editor.client.editor.properties.AuthorProperties;
import edu.tn.xds.metadata.editor.client.editor.properties.CodedTermProperties;
import edu.tn.xds.metadata.editor.client.editor.properties.InternationalStringProperties;
import edu.tn.xds.metadata.editor.client.editor.widgets.AuthorEditorWidget;
import edu.tn.xds.metadata.editor.client.editor.widgets.CodedTermEditorWidget;
import edu.tn.xds.metadata.editor.client.editor.widgets.ConfirmDeleteDialog;
import edu.tn.xds.metadata.editor.client.editor.widgets.IdentifierOIDEditorWidget;
import edu.tn.xds.metadata.editor.client.editor.widgets.IdentifierString256EditorWidget;
import edu.tn.xds.metadata.editor.client.editor.widgets.InternationalStringEditorWidget;
import edu.tn.xds.metadata.editor.client.editor.widgets.LanguageCodeComboBox;
import edu.tn.xds.metadata.editor.client.editor.widgets.NameValueDTMEditorWidget;
import edu.tn.xds.metadata.editor.client.editor.widgets.NameValueIntegerEditorWidget;
import edu.tn.xds.metadata.editor.client.editor.widgets.NameValueString256EditorWidget;
import edu.tn.xds.metadata.editor.client.editor.widgets.OIDEditorWidget;
import edu.tn.xds.metadata.editor.client.editor.widgets.String256EditorWidget;
import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractView;
import edu.tn.xds.metadata.editor.shared.model.Author;
import edu.tn.xds.metadata.editor.shared.model.CodedTerm;
import edu.tn.xds.metadata.editor.shared.model.DocumentModel;
import edu.tn.xds.metadata.editor.shared.model.InternationalString;
import edu.tn.xds.metadata.editor.shared.model.LanguageCode;

public class DocumentModelEditorView extends AbstractView<DocumentModelEditorPresenter> implements Editor<DocumentModel> {
	private final InternationalStringProperties isprops = GWT.create(InternationalStringProperties.class);
	private final AuthorProperties authorprops = GWT.create(AuthorProperties.class);
	private final CodedTermProperties codedTermProps = GWT.create(CodedTermProperties.class);

	private final VerticalLayoutContainer form = new VerticalLayoutContainer();

	VerticalLayoutContainer requiredFields = new VerticalLayoutContainer();
	VerticalLayoutContainer optionalFields = new VerticalLayoutContainer();

	/* simple fields declaration */
	String256EditorWidget id = new String256EditorWidget();
	String256EditorWidget hash = new String256EditorWidget();
	LanguageCodeComboBox languageCode = new LanguageCodeComboBox();
	String256EditorWidget mimeType = new String256EditorWidget();
	String256EditorWidget uri = new String256EditorWidget();
	OIDEditorWidget repoUId = new OIDEditorWidget(false);

	/* coded terms declaration */
	CodedTermEditorWidget classCode = new CodedTermEditorWidget();
	CodedTermEditorWidget formatCode = new CodedTermEditorWidget();
	CodedTermEditorWidget healthcareFacilityType = new CodedTermEditorWidget();
	CodedTermEditorWidget practiceSettingCode = new CodedTermEditorWidget();
	CodedTermEditorWidget typeCode = new CodedTermEditorWidget();

	/* Identifiers declaration */
	IdentifierOIDEditorWidget uniqueId = new IdentifierOIDEditorWidget();
	IdentifierString256EditorWidget patientID = new IdentifierString256EditorWidget();

	/* name values declaration */
	NameValueString256EditorWidget legalAuthenticator = new NameValueString256EditorWidget();
	NameValueString256EditorWidget sourcePatientId = new NameValueString256EditorWidget();
	NameValueDTMEditorWidget creationTime = new NameValueDTMEditorWidget();
	NameValueDTMEditorWidget serviceStartTime = new NameValueDTMEditorWidget();
	NameValueDTMEditorWidget serviceStopTime = new NameValueDTMEditorWidget();
	NameValueIntegerEditorWidget size = new NameValueIntegerEditorWidget();

	// ///////////////////////////////////////////////////////////////////////
	// ---- AUTHORS WIDGETS
	// ///////////////////////////////////////////////////////////////////////
	ListStoreEditor<Author> authors;
	@Ignore
	ListView<Author, String> listViewAuthors;
	@Ignore
	AuthorEditorWidget author = new AuthorEditorWidget();
	@Ignore
	TextButton newAuthorWidget = new TextButton("New entry");
	@Ignore
	TextButton editAuthorWidget = new TextButton("Edit entry");
	@Ignore
	TextButton saveAuthorWidget = new TextButton("Save entry");
	@Ignore
	TextButton cancelAuthorWidget = new TextButton("Cancel");
	@Ignore
	TextButton deleteAuthorWidget = new TextButton("Delete entry");

	// ///////////////////////////////////////////////////////////////////////
	// ---- Titles WIDGETS
	// ///////////////////////////////////////////////////////////////////////
	ListStoreEditor<InternationalString> titles;
	// @Ignore
	// ListView<InternationalString, String> listViewTitles;
	@Ignore
	Grid<InternationalString> gridTitles;
	@Ignore
	InternationalStringEditorWidget title = new InternationalStringEditorWidget();
	@Ignore
	TextButton newTitleWidget = new TextButton("New entry");
	@Ignore
	TextButton editTitleWidget = new TextButton("Edit entry");
	@Ignore
	TextButton saveTitleWidget = new TextButton("Save entry");
	@Ignore
	TextButton cancelTitleWidget = new TextButton("Cancel");
	@Ignore
	TextButton deleteTitleWidget = new TextButton("Delete entry");

	// ///////////////////////////////////////////////////////////////////////
	// ---- Comments WIDGETS
	// ///////////////////////////////////////////////////////////////////////
	ListStoreEditor<InternationalString> comments;
	// @Ignore
	// ListView<InternationalString, String> listViewComments;
	@Ignore
	Grid<InternationalString> gridComments;
	@Ignore
	InternationalStringEditorWidget comment = new InternationalStringEditorWidget();
	@Ignore
	TextButton newCommentWidget = new TextButton("New entry");
	@Ignore
	TextButton editCommentWidget = new TextButton("Edit entry");
	@Ignore
	TextButton saveCommentWidget = new TextButton("Save entry");
	@Ignore
	TextButton cancelCommentWidget = new TextButton("Cancel");
	@Ignore
	TextButton deleteCommentWidget = new TextButton("Delete entry");

	// ///////////////////////////////////////////////////////////////////////
	// ---- ConfidentialityCodes WIDGETS
	// ///////////////////////////////////////////////////////////////////////
	ListStoreEditor<CodedTerm> confidentialityCodes;
	@Ignore
	Grid<CodedTerm> gridConfidentialityCode;
	// @Ignore
	// ListView<CodedTerm, String> listViewConfidentialityCode;
	@Ignore
	CodedTermEditorWidget confidentialityCode = new CodedTermEditorWidget();
	@Ignore
	TextButton newConfidentialityCodeWidget = new TextButton("New entry");
	@Ignore
	TextButton editConfidentialityCodeWidget = new TextButton("Edit entry");
	@Ignore
	TextButton saveConfidentialityCodeWidget = new TextButton("Save entry");
	@Ignore
	TextButton cancelConfidentialityCodeWidget = new TextButton("Cancel");
	@Ignore
	TextButton deleteConfidentialityCodeWidget = new TextButton("Delete entry");

	// test button
	// @Ignore
	// TextButton btnSave = new TextButton("Save File");

	@Override
	protected Widget buildUI() {
		HorizontalLayoutContainer container = new HorizontalLayoutContainer();
		container.getElement().setMargins(10);
		container.setBorders(false);

		FramedPanel fp1 = new FramedPanel();
		FramedPanel fp2 = new FramedPanel();

		fp1.add(requiredFields);
		fp2.add(optionalFields);
		fp1.setHeadingText("Required Fields");
		fp2.setHeadingText("Optional Fields");

		// Adding required and optional fields panels to the main container of
		// editor view
		container.add(fp1, new HorizontalLayoutData(0.5, -1, new Margins(10, 10, 10, 10)));
		container.add(fp2, new HorizontalLayoutData(0.5, -1, new Margins(10, 10, 10, 10)));

		// //////////////////////////////////////
		// Simple fields label and options (init)
		// /////////////////////////////////////
		// ID Field (required)
		FieldLabel idLabel = new FieldLabel(id, "ID");
		idLabel.setLabelWidth(125);
		id.setAllowBlank(false);

		// Hash Field (required)
		FieldLabel hashLabel = new FieldLabel(hash, "Hash");
		hashLabel.setLabelWidth(125);
		hash.setAllowBlank(true);

		// Language Code Field (required)
		FieldLabel languageCodeLabel = new FieldLabel(languageCode, "Language Code");
		languageCodeLabel.setLabelWidth(125);
		languageCode.setAllowBlank(false);

		// Mime Type Field (required)
		FieldLabel mimeTypeLabel = new FieldLabel(mimeType, "Mime Type");
		mimeTypeLabel.setLabelWidth(125);
		mimeType.setAllowBlank(false);

		// Repository Unique ID Field (optional)
		FieldLabel repositoryLabel = new FieldLabel(repoUId, "Repository Unique ID");
		repositoryLabel.setLabelWidth(125);
		repoUId.setAllowBlank(true);

		// URI Field (optional)
		FieldLabel uriLabel = new FieldLabel(uri, "URI");
		uriLabel.setLabelWidth(125);
		uri.setAllowBlank(true);

		// ////////////////////////////////////////////////////
		// --- Adding REQUIRED simple fields labels to containers
		// ////////////////////////////////////////////////////
		VerticalLayoutContainer simpleRequiredFieldsContainer = new VerticalLayoutContainer();
		simpleRequiredFieldsContainer.add(idLabel, new VerticalLayoutData(1, -1));
		simpleRequiredFieldsContainer.add(languageCodeLabel, new VerticalLayoutData(1, -1));
		simpleRequiredFieldsContainer.add(mimeTypeLabel, new VerticalLayoutData(1, -1));

		/* REQUIRED container added to a fieldset */
		FieldSet fieldSet_general_fields_required = new FieldSet();
		fieldSet_general_fields_required.setHeadingText("General required");
		fieldSet_general_fields_required.setCollapsible(true);
		fieldSet_general_fields_required.add(simpleRequiredFieldsContainer);

		// //////////////////////////////////////////////////////////
		// --- Adding OPTIONAL simple fields labels to containers
		// //////////////////////////////////////////////////////////
		VerticalLayoutContainer simpleOptionalFieldsContainer = new VerticalLayoutContainer();
		simpleOptionalFieldsContainer.add(hashLabel, new VerticalLayoutData(1, -1));
		simpleOptionalFieldsContainer.add(repositoryLabel, new VerticalLayoutData(1, -1));
		simpleOptionalFieldsContainer.add(uriLabel, new VerticalLayoutData(1, -1));

		/* OPTIONAL container added to a fieldset */
		FieldSet fieldSet_general_fields_optional = new FieldSet();
		fieldSet_general_fields_optional.setHeadingText("General optional");
		fieldSet_general_fields_optional.setCollapsible(true);
		// fieldSet_simple_fields.setWidth("auto");
		fieldSet_general_fields_optional.add(simpleOptionalFieldsContainer);

		// //////////////////////////////////////////////////////
		// Other fields and options (init)
		// //////////////////////////////////////////////////////

		/* ********************************* */
		/* identifiers options and fieldset */
		/* ********************************* */
		// Patient ID Fields (required)
		FieldSet fieldSet_identifier_patient = new FieldSet();
		fieldSet_identifier_patient.setHeadingText("Patient ID");
		fieldSet_identifier_patient.setCollapsible(true);
		fieldSet_identifier_patient.add(patientID);
		patientID.setAllowBlanks(false, false);

		// Unique ID Fieds (required)
		FieldSet fieldSet_identifier_unique = new FieldSet();
		fieldSet_identifier_unique.setHeadingText("Unique ID");
		fieldSet_identifier_unique.setCollapsible(true);
		fieldSet_identifier_unique.add(uniqueId);
		uniqueId.setAllowBlanks(false, false);

		/* ********************************** */
		/* coded terms options and fieldset */
		/* ********************************** */
		// Class Code (required)
		FieldSet fieldSet_codedTerm_classCode = new FieldSet();
		fieldSet_codedTerm_classCode.setHeadingText("Class Code");
		fieldSet_codedTerm_classCode.setCollapsible(true);
		fieldSet_codedTerm_classCode.add(classCode);
		classCode.setAllowBlanks(false, false, false);

		// Format Code (required)
		FieldSet fieldSet_codedTerm_formatCode = new FieldSet();
		fieldSet_codedTerm_formatCode.setHeadingText("Format Code");
		fieldSet_codedTerm_formatCode.setCollapsible(true);
		fieldSet_codedTerm_formatCode.add(formatCode);
		formatCode.setAllowBlanks(false, false, false);

		// Healthcare Facility Type (required)
		FieldSet fieldSet_codedTerm_healthcareFacility = new FieldSet();
		fieldSet_codedTerm_healthcareFacility.setHeadingText("Healthcare Facility Type");
		fieldSet_codedTerm_healthcareFacility.setCollapsible(true);
		fieldSet_codedTerm_healthcareFacility.add(healthcareFacilityType);
		healthcareFacilityType.setAllowBlanks(false, false, false);

		// Practice Setting Code (required)
		FieldSet fieldSet_codedTerm_practiceSettingCode = new FieldSet();
		fieldSet_codedTerm_practiceSettingCode.setHeadingText("Practice Setting Code");
		fieldSet_codedTerm_practiceSettingCode.setCollapsible(true);
		fieldSet_codedTerm_practiceSettingCode.add(practiceSettingCode);
		practiceSettingCode.setAllowBlanks(false, false, false);

		// Type Code (required)
		FieldSet fieldSet_codedTerm_typeCode = new FieldSet();
		fieldSet_codedTerm_typeCode.setHeadingText("Type Code");
		fieldSet_codedTerm_typeCode.setCollapsible(true);
		fieldSet_codedTerm_typeCode.add(typeCode);
		typeCode.setAllowBlanks(false, false, false);

		/* ****************************** */
		/* name values options and fields */
		/* ****************************** */
		// Legal Authenticator (optional)
		FieldSet fieldSet_nameValue_legalAuthenticator = new FieldSet();
		fieldSet_nameValue_legalAuthenticator.setHeadingText("Legal Authenticator");
		fieldSet_nameValue_legalAuthenticator.setCollapsible(true);
		fieldSet_nameValue_legalAuthenticator.add(legalAuthenticator);
		legalAuthenticator.setAllowBlanks(true, true);

		// Source Patient ID (optional)
		FieldSet fieldSet_nameValue_sourcePatientID = new FieldSet();
		fieldSet_nameValue_sourcePatientID.setHeadingText("Source Patient ID");
		fieldSet_nameValue_sourcePatientID.setCollapsible(true);
		fieldSet_nameValue_sourcePatientID.add(sourcePatientId);
		sourcePatientId.setAllowBlanks(true, true);

		// Service Start Time (optional)
		FieldSet fieldSet_nameValue_serviceStartTime = new FieldSet();
		fieldSet_nameValue_serviceStartTime.setHeadingText("Service Start Time");
		fieldSet_nameValue_serviceStartTime.setCollapsible(true);
		fieldSet_nameValue_serviceStartTime.add(serviceStartTime);
		serviceStartTime.setAllowBlanks(true, true);

		// Service Stop Time (optional)
		FieldSet fieldSet_nameValue_serviceStopTime = new FieldSet();
		fieldSet_nameValue_serviceStopTime.setHeadingText("Service Stop Time");
		fieldSet_nameValue_serviceStopTime.setCollapsible(true);
		fieldSet_nameValue_serviceStopTime.add(serviceStopTime);
		serviceStopTime.setAllowBlanks(true, true);

		// Size (optional)
		FieldSet fieldSet_nameValue_size = new FieldSet();
		fieldSet_nameValue_size.setHeadingText("Size");
		fieldSet_nameValue_size.setCollapsible(true);
		fieldSet_nameValue_size.add(size);
		size.setAllowBlanks(true, true);

		// Creation Time (required)
		FieldSet fieldSet_nameValue_creationTime = new FieldSet();
		fieldSet_nameValue_creationTime.setHeadingText("Creation Time");
		fieldSet_nameValue_creationTime.setCollapsible(true);
		fieldSet_nameValue_creationTime.add(creationTime);
		creationTime.setAllowBlanks(true, true);

		// AUTHORS (Optional)
		FieldSet fieldSet_authors = new FieldSet();
		fieldSet_authors.setHeadingText("Authors");
		fieldSet_authors.setCollapsible(true);
		fieldSet_authors.add(buildAuthorsEditorWidget());

		// TITLES (Optional)
		FieldSet field_titles = new FieldSet();
		field_titles.setHeadingText("Titles");
		field_titles.setCollapsible(true);
		field_titles.add(buildTitlesEditorWidget());

		// COMMENTS (Optional)
		FieldSet field_comments = new FieldSet();
		field_comments.setHeadingText("Comments");
		field_comments.setCollapsible(true);
		field_comments.add(buildCommentsEditorWidget());

		// CONFIDENTIALITY CODE (Optional)
		FieldSet field_confCode = new FieldSet();
		field_confCode.setHeadingText("Confidentiality Codes");
		field_confCode.setCollapsible(true);
		field_confCode.add(buildConfCodesEditorWidget());

		// /////////////////////////////////////////////////////////
		// Adding and ordering fieldsets in REQUIRED panel
		// /////////////////////////////////////////////////////////
		/* simple required fields added to FramedPanel container */
		requiredFields.add(fieldSet_general_fields_required);
		requiredFields.add(fieldSet_codedTerm_classCode);
		requiredFields.add(fieldSet_nameValue_creationTime);
		requiredFields.add(fieldSet_codedTerm_formatCode);
		requiredFields.add(fieldSet_codedTerm_healthcareFacility);
		requiredFields.add(fieldSet_identifier_patient);
		requiredFields.add(fieldSet_codedTerm_practiceSettingCode);
		requiredFields.add(fieldSet_codedTerm_typeCode);
		requiredFields.add(fieldSet_identifier_unique);

		// /////////////////////////////////////////////////////////
		// Adding and ordering fieldsets in OPTIONAL panel
		// /////////////////////////////////////////////////////////
		/* simple optional fields added to FramedPanel container */
		optionalFields.add(fieldSet_general_fields_optional);
		optionalFields.add(fieldSet_authors);
		optionalFields.add(field_comments);
		optionalFields.add(field_confCode);
		optionalFields.add(fieldSet_nameValue_legalAuthenticator);
		optionalFields.add(fieldSet_nameValue_serviceStartTime);
		optionalFields.add(fieldSet_nameValue_serviceStopTime);
		optionalFields.add(fieldSet_nameValue_size);
		optionalFields.add(fieldSet_nameValue_sourcePatientID);
		optionalFields.add(field_titles);

		setWidgetsInfo();

		form.setScrollMode(ScrollMode.AUTOY);
		form.add(container);

		return form;
	}

	private Widget buildAuthorsEditorWidget() {
		listViewAuthors = new ListView<Author, String>(new ListStore<Author>(authorprops.key()), authorprops.authorPerson());
		listViewAuthors.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		authors = new ListStoreEditor<Author>(listViewAuthors.getStore());

		FieldSet authorFS = new FieldSet();
		authorFS.setHeadingText("Author Entry");
		authorFS.add(author);
		author.setEditionMode(EditionMode.NODATA);

		ContentPanel authorCP = new ContentPanel();
		authorCP.setHeaderVisible(false);
		authorCP.setBorders(false);
		authorCP.setBodyBorder(false);
		enableAuthorButtonWidgets(EditionMode.NODATA);
		authorCP.addButton(newAuthorWidget);
		authorCP.addButton(editAuthorWidget);
		authorCP.addButton(saveAuthorWidget);
		authorCP.addButton(cancelAuthorWidget);
		authorCP.addButton(deleteAuthorWidget);
		authorCP.setButtonAlign(BoxLayoutPack.END);

		VerticalLayoutContainer vcon = new VerticalLayoutContainer();
		vcon.add(listViewAuthors, new VerticalLayoutData(1, 150));
		vcon.add(authorFS, new VerticalLayoutData(0.999, -1, new Margins(10, 0, 0, 0)));
		vcon.add(authorCP, new VerticalLayoutData(-1, 30));
		return vcon;
	}

	private Widget buildTitlesEditorWidget() {
		// listViewTitles = new ListView<InternationalString, String>(new
		// ListStore<InternationalString>(isprops.key()), isprops.value());
		List<ColumnConfig<InternationalString, ?>> list = new ArrayList<ColumnConfig<InternationalString, ?>>();
		list.add(new ColumnConfig<InternationalString, LanguageCode>(isprops.langCode(), 25, "Language Code"));
		list.add(new ColumnConfig<InternationalString, String>(isprops.value(), 100, "Title"));
		ColumnModel<InternationalString> cm = new ColumnModel<InternationalString>(list);
		gridTitles = new Grid<InternationalString>(new ListStore<InternationalString>(isprops.key()), cm);
		gridTitles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		gridTitles.getView().setAutoFill(true);

		titles = new ListStoreEditor<InternationalString>(gridTitles.getStore());

		FieldSet titlesFS = new FieldSet();
		titlesFS.setHeadingText("Title Entry");
		titlesFS.add(title);
		title.setEditionMode(EditionMode.NODATA);

		ContentPanel titleCP = new ContentPanel();
		titleCP.setHeaderVisible(false);
		titleCP.setBorders(false);
		titleCP.setBodyBorder(false);
		titleCP.addButton(newTitleWidget);
		titleCP.addButton(editTitleWidget);
		titleCP.addButton(saveTitleWidget);
		titleCP.addButton(cancelTitleWidget);
		titleCP.addButton(deleteTitleWidget);
		titleCP.setButtonAlign(BoxLayoutPack.END);

		enableTitleButtonWidgets(EditionMode.NODATA);

		VerticalLayoutContainer vcon = new VerticalLayoutContainer();
		vcon.add(gridTitles, new VerticalLayoutData(1, 150));
		vcon.add(titlesFS, new VerticalLayoutData(0.999, -1, new Margins(10, 0, 0, 0)));
		vcon.add(titleCP, new VerticalLayoutData(-1, 30));
		return vcon;
	}

	private Widget buildCommentsEditorWidget() {
		// listViewComments = new ListView<InternationalString, String>(new
		// ListStore<InternationalString>(isprops.key()), isprops.value());
		List<ColumnConfig<InternationalString, ?>> list = new ArrayList<ColumnConfig<InternationalString, ?>>();
		list.add(new ColumnConfig<InternationalString, LanguageCode>(isprops.langCode(), 25, "Language Code"));
		list.add(new ColumnConfig<InternationalString, String>(isprops.value(), 100, "Title"));
		ColumnModel<InternationalString> cm = new ColumnModel<InternationalString>(list);
		gridComments = new Grid<InternationalString>(new ListStore<InternationalString>(isprops.key()), cm);
		gridComments.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		gridComments.getView().setAutoFill(true);

		comments = new ListStoreEditor<InternationalString>(gridComments.getStore());

		FieldSet commentsFS = new FieldSet();
		commentsFS.setHeadingText("Comment Entry");
		commentsFS.add(comment);
		comment.setEditionMode(EditionMode.NODATA);

		ContentPanel commentCP = new ContentPanel();
		commentCP.setHeaderVisible(false);
		commentCP.setBorders(false);
		commentCP.setBodyBorder(false);
		commentCP.addButton(newCommentWidget);
		commentCP.addButton(editCommentWidget);
		commentCP.addButton(saveCommentWidget);
		commentCP.addButton(cancelCommentWidget);
		commentCP.addButton(deleteCommentWidget);
		commentCP.setButtonAlign(BoxLayoutPack.END);

		enableTitleButtonWidgets(EditionMode.NODATA);

		VerticalLayoutContainer vcon = new VerticalLayoutContainer();
		vcon.add(gridComments, new VerticalLayoutData(1, 150));
		vcon.add(commentsFS, new VerticalLayoutData(0.999, -1, new Margins(10, 0, 0, 0)));
		vcon.add(commentCP, new VerticalLayoutData(-1, 30));
		return vcon;
	}

	private Widget buildConfCodesEditorWidget() {
		// listViewConfidentialityCode = new ListView<CodedTerm, String>(new
		// ListStore<CodedTerm>(codedTermProps.key()),
		// codedTermProps.displayName());
		List<ColumnConfig<CodedTerm, ?>> list = new ArrayList<ColumnConfig<CodedTerm, ?>>();
		list.add(new ColumnConfig<CodedTerm, String>(codedTermProps.displayName(), 33, "Display name"));
		list.add(new ColumnConfig<CodedTerm, String>(codedTermProps.code(), 33, "Code"));
		list.add(new ColumnConfig<CodedTerm, String>(codedTermProps.codingScheme(), 33, "Coding scheme"));
		ColumnModel<CodedTerm> cm = new ColumnModel<CodedTerm>(list);
		gridConfidentialityCode = new Grid<CodedTerm>(new ListStore<CodedTerm>(codedTermProps.key()), cm);
		gridConfidentialityCode.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		gridConfidentialityCode.getView().setAutoFill(true);

		confidentialityCodes = new ListStoreEditor<CodedTerm>(gridConfidentialityCode.getStore());

		FieldSet confCodeFS = new FieldSet();
		confCodeFS.setHeadingText("Confidentiality Code Entry");
		confCodeFS.add(confidentialityCode);
		confidentialityCode.setEditionMode(EditionMode.NODATA);

		ContentPanel confCodeCP = new ContentPanel();
		confCodeCP.setHeaderVisible(false);
		confCodeCP.setBorders(false);
		confCodeCP.setBodyBorder(false);
		confCodeCP.addButton(newConfidentialityCodeWidget);
		confCodeCP.addButton(editConfidentialityCodeWidget);
		confCodeCP.addButton(saveConfidentialityCodeWidget);
		confCodeCP.addButton(cancelConfidentialityCodeWidget);
		confCodeCP.addButton(deleteConfidentialityCodeWidget);
		confCodeCP.setButtonAlign(BoxLayoutPack.END);

		enableConfCodeButtonWidgets(EditionMode.NODATA);

		VerticalLayoutContainer vcon = new VerticalLayoutContainer();
		vcon.add(gridConfidentialityCode, new VerticalLayoutData(1, 150));
		vcon.add(confCodeFS, new VerticalLayoutData(0.999, -1, new Margins(10, 0, 0, 0)));
		vcon.add(confCodeCP, new VerticalLayoutData(-1, 30));
		return vcon;
	}

	private void enableConfCodeButtonWidgets(EditionMode editionMode) {
		if (editionMode.equals(EditionMode.NODATA) || editionMode.equals(EditionMode.DISPLAY)) {
			newConfidentialityCodeWidget.setEnabled(true);
			saveConfidentialityCodeWidget.setEnabled(false);
			cancelConfidentialityCodeWidget.setEnabled(false);
		}
		if (editionMode.equals(EditionMode.NODATA)) {
			editConfidentialityCodeWidget.setEnabled(false);
			deleteConfidentialityCodeWidget.setEnabled(false);
		}
		if (editionMode.equals(EditionMode.DISPLAY)) {
			deleteConfidentialityCodeWidget.setEnabled(true);
			editConfidentialityCodeWidget.setEnabled(true);
		}
		if (editionMode.equals(EditionMode.NEW) || editionMode.equals(EditionMode.EDIT)) {
			editConfidentialityCodeWidget.setEnabled(false);
			saveConfidentialityCodeWidget.setEnabled(true);
			cancelConfidentialityCodeWidget.setEnabled(true);
			deleteConfidentialityCodeWidget.setEnabled(true);
		}
		if (editionMode.equals(EditionMode.NEW)) {
			newConfidentialityCodeWidget.setEnabled(false);
		}
		if (editionMode.equals(EditionMode.EDIT)) {
			newConfidentialityCodeWidget.setEnabled(true);
		}
	}

	private void enableAuthorButtonWidgets(EditionMode editionMode) {
		if (editionMode.equals(EditionMode.NODATA) || editionMode.equals(EditionMode.DISPLAY)) {
			newAuthorWidget.setEnabled(true);
			saveAuthorWidget.setEnabled(false);
			cancelAuthorWidget.setEnabled(false);
		}
		if (editionMode.equals(EditionMode.NODATA)) {
			editAuthorWidget.setEnabled(false);
			deleteAuthorWidget.setEnabled(false);
		}
		if (editionMode.equals(EditionMode.DISPLAY)) {
			deleteAuthorWidget.setEnabled(true);
			editAuthorWidget.setEnabled(true);
		}
		if (editionMode.equals(EditionMode.NEW) || editionMode.equals(EditionMode.EDIT)) {
			editAuthorWidget.setEnabled(false);
			saveAuthorWidget.setEnabled(true);
			cancelAuthorWidget.setEnabled(true);
			deleteAuthorWidget.setEnabled(true);
		}
		if (editionMode.equals(EditionMode.NEW)) {
			newAuthorWidget.setEnabled(false);
		}
		if (editionMode.equals(EditionMode.EDIT)) {
			newAuthorWidget.setEnabled(true);
		}
	}

	private void enableTitleButtonWidgets(EditionMode editionMode) {
		if (editionMode.equals(EditionMode.NODATA) || editionMode.equals(EditionMode.DISPLAY)) {
			newTitleWidget.setEnabled(true);
			saveTitleWidget.setEnabled(false);
			cancelTitleWidget.setEnabled(false);
		}
		if (editionMode.equals(EditionMode.NODATA)) {
			editTitleWidget.setEnabled(false);
			deleteTitleWidget.setEnabled(false);
		}
		if (editionMode.equals(EditionMode.DISPLAY)) {
			deleteTitleWidget.setEnabled(true);
			editTitleWidget.setEnabled(true);
		}
		if (editionMode.equals(EditionMode.NEW) || editionMode.equals(EditionMode.EDIT)) {
			editTitleWidget.setEnabled(false);
			saveTitleWidget.setEnabled(true);
			cancelTitleWidget.setEnabled(true);
			deleteTitleWidget.setEnabled(true);
		}
		if (editionMode.equals(EditionMode.NEW)) {
			newTitleWidget.setEnabled(false);
		}
		if (editionMode.equals(EditionMode.EDIT)) {
			newTitleWidget.setEnabled(true);
		}
	}

	private void enableCommentButtonWidgets(EditionMode editionMode) {
		if (editionMode.equals(EditionMode.NODATA) || editionMode.equals(EditionMode.DISPLAY)) {
			newCommentWidget.setEnabled(true);
			saveCommentWidget.setEnabled(false);
			cancelCommentWidget.setEnabled(false);
		}
		if (editionMode.equals(EditionMode.NODATA)) {
			editCommentWidget.setEnabled(false);
			deleteCommentWidget.setEnabled(false);
		}
		if (editionMode.equals(EditionMode.DISPLAY)) {
			deleteCommentWidget.setEnabled(true);
			editCommentWidget.setEnabled(true);
		}
		if (editionMode.equals(EditionMode.NEW) || editionMode.equals(EditionMode.EDIT)) {
			editCommentWidget.setEnabled(false);
			saveCommentWidget.setEnabled(true);
			cancelCommentWidget.setEnabled(true);
			deleteCommentWidget.setEnabled(true);
		}
		if (editionMode.equals(EditionMode.NEW)) {
			newCommentWidget.setEnabled(false);
		}
		if (editionMode.equals(EditionMode.EDIT)) {
			newCommentWidget.setEnabled(true);
		}
	}

	@Override
	protected void bindUI() {
		// btnSave.addSelectHandler(new SelectHandler() {
		//
		// @Override
		// public void onSelect(SelectEvent event) {
		// getPresenter().doSave();
		// }
		//
		// });
		// ///////////////////////////////////////////////////
		// --- AUTHORS WIDGETS UI BINDING
		// ///////////////////////////////////////////////////
		listViewAuthors.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Author>() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent<Author> event) {
				if (listViewAuthors.getSelectionModel().getSelectedItem() != null) {
					author.setEditionMode(EditionMode.DISPLAY);
					author.edit(listViewAuthors.getSelectionModel().getSelectedItem());
					enableAuthorButtonWidgets(EditionMode.DISPLAY);
				} else {
					author.setEditionMode(EditionMode.NODATA);
					enableAuthorButtonWidgets(EditionMode.NODATA);
				}
			}
		});
		newAuthorWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				listViewAuthors.getSelectionModel().deselectAll();
				author.setEditionMode(EditionMode.NEW);
				author.editNew();
				enableAuthorButtonWidgets(EditionMode.NEW);
			}
		});
		editAuthorWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				author.setEditionMode(EditionMode.EDIT);
				// author.edit(author);
				enableAuthorButtonWidgets(EditionMode.EDIT);
			}
		});
		cancelAuthorWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				author.setEditionMode(EditionMode.NODATA);
				enableAuthorButtonWidgets(EditionMode.NODATA);
				listViewAuthors.getSelectionModel().deselectAll();
				author.editNew();
			}
		});
		deleteAuthorWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				final ConfirmDeleteDialog cdd = new ConfirmDeleteDialog(listViewAuthors.getSelectionModel().getSelectedItem().getAuthorPerson()
						.getString());
				cdd.show();
				cdd.addHideHandler(new HideHandler() {

					@Override
					public void onHide(HideEvent event) {
						if (cdd.getHideButton() == cdd.getButtonById(PredefinedButton.YES.name())) {
							// perform YES action
							authors.getStore().remove(listViewAuthors.getSelectionModel().getSelectedItem());
							author.editNew();
						}
					}
				});
			}
		});
		saveAuthorWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				author.save();
				if (author.getEditionMode().equals(EditionMode.NEW)) {
					boolean isAlreadyThere = false;
					for (Author a : authors.getStore().getAll()) {
						if (a.getAuthorPerson().getString().equals(author.getModel().getAuthorPerson().getString())) {
							isAlreadyThere = true;
							break;
						}
					}
					if (isAlreadyThere) {
						Info.display("Impossible to add this value",
								"This author person already exists for this document. You can not add him again.");
					} else {
						authors.getStore().add(author.getModel());
						author.editNew();
						listViewAuthors.getSelectionModel().deselectAll();
						author.setEditionMode(EditionMode.NODATA);
						enableAuthorButtonWidgets(EditionMode.NODATA);
					}

				} else if (author.getEditionMode().equals(EditionMode.EDIT)) {
					authors.getStore().update(author.getModel());
					author.setEditionMode(EditionMode.DISPLAY);
					enableAuthorButtonWidgets(EditionMode.DISPLAY);
				}
			}
		});
		// ///////////////////////////////////////////////////
		// --- TITLES WIDGETS UI BINDING
		// ///////////////////////////////////////////////////
		gridTitles.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<InternationalString>() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent<InternationalString> event) {
				if (gridTitles.getSelectionModel().getSelectedItem() != null) {
					title.setEditionMode(EditionMode.DISPLAY);
					title.edit(gridTitles.getSelectionModel().getSelectedItem());
					enableTitleButtonWidgets(EditionMode.DISPLAY);
				} else {
					title.setEditionMode(EditionMode.NODATA);
					enableTitleButtonWidgets(EditionMode.NODATA);
				}
			}
		});
		newTitleWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				gridTitles.getSelectionModel().deselectAll();
				title.setEditionMode(EditionMode.NEW);
				title.editNew();
				enableTitleButtonWidgets(EditionMode.NEW);
			}
		});
		editTitleWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				title.setEditionMode(EditionMode.EDIT);
				// author.edit(author);
				enableTitleButtonWidgets(EditionMode.EDIT);
			}
		});
		cancelTitleWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				title.setEditionMode(EditionMode.NODATA);
				enableTitleButtonWidgets(EditionMode.NODATA);
				gridTitles.getSelectionModel().deselectAll();
				title.editNew();
			}
		});
		deleteTitleWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				final ConfirmDeleteDialog cdd = new ConfirmDeleteDialog(gridTitles.getSelectionModel().getSelectedItem().getValue().getString());
				cdd.show();
				cdd.addHideHandler(new HideHandler() {

					@Override
					public void onHide(HideEvent event) {
						if (cdd.getHideButton() == cdd.getButtonById(PredefinedButton.YES.name())) {
							// perform YES action
							titles.getStore().remove(gridTitles.getSelectionModel().getSelectedItem());
							title.editNew();
						}
					}
				});
			}
		});
		saveTitleWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				title.save();
				if (title.getEditionMode().equals(EditionMode.NEW)) {
					boolean isAlreadyThere = false;
					for (InternationalString is : titles.getStore().getAll()) {
						if (is.getValue().getString().equals(title.getModel().getValue().getString())) {
							isAlreadyThere = true;
							break;
						}
					}
					if (isAlreadyThere) {
						Info.display("Impossible to add this value", "This title already exists for this document. You can not add it again.");
					} else {
						titles.getStore().add(title.getModel());
						title.editNew();
						gridTitles.getSelectionModel().deselectAll();
						title.setEditionMode(EditionMode.NODATA);
						enableTitleButtonWidgets(EditionMode.NODATA);
					}

				} else if (title.getEditionMode().equals(EditionMode.EDIT)) {
					titles.getStore().update(title.getModel());
					title.setEditionMode(EditionMode.DISPLAY);
					enableTitleButtonWidgets(EditionMode.DISPLAY);
				}
			}
		});

		// ///////////////////////////////////////////////////
		// --- COMMENTS WIDGETS UI BINDING
		// ///////////////////////////////////////////////////
		gridComments.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<InternationalString>() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent<InternationalString> event) {
				if (gridComments.getSelectionModel().getSelectedItem() != null) {
					comment.setEditionMode(EditionMode.DISPLAY);
					comment.edit(gridComments.getSelectionModel().getSelectedItem());
					enableCommentButtonWidgets(EditionMode.DISPLAY);
				} else {
					comment.setEditionMode(EditionMode.NODATA);
					enableCommentButtonWidgets(EditionMode.NODATA);
				}
			}
		});
		newCommentWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				gridComments.getSelectionModel().deselectAll();
				comment.setEditionMode(EditionMode.NEW);
				comment.editNew();
				enableCommentButtonWidgets(EditionMode.NEW);
			}
		});
		editCommentWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				comment.setEditionMode(EditionMode.EDIT);
				// author.edit(author);
				enableCommentButtonWidgets(EditionMode.EDIT);
			}
		});
		cancelCommentWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				comment.setEditionMode(EditionMode.NODATA);
				enableCommentButtonWidgets(EditionMode.NODATA);
				gridComments.getSelectionModel().deselectAll();
				comment.editNew();
			}
		});
		deleteCommentWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				final ConfirmDeleteDialog cdd = new ConfirmDeleteDialog(gridComments.getSelectionModel().getSelectedItem().getValue().getString());
				cdd.show();
				cdd.addHideHandler(new HideHandler() {

					@Override
					public void onHide(HideEvent event) {
						if (cdd.getHideButton() == cdd.getButtonById(PredefinedButton.YES.name())) {
							// perform YES action
							comments.getStore().remove(gridComments.getSelectionModel().getSelectedItem());
							comment.editNew();
						}
					}
				});
			}
		});
		saveCommentWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				comment.save();
				if (comment.getEditionMode().equals(EditionMode.NEW)) {
					boolean isAlreadyThere = false;
					for (InternationalString is : comments.getStore().getAll()) {
						if (is.getValue().getString().equals(comment.getModel().getValue().getString())) {
							isAlreadyThere = true;
							break;
						}
					}
					if (isAlreadyThere) {
						Info.display("Impossible to add this value", "This comment already exists for this document. You can not add it again.");
					} else {
						comments.getStore().add(comment.getModel());
						comment.editNew();
						gridComments.getSelectionModel().deselectAll();
						comment.setEditionMode(EditionMode.NODATA);
						enableCommentButtonWidgets(EditionMode.NODATA);
					}

				} else if (comment.getEditionMode().equals(EditionMode.EDIT)) {
					comments.getStore().update(comment.getModel());
					comment.setEditionMode(EditionMode.DISPLAY);
					enableCommentButtonWidgets(EditionMode.DISPLAY);
				}
			}
		});

		// ///////////////////////////////////////////////////
		// --- Confidentiality Codes WIDGETS UI BINDING
		// ///////////////////////////////////////////////////
		gridConfidentialityCode.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<CodedTerm>() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent<CodedTerm> event) {
				if (gridConfidentialityCode.getSelectionModel().getSelectedItem() != null) {
					confidentialityCode.setEditionMode(EditionMode.DISPLAY);
					confidentialityCode.edit(gridConfidentialityCode.getSelectionModel().getSelectedItem());
					enableConfCodeButtonWidgets(EditionMode.DISPLAY);
				} else {
					confidentialityCode.setEditionMode(EditionMode.NODATA);
					enableConfCodeButtonWidgets(EditionMode.NODATA);
				}
			}
		});
		newConfidentialityCodeWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				gridConfidentialityCode.getSelectionModel().deselectAll();
				confidentialityCode.setEditionMode(EditionMode.NEW);
				confidentialityCode.editNew();
				enableConfCodeButtonWidgets(EditionMode.NEW);
			}
		});
		editConfidentialityCodeWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				confidentialityCode.setEditionMode(EditionMode.EDIT);
				// author.edit(author);
				enableConfCodeButtonWidgets(EditionMode.EDIT);
			}
		});
		cancelConfidentialityCodeWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				confidentialityCode.setEditionMode(EditionMode.NODATA);
				enableConfCodeButtonWidgets(EditionMode.NODATA);
				gridConfidentialityCode.getSelectionModel().deselectAll();
				confidentialityCode.editNew();
			}
		});
		deleteConfidentialityCodeWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				final ConfirmDeleteDialog cdd = new ConfirmDeleteDialog(gridConfidentialityCode.getSelectionModel().getSelectedItem()
						.getDisplayName().getString());
				cdd.show();
				cdd.addHideHandler(new HideHandler() {

					@Override
					public void onHide(HideEvent event) {
						if (cdd.getHideButton() == cdd.getButtonById(PredefinedButton.YES.name())) {
							// perform YES action
							confidentialityCodes.getStore().remove(gridConfidentialityCode.getSelectionModel().getSelectedItem());
							confidentialityCode.editNew();
						}
					}
				});
			}
		});
		saveCommentWidget.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				confidentialityCode.save();
				if (confidentialityCode.getEditionMode().equals(EditionMode.NEW)) {
					boolean isAlreadyThere = false;
					for (CodedTerm is : confidentialityCodes.getStore().getAll()) {
						if (is.getDisplayName().getString().equals(confidentialityCode.getModel().getDisplayName().getString())) {
							isAlreadyThere = true;
							break;
						}
					}
					if (isAlreadyThere) {
						Info.display("Impossible to add this value", "This comment already exists for this document. You can not add it again.");
					} else {
						confidentialityCodes.getStore().add(confidentialityCode.getModel());
						confidentialityCode.editNew();
						gridConfidentialityCode.getSelectionModel().deselectAll();
						confidentialityCode.setEditionMode(EditionMode.NODATA);
						enableConfCodeButtonWidgets(EditionMode.NODATA);
					}

				} else if (confidentialityCode.getEditionMode().equals(EditionMode.EDIT)) {
					confidentialityCodes.getStore().update(confidentialityCode.getModel());
					confidentialityCode.setEditionMode(EditionMode.DISPLAY);
					enableConfCodeButtonWidgets(EditionMode.DISPLAY);
				}
			}
		});
	}

	/**
	 * Sets information about each widgets to guide the user.
	 */
	public void setWidgetsInfo() {
		/* simple fields info */
		id.setEmptyText("ex: 123456789");
		id.setToolTipConfig(new ToolTipConfig("ID is a string", "It should contain less than 256 characters"));
		hash.setEmptyText("ex: Hex456");
		hash.setToolTipConfig(new ToolTipConfig("Hash is a string", "It should contain less than 256 characters"));
		languageCode.setEmptyText("ex: en-GB");
		languageCode.setToolTipConfig(new ToolTipConfig("LanguageCode from RFC3066", "Language code format is \"[a-z](2)-[A-Z](2)\""));
		mimeType.setEmptyText("ex: mimetype");
		mimeType.setToolTipConfig(new ToolTipConfig("Mime Type is a string", "It should contain less than 256 characters"));
		repoUId.setEmptyText("ex: 1.2.7.0.3.2.37768.2007.2.2");
		repoUId.setToolTipConfig(new ToolTipConfig("Repository Unique ID is an OID",
				"As defined in the HL7 implementation for OID (http://www.hl7.org/implement/standards/product_brief.cfm?product_id=210)" + "\n"
						+ "OID format is \"[1-9](\\.[0-9]+)*]\""));
		uri.setEmptyText("ex: uriO");
		uri.setToolTipConfig(new ToolTipConfig("URI is a string", "It should contain less than 256 characters"));

		/* identifiers info */
		patientID.setEmptyTexts("ex: 76cc^^1.3.6367.2005.3.7&ISO", "ex: urn:uuid:6b5aea1a-625s-5631-v4se-96a0a7b38446");
		patientID.setToolTipConfigs(new ToolTipConfig("Value is a string", "It should contain less than 256 characters"), new ToolTipConfig(
				"idType is a string", "It should contain less than 256 characters"));
		uniqueId.setEmptyTexts("ex: 76cc^^1.3.6367.2005.3.7&ISO", "ex: 2008.8.1.35447");
		uniqueId.setToolTipConfigs(
				new ToolTipConfig("Value is a string", "76cc^^1.3.6367.2005.3.7&ISO"),
				new ToolTipConfig(
						"idType is an OID",
						"As defined in the HL7 implementation for OID (http://www.hl7.org/implement/standards/product_brief.cfm?product_id=210)\nOID format is \"[1-9](\\.[0-9]+)*]\""));

		/* name values info */
		legalAuthenticator.setEmptyTexts("", "ex: 123534YFQ1662");
		legalAuthenticator.setToolTipConfigs(null, new ToolTipConfig("Value is a String", "It should contain les than 256 characters"));
		sourcePatientId.setEmptyTexts("", "ex: 58642j65s^^^5.8.4");
		sourcePatientId.setToolTipConfigs(null, new ToolTipConfig("Value is a String", "It should contain les than 256 characters"));
		serviceStartTime.setEmptyTexts("", "ex: 201103160830");
		serviceStartTime
				.setToolTipConfigs(
						null,
						new ToolTipConfig(
								"DTM Value is a date/time value",
								"The format of these values is defined as following: YYYY[MM[DD[hh[mm[ss]]]]]; YYYY is the four digit year (ex: 2014); MM is the two digit month 01-12, where January is 01, December is 12; DD is the two digit day of the month 01-31; HH is the two digit hour, 00-23, where 00 is midnight, 01 is 1 am, 12 is noon, 13 is 1 pm; mm is the two digit minute, 00-59; ss is the two digit seconds, 00-59"));
		serviceStopTime.setEmptyTexts("", "ex: 201103160830");
		serviceStopTime
				.setToolTipConfigs(
						null,
						new ToolTipConfig(
								"DTM Value is a date/time value",
								"The format of these values is defined as following: YYYY[MM[DD[hh[mm[ss]]]]]; YYYY is the four digit year (ex: 2014); MM is the two digit month 01-12, where January is 01, December is 12; DD is the two digit day of the month 01-31; HH is the two digit hour, 00-23, where 00 is midnight, 01 is 1 am, 12 is noon, 13 is 1 pm; mm is the two digit minute, 00-59; ss is the two digit seconds, 00-59"));
		creationTime.setEmptyTexts("", "ex: 201103160830");
		creationTime
				.setToolTipConfigs(
						null,
						new ToolTipConfig(
								"DTM Value is a date/time value",
								"The format of these values is defined as following: YYYY[MM[DD[hh[mm[ss]]]]]; YYYY is the four digit year (ex: 2014); MM is the two digit month 01-12, where January is 01, December is 12; DD is the two digit day of the month 01-31; HH is the two digit hour, 00-23, where 00 is midnight, 01 is 1 am, 12 is noon, 13 is 1 pm; mm is the two digit minute, 00-59; ss is the two digit seconds, 00-59"));
	}

}
