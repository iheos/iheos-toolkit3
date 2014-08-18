package edu.tn.xds.metadata.editor.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
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
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import edu.tn.xds.metadata.editor.client.editor.properties.AuthorProperties;
import edu.tn.xds.metadata.editor.client.editor.widgets.*;
import edu.tn.xds.metadata.editor.client.editor.widgets.CodedTermWidgets.CodedTermsEditableGridWidget;
import edu.tn.xds.metadata.editor.client.editor.widgets.InternationalStringWidgets.InternationalStringEditableGrid;
import edu.tn.xds.metadata.editor.client.editor.widgets.NameValueWidgets.NameValueDTMEditorWidget;
import edu.tn.xds.metadata.editor.client.editor.widgets.NameValueWidgets.NameValueIntegerEditorWidget;
import edu.tn.xds.metadata.editor.client.editor.widgets.NameValueWidgets.NameValueString256EditorWidget;
import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractView;
import edu.tn.xds.metadata.editor.client.widgets.ConfirmDeleteDialog;
import edu.tn.xds.metadata.editor.shared.model.Author;
import edu.tn.xds.metadata.editor.shared.model.CodedTerm;
import edu.tn.xds.metadata.editor.shared.model.InternationalString;
import edu.tn.xds.metadata.editor.shared.model.XdsDocumentEntry;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class DocumentModelEditorView extends AbstractView<DocumentModelEditorPresenter> implements Editor<XdsDocumentEntry> {
    private final AuthorProperties authorprops = GWT.create(AuthorProperties.class);

    private final VerticalLayoutContainer form = new VerticalLayoutContainer();

    VerticalLayoutContainer requiredFields = new VerticalLayoutContainer();
    VerticalLayoutContainer optionalFields = new VerticalLayoutContainer();

    /* simple fields declaration */
    @Inject
    String256EditorWidget id;
    @Inject
    String256EditorWidget hash;
    @Inject
    String256EditorWidget fileName;
    @Inject
    String256EditorWidget uri;
    @Inject
    LanguageCodeComboBox languageCode;
    @Inject
    MimeTypeComboBox mimeType;

    // Could be injected using FactoryProvider assisted inject
    OIDEditorWidget repoUId = new OIDEditorWidget(false);

    /* Identifiers declaration */
    @Inject
    IdentifierOIDEditorWidget uniqueId;
    @Inject
    IdentifierString256EditorWidget patientID;

    /* coded terms declaration */
    // Could be injected using FactoryProvider assisted inject
    PredefinedCodedTermComboBox classCode = new PredefinedCodedTermComboBox(PredefinedCodes.CLASS_CODES);
    PredefinedCodedTermComboBox formatCode = new PredefinedCodedTermComboBox(PredefinedCodes.FORMAT_CODES);
    PredefinedCodedTermComboBox healthcareFacilityType = new PredefinedCodedTermComboBox(PredefinedCodes.HEALTHCARE_FACILITY_TYPE_CODES);
    PredefinedCodedTermComboBox practiceSettingCode = new PredefinedCodedTermComboBox(PredefinedCodes.PRACTICE_SETTING_CODES);
    PredefinedCodedTermComboBox typeCode = new PredefinedCodedTermComboBox(PredefinedCodes.TYPE_CODES);

    /* name values declaration */
    // Could be injected using FactoryProvider assisted inject
    NameValueString256EditorWidget legalAuthenticator = new NameValueString256EditorWidget("Legal Authenticator");
    NameValueString256EditorWidget sourcePatientId = new NameValueString256EditorWidget("Source Patient ID");
    NameValueString256EditorWidget sourcePatientInfo = new NameValueString256EditorWidget("Source Patient Info");
    NameValueDTMEditorWidget creationTime = new NameValueDTMEditorWidget("Creation Time");
    NameValueDTMEditorWidget serviceStartTime = new NameValueDTMEditorWidget("Service Start Time");
    NameValueDTMEditorWidget serviceStopTime = new NameValueDTMEditorWidget("Service Stop Time");
    NameValueIntegerEditorWidget size = new NameValueIntegerEditorWidget("File Size");

    // ///////////////////////////////////////////////////////////////////////
    // ---- AUTHORS WIDGETS
    // ///////////////////////////////////////////////////////////////////////
    ListStoreEditor<Author> authors;
    @Ignore
    ListView<Author, String> listViewAuthors;
    @Ignore
    @Inject
    AuthorEditorWidget author;
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
    InternationalStringEditableGrid titlesGrid;
    // ///////////////////////////////////////////////////////////////////////
    // ---- Comments WIDGETS
    // ///////////////////////////////////////////////////////////////////////
    ListStoreEditor<InternationalString> comments;
    InternationalStringEditableGrid commentsGrid;

    // ///////////////////////////////////////////////////////////////////////
    // ---- ConfidentialityCodes WIDGETS
    // ///////////////////////////////////////////////////////////////////////
    ListStoreEditor<CodedTerm> confidentialityCodes;
    CodedTermsEditableGridWidget confidentialityCodesGrid;

    // ///////////////////////////////////////////////////////////////////////
    // ---- EventCodes WIDGETS
    // ///////////////////////////////////////////////////////////////////////
    ListStoreEditor<CodedTerm> eventCode;
    CodedTermsEditableGridWidget eventCodesGrid;

    @Override
    protected Map<String, Widget> getPathToWidgetsMap() {
        Map<String, Widget> map = new HashMap<String, Widget>();
        map.put("id", id);
        map.put("fileName", fileName);
        map.put("hash", hash);
        map.put("patientID", patientID);
        map.put("uniqueId", uniqueId);
        map.put("uri", uri);
        map.put("repoUId", repoUId);
        map.put("author", author);
        return map;
    }

    @Override
    protected Widget buildUI() {
        final VerticalLayoutContainer container = new VerticalLayoutContainer();
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
        container.add(fp1, new VerticalLayoutData(1, -1, new Margins(10, 10, 10, 10)));
        container.add(fp2, new VerticalLayoutData(1, -1, new Margins(10, 10, 10, 10)));

        // //////////////////////////////////////
        // Simple fields label and options (init)
        // /////////////////////////////////////
        // ID Field (required)
        FieldLabel idLabel = new FieldLabel(id, "Entry UUID");
        idLabel.setLabelWidth(125);

        // Filename field
        FieldLabel filenameLabel = new FieldLabel(fileName, "File name");
        filenameLabel.setLabelWidth(125);

        // Hash Field (required)
        FieldLabel hashLabel = new FieldLabel(hash, "Hash");
        hashLabel.setLabelWidth(125);

        // Language Code Field (required)
        FieldLabel languageCodeLabel = new FieldLabel(languageCode, "Language Code");
        languageCodeLabel.setLabelWidth(125);


        // Mime Type Field (required)
        FieldLabel mimeTypeLabel = new FieldLabel(mimeType, "Mime Type");
        mimeTypeLabel.setLabelWidth(125);

        // Class Code Field (required)
        FieldLabel classCodeLabel = new FieldLabel(classCode.asWidget(), "Class Code");
        classCodeLabel.setLabelWidth(125);

        // Format Code Field (required)
        FieldLabel formatCodeLabel = new FieldLabel(formatCode, "Format Code");
        formatCodeLabel.setLabelWidth(125);

        // practiceSettingCode Field (required)
        FieldLabel healthcareFacilityTypeLabel = new FieldLabel(healthcareFacilityType, "Healthcare Facility");
        healthcareFacilityTypeLabel.setLabelWidth(125);

        // healthcare facility Code Field (required)
        FieldLabel practiceSettingCodeLabel = new FieldLabel(practiceSettingCode, "Practice Setting Code");
        practiceSettingCodeLabel.setLabelWidth(125);

        // type Code Field (required)
        FieldLabel typeCodeLabel = new FieldLabel(typeCode, "Type Code");
        typeCodeLabel.setLabelWidth(125);

        // Repository Unique ID Field (optional)
        FieldLabel repositoryLabel = new FieldLabel(repoUId, "Repository Unique ID");
        repositoryLabel.setLabelWidth(125);

        // URI Field (optional)
        FieldLabel uriLabel = new FieldLabel(uri, "URI");
        uriLabel.setLabelWidth(125);

        // ////////////////////////////////////////////////////
        // --- Adding REQUIRED simple fields labels to containers
        // ////////////////////////////////////////////////////
        VerticalLayoutContainer simpleRequiredFieldsContainer = new VerticalLayoutContainer();
        simpleRequiredFieldsContainer.add(filenameLabel, new VerticalLayoutData(1, -1));
        simpleRequiredFieldsContainer.add(idLabel, new VerticalLayoutData(1, -1));
        simpleRequiredFieldsContainer.add(languageCodeLabel, new VerticalLayoutData(1, -1));
        simpleRequiredFieldsContainer.add(mimeTypeLabel, new VerticalLayoutData(1, -1));
        simpleRequiredFieldsContainer.add(classCodeLabel, new VerticalLayoutData(1, -1));
        simpleRequiredFieldsContainer.add(formatCodeLabel, new VerticalLayoutData(1, -1));
        simpleRequiredFieldsContainer.add(healthcareFacilityTypeLabel, new VerticalLayoutData(1, -1));
        simpleRequiredFieldsContainer.add(practiceSettingCodeLabel, new VerticalLayoutData(1, -1));
        simpleRequiredFieldsContainer.add(typeCodeLabel, new VerticalLayoutData(1, -1));

		/* REQUIRED container added to a fieldset */
        FieldSet fieldSet_general_fields_required = new FieldSet();
        fieldSet_general_fields_required.setHeadingText("General required");
        fieldSet_general_fields_required.setCollapsible(true);
        fieldSet_general_fields_required.add(simpleRequiredFieldsContainer);

        // //////////////////////////////////////////////////////////
        // --- Adding OPTIONAL simple fields labels to containers
        // //////////////////////////////////////////////////////////
        VerticalLayoutContainer filePropertiesFieldsContainer = new VerticalLayoutContainer();
        filePropertiesFieldsContainer.add(hashLabel, new VerticalLayoutData(1, -1));
        filePropertiesFieldsContainer.add(size.asWidget(), new VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));

        VerticalLayoutContainer repositoryAttributesFieldsContainer = new VerticalLayoutContainer();
        repositoryAttributesFieldsContainer.add(uriLabel, new VerticalLayoutData(1, -1));
        repositoryAttributesFieldsContainer.add(repositoryLabel, new VerticalLayoutData(1, -1));

		/* OPTIONAL container added to a fieldset */
        FieldSet fieldSet_fileProperties = new FieldSet();
        fieldSet_fileProperties.setHeadingText("Files properties");
        fieldSet_fileProperties.setCollapsible(true);
        fieldSet_fileProperties.add(filePropertiesFieldsContainer);

        FieldSet fieldSet_repoAttributes = new FieldSet();
        fieldSet_repoAttributes.setHeadingText("Repository attributes");
        fieldSet_repoAttributes.setCollapsible(true);
        fieldSet_repoAttributes.add(repositoryAttributesFieldsContainer, new VerticalLayoutData(1, -1));

        // //////////////////////////////////////////////////////
        // Other fields and options (init)
        // //////////////////////////////////////////////////////

		/* ********************************* */
        /* identifiers options and fieldset */
        /* ********************************* */
        // Patient ID Fields (required)
        FieldLabel patientIdLabel = new FieldLabel(patientID, "Patient ID");
        patientIdLabel.setLabelWidth(125);
        simpleRequiredFieldsContainer.add(patientIdLabel, new VerticalLayoutData(1, -1));

        // Unique ID Fieds (required)
        FieldLabel uniqueIdLabel = new FieldLabel(uniqueId, "Unique ID");
        uniqueIdLabel.setLabelWidth(125);
        simpleRequiredFieldsContainer.add(uniqueIdLabel, new VerticalLayoutData(1, -1));

		/* ********************************** */
        /* coded terms options and fieldset */
        /* ********************************** */

		/* ****************************** */
        /* name values options and fields */
        /* ****************************** */
        // Legal Authenticator (optional)
        legalAuthenticator.setListMaxSize(1);

        // Source Patient ID (optional)
        sourcePatientId.setListMaxSize(1);

        // Source Patient Info
        sourcePatientInfo.setListMaxSize(10);

        // Service Start Time (optional)
        serviceStartTime.setListMaxSize(1);
//        serviceStartTime.disableEditing();

        // Service Stop Time (optional)
        serviceStopTime.setListMaxSize(1);

        // Size (optional)
        size.disableEditing();

        // Creation Time (required)
        creationTime.disableToolbar();

        // AUTHORS (Optional)
        FieldSet fieldSet_authors = new FieldSet();
        fieldSet_authors.setHeadingText("Authors");
        fieldSet_authors.setCollapsible(true);
        fieldSet_authors.add(buildAuthorsEditorWidget());

        // TITLES (Optional)
        titlesGrid = new InternationalStringEditableGrid("Titles");
        titles = new ListStoreEditor<InternationalString>(titlesGrid.getStore());


        // COMMENTS (Optional)
        commentsGrid = new InternationalStringEditableGrid("Comments");
        comments = new ListStoreEditor<InternationalString>(commentsGrid.getStore());

        // CONFIDENTIALITY CODE (Optional)
        confidentialityCodesGrid = new CodedTermsEditableGridWidget("Confidentiality Codes", PredefinedCodes.CONFIDENTIALITY_CODES);
        confidentialityCodes = new ListStoreEditor<CodedTerm>(confidentialityCodesGrid.getStore());

        // CONFIDENTIALITY CODE (Optional)
        eventCodesGrid = new CodedTermsEditableGridWidget("Event Codes", PredefinedCodes.EVENT_CODES);
        eventCode = new ListStoreEditor<CodedTerm>(eventCodesGrid.getStore());

        // /////////////////////////////////////////////////////////
        // Adding and ordering fieldsets in REQUIRED panel
        // /////////////////////////////////////////////////////////
        /* simple required fields added to FramedPanel container */
        requiredFields.add(fieldSet_general_fields_required);
        requiredFields.add(creationTime.asWidget(), new VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));

        // /////////////////////////////////////////////////////////
        // Adding and ordering fieldsets in OPTIONAL fields panel
        // /////////////////////////////////////////////////////////
        /* simple optional fields added to FramedPanel container */
        optionalFields.add(fieldSet_fileProperties);
        optionalFields.add(fieldSet_repoAttributes);
        optionalFields.add(titlesGrid.asWidget(), new VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));
        optionalFields.add(commentsGrid.asWidget(), new VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));
        optionalFields.add(fieldSet_authors);
        optionalFields.add(legalAuthenticator.asWidget(), new VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));
        optionalFields.add(sourcePatientId.asWidget(), new VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));
        optionalFields.add(sourcePatientInfo.asWidget(), new VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));
        optionalFields.add(confidentialityCodesGrid.asWidget(), new VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));
        optionalFields.add(eventCodesGrid.asWidget(), new VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));
        optionalFields.add(serviceStartTime.asWidget(), new VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));
        optionalFields.add(serviceStopTime.asWidget(), new VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));

        setWidgetsInfo();

        form.setScrollMode(ScrollMode.AUTO);
        form.add(container);

        form.forceLayout();

        return form;
    }

    @Override
    protected void bindUI() {
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
                    } else if (author.hasErrors()) {
                        Info.display("Impossible to add this value", "There still are errors in the editor.");
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
        form.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                form.forceLayout();
            }
        });
    }

    /**
     * Method to build an authors editor widget with author sub-editor
     *
     * @return authors editor widget w/ sub-editor
     */
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

    /**
     * Sets information about each widgets to guide the user. Tooltips, empty texts and validators
     */
    public void setWidgetsInfo() {
        // class code
        classCode.setEmptyText("Select a class...");
        classCode.clear();
        classCode.setAllowBlank(false);
        // comments
        commentsGrid.setToolbarHelpButtonTooltip(new ToolTipConfig("Help on Comments?", "Comments associated with the Document. Free form text with an XDS " +
                "Affinity Domain specified usage. Each comment should have the same meaning in a different language."));
        // confidentiality codes
        confidentialityCodesGrid.setToolbarHelpButtonTooltip(new ToolTipConfig("Help on Confidentiality codes?", "The code specifying the level of confidentiality of the XDS Document." +
                "These codes are specific to an XDS Affinity Domain. Enforcement and " +
                "issues related to highly sensitive documents are beyond the scope of " +
                "XDS (see security section). These issues are expected to be addressed in " +
                "later years. confidentialityCode is part of a codification scheme and " +
                "value set enforced by the Document Registry. Shall have one or more " +
                "values. Code multiple values by creating multiple classification objects"));
        // event codes
        eventCodesGrid.setToolbarHelpButtonTooltip(new ToolTipConfig("Help on Event codes?", "This list of codes represents the main clinical acts, such as a colonoscopy " +
                "or an appendectomy, being documented. In some cases, the event is " +
                "inherent in the typeCode, such as a \"History and Physical Report\" in " +
                "which the procedure being documented is necessarily a \"History and " +
                "Physical\" act. " +
                "An event can further specialize the act inherent in the typeCode, such as " +
                "where it is simply \"Procedure Report\" and the procedure was a " +
                "\"colonoscopy\". If one or more eventCodes are included, they shall not " +
                "conflict with the values inherent in the classCode, practiceSettingCode or " +
                "typeCode, as such a conflict would create an ambiguous situation. " +
                "This short list of codes is provided to be used as “key words” for certain " +
                "types of queries. If present, shall have one or more values. Code multiple " +
                "values by creating multiple classification objects."));
        // filename
        fileName.setEmptyText("Metadata filename (with file extension)");
        fileName.setToolTipConfig(new ToolTipConfig("Metadata file name (with extension)", "This is the name of the metadata file which will be generated. <br/>For example: xds-metadata.xml"));
        fileName.setAllowBlank(false);
        // format code
        formatCode.setEmptyText("Select a format...");
        formatCode.clear();
        formatCode.setAllowBlank(false);
        // hash code
        hash.setEmptyText("ex: Hex456");
        hash.setToolTipConfig(new ToolTipConfig("Hash is a string", "It should contain less than 256 characters"));
        hash.setAllowBlank(true);
        hash.addValidator(new RegExValidator("^[0-9a-fA-F]+$", "Value is not correct. It is supposed to be a hexadecimal value."));
        // healthcare facility
        healthcareFacilityType.setEmptyText("Select an healthcare facility...");
        healthcareFacilityType.clear();
        healthcareFacilityType.setAllowBlank(false);
        // entry uuid
        id.setEmptyText("ex: 123456789");
        id.setToolTipConfig(new ToolTipConfig("ID is a string", "It should contain less than 256 characters"));
        id.setAllowBlank(false);
        id.addValidator(new RegExValidator("[1-9][0-9]+", "Value is not correct. It is supposed to be a number."));
        // language code
        languageCode.setAllowBlank(false);
        languageCode.setEmptyText("Select a language...");
        languageCode.setToolTipConfig(new ToolTipConfig("LanguageCode from RFC3066", "Language code format is \"[a-z](2)-[A-Z](2)\""));
        // legal authenticator
        legalAuthenticator.setEditingFieldToolTip("A legal authenticator is a string256 in XCN format. It should be formatted as follow: \n<b>Identifier^LastName^FirstName[^SecondName[^FurtherGivenNames]][^Suffix][^Prefix]^AssigningAuthority</b>.");
        legalAuthenticator.setEmptyTexts("ex: 11375^Welby^Marcus^J^Jr. MD^Dr^^^&1.2.840.113619.6.197&ISO");
        legalAuthenticator.addFieldValidator(new RegExValidator("^[0-9]+\\^(([A-Za-z]+\\.?\\s?)+\\^){3,7}\\^{2}&[0-9]+(\\.[0-9]+)*(&ISO)$"));
        legalAuthenticator.setToolbarHelpButtonTooltip(new ToolTipConfig("Help about legal authenticator?", "Represents a participant who has legally authenticated or attested the" +
                "document within the authorInstitution. Legal authentication implies that " +
                "a document has been signed manually or electronically by the " +
                "legalAuthenticator. This attribute may be absent if not applicable. If " +
                "present, shall have a single value. <br/>It should be formatted as following:<br/>" +
                "<ul><li>Identifier</li><li>Last Name</li><li>First name</li><li>Second and further names</li><li>Suffix</li><li>Prefix</li><li>Assigning authority</li></ul><br/>" +
                "An example of person name with ID number using this data type is as follows:<br/>" +
                "<b>11375^Welby^Marcus^J^Jr. MD^Dr^^^&1.2.840.113619.6.197&ISO</b>"));
        // mime type
        mimeType.setAllowBlank(false);
        mimeType.setEmptyText("Select a mime type...");
        mimeType.setToolTipConfig(new ToolTipConfig("Mime Type is a string", "It should contain less than 256 characters"));
        // patient id
        patientID.setEmptyTexts("ex: 76cc^^1.3.6367.2005.3.7&ISO", "ex: urn:uuid:6b5aea1a-625s-5631-v4se-96a0a7b38446");
        patientID.setToolTipConfigs(new ToolTipConfig("Patient ID is a String256 in HL7 CX format", "The required format is:" +
                "IDNumber^^^&OIDofAssigningAuthority&ISO"), new ToolTipConfig(
                "idType is a string256 in HL7 CX format", "The required format is: " +
                "IDNumber^^^&OIDofAssigningAuthority&ISO"));
        patientID.setAllowBlanks(false, false);
        patientID.addValueFieldValidator(new RegExValidator("^(([A-Za-z])|([1-9]))*[0-9A-z]+\\^{3}&[1-9][0-9]*(\\.[1-9][0-9]*)+(&ISO)$", "Value's format is not a correct. \nIt should be like this: 6578946^^^&1.3.6.1.4.1.21367.2005.3.7&ISO."));
        // pratice setting code
        practiceSettingCode.setEmptyText("Select a practice setting...");
        practiceSettingCode.clear();
        practiceSettingCode.setAllowBlank(false);
        // repository unique id
        repoUId.setEmptyText("ex: 1.2.7.0.3.2.37768.2007.2.2");
        repoUId.setToolTipConfig(new ToolTipConfig("Repository Unique ID is an OID",
                "As defined in the HL7 implementation for OID (http://www.hl7.org/implement/standards/product_brief.cfm?product_id=210) "
                        + "OID format is \"[1-9](\\.[0-9]+)*]\""));
        repoUId.setAllowBlank(true);
        repoUId.addValidator(new RegExValidator("^[1-9][0-9]*(\\.[1-9][0-9]*)+$", "Value is not correct. A repository unique ID is supposed to be a suite of numbers separated by periods."));
        // service start time
        serviceStartTime.setToolbarHelpButtonTooltip(new ToolTipConfig("Help on Service start time?", "Represents the start time the service being documented took place " +
                "(clinically significant, but not necessarily when the document was " +
                "produced or approved). This may be the same as the encounter time in " +
                "case the service was delivered during an encounter. Encounter time is not " +
                "coded in XDS metadata but may be coded in documents managed by " +
                "XDS. This time is expressed as (date/time/UTC). If present, shall have a " +
                "single value. " +
                "<br/><i>Note: Other times, such as document creation or approval are to be " +
                "recorded, if needed, within the document.</i>"));
        serviceStopTime.setToolbarHelpButtonTooltip(new ToolTipConfig("Help on Service stop time?",
                "Represents the stop time the service being documented took place." +
                        "This may be the same as the encounter time in " +
                        "case the service was delivered during an encounter. This time is expressed as (date/time/UTC). " +
                        "If present, shall have a single value."));
        // source patient id
        sourcePatientId.addFieldValidator(new RegExValidator("^[A-Za-z]*[0-9]+\\^{3}&[1-9][0-9]*(\\.[1-9][0-9]*)+(&ISO)$", "This value is not a correct source patient id."));
//        sourcePatientId.setEmptyTexts("j98789^^^&1.2.3.4.343.1&ISO");
        sourcePatientId.setToolbarHelpButtonTooltip(new ToolTipConfig("Help on Source Patient ID?", "The sourcePatientId represents the subject of care medical record " +
                "Identifier (e.g., Patient Id) in the local patient Identifier Domain of the " +
                "Document Source. It shall contain two parts:" +
                "<ul><li>Authority Domain Id</li>" +
                "<li>An Id in the above domain (e.g., Patient Id).</li></ul>" +
                "This sourcePatientId is not intended to be updated once the Document is " +
                "registered (just as the Document content and metadata itself will not be " +
                "updated without replacing the previous document). As this " +
                "sourcePatientId may have been merged by the source actor, it may no " +
                "longer be in use within the Document Source (EHR-CR). It is only " +
                "intended as an audit/checking mechanism and has occasional use for " +
                "Document Consumer Actors. There can be only one Slot named " +
                "sourcePatientId."));
        // source patient info
        sourcePatientInfo.setToolbarHelpButtonTooltip(new ToolTipConfig("Help on Source Patient Info?", "These elements should contain demographics information of the patient to " +
                "whose medical record this document belongs. It is made several values and should be formatted as follow:<br/>" +
                "<b>PID-3</b> should include the source patient identifier.<br/>" +
                "<b>PID-5</b> should include the patient name.<br/>" +
                "<b>PID-8</b> should code the patient gender as <br/>" +
                "<center><i>M Male - F Female - O Other - U Unknown</i></center>" +
                "<b>PID-7</b> should include the patient date of birth.<br/>" +
                "<b>PID-11</b> should include the patient address.<br/>" +
                "PID-2, PID-4, PID-12 and PID-19 should not be used.<br/>"));
        sourcePatientInfo.addFieldValidator(new RegExValidator("^PID-(((3|5|11|2|4|12|19)\\|)|(8\\|(M|F|O|U)$)|(7\\|(((19|20)\\d\\d)(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])))$)", "This value is not a correct source patient info."));
        // title
        titlesGrid.setToolbarHelpButtonTooltip(new ToolTipConfig("Help on Titles?", "Represents the title of the document. Clinical documents often do not " +
                "have a title, and are collectively referred to by the display name of the " +
                "classCode (e.g., a \"consultation\" or \"progress note\"). Where these display " +
                "names are rendered to the clinician, or where the document has a unique " +
                "title, the title component shall be used. Max length, 128 bytes, UTF-8. " +
                "Each title must be the same in a different language."));
        // type code
        typeCode.setEmptyText("Select a type...");
        typeCode.clear();
        typeCode.setAllowBlank(false);
        // unique id
        uniqueId.setAllowBlanks(false, false);uniqueId.addValueFieldValidator(new RegExValidator("^[1-9][0-9]*(\\.[1-9][0-9]*)+(\\^[1-9][0-9]+)?$", "Value's format is not a correct. It is supposed to be a suite of figures separated by periods."));

        uniqueId.setEmptyTexts("ex: 2008.8.1.35447^5846", "ex: 2008.8.1.35447");
        uniqueId.setToolTipConfigs(
                new ToolTipConfig("Unique ID is an OID", "As defined in the HL7 implementation for OID (http://www.hl7.org/implement/standards/product_brief.cfm?product_id=210)<br/>Unique ID format is \"[1-9](\\.[0-9]+)*]\""),
                new ToolTipConfig(
                        "idType is an OID",
                        "As defined in the HL7 implementation for OID (http://www.hl7.org/implement/standards/product_brief.cfm?product_id=210)<br/>Unique ID format is \"[1-9](\\.[0-9]+)*]\""));
        // uri
        uri.setAllowBlank(true);
        uri.setEmptyText("ex: uriO");
        uri.setToolTipConfig(new ToolTipConfig("URI is a string", "It should contain less than 256 characters"));
    }

}
