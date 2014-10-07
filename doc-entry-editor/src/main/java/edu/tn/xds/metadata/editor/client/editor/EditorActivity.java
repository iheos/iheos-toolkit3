package edu.tn.xds.metadata.editor.client.editor;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.LayoutRegion;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import edu.tn.xds.metadata.editor.client.editor.validation.ValidationPresenter;
import edu.tn.xds.metadata.editor.client.editor.validation.ValidationView;
import edu.tn.xds.metadata.editor.client.event.MetadataEditorEventBus;
import edu.tn.xds.metadata.editor.client.event.XdsEditorLoadedEvent;
import edu.tn.xds.metadata.editor.client.generics.ActivityDisplayer;
import edu.tn.xds.metadata.editor.client.generics.GenericMVP;
import edu.tn.xds.metadata.editor.shared.model.XdsDocumentEntry;

import javax.inject.Inject;

public class EditorActivity extends AbstractActivity {

    @Inject
    ActivityDisplayer displayer;

    GenericMVP<XdsDocumentEntry, DocumentModelEditorView, DocumentModelEditorPresenter> editorMVP;
    GenericMVP<XdsDocumentEntry, ValidationView, ValidationPresenter> validationMVP;

    @Inject
    DocumentModelEditorView editorView;
    @Inject
    DocumentModelEditorPresenter editorPresenter;
    @Inject
    ValidationView validationView;
    @Inject
    ValidationPresenter validationPresenter;
    @Inject
    MetadataEditorEventBus eventBus;

    SimpleContainer sc;
    BorderLayoutContainer blc;

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {

        editorMVP = buildEditorMVP();
        editorMVP.init();
        validationMVP = buildValidationMVP();
        validationMVP.init();
        displayer.display(getContainer(), panel, eventBus);
//        this.eventBus.fireEditNewEvent(new EditNewEvent());
        this.eventBus.fireXdsEditorLoadedEvent(new XdsEditorLoadedEvent());
    }

    /**
     * Build editor layout with borderlayoutcontainer.
     * Made of a main panel for the editor itself and a collapsible and splittable validation panel underneath.
     *
     * @return widget editor container
     */
    private Widget getContainer() {
        sc = new SimpleContainer();
        blc = new BorderLayoutContainer();

        ContentPanel validationView = new ContentPanel();
        validationView.setHeadingText("Validation");

        ContentPanel center = new ContentPanel();
        center.setHeaderVisible(false);
        center.add(editorMVP.getDisplay());

        blc.setCenterWidget(center);

        BorderLayoutData southData = new BorderLayoutData(250);
        southData.setCollapsible(true);
        southData.setSplit(true);
        southData.setCollapsed(true);

        blc.setSouthWidget(validationView, southData);

        blc.collapse(LayoutRegion.SOUTH);

        sc.add(blc);

        return sc.asWidget();
    }

    /**
     * @return
     */
    public GenericMVP<XdsDocumentEntry, DocumentModelEditorView, DocumentModelEditorPresenter> buildEditorMVP() {
        return new GenericMVP<XdsDocumentEntry, DocumentModelEditorView, DocumentModelEditorPresenter>(editorView, editorPresenter);
    }

    public GenericMVP<XdsDocumentEntry, ValidationView, ValidationPresenter> buildValidationMVP() {
        return new GenericMVP<XdsDocumentEntry, ValidationView, ValidationPresenter>(validationView, validationPresenter);
    }
}
