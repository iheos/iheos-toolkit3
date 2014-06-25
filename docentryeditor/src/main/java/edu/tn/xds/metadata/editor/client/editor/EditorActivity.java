package edu.tn.xds.metadata.editor.client.editor;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
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
import edu.tn.xds.metadata.editor.client.generics.ActivityDisplayer;
import edu.tn.xds.metadata.editor.client.generics.GenericMVP;
import edu.tn.xds.metadata.editor.shared.model.DocumentModel;

import javax.inject.Inject;

public class EditorActivity extends AbstractActivity {

    @Inject
    ActivityDisplayer displayer;

    GenericMVP<DocumentModel, DocumentModelEditorView, DocumentModelEditorPresenter> editorMVP;
    GenericMVP<DocumentModel, ValidationView, ValidationPresenter> validationMVP;

    @Inject
    DocumentModelEditorView editorView;
    @Inject
    DocumentModelEditorPresenter editorPresenter;
    @Inject
    ValidationView validationView;
    @Inject
    ValidationPresenter validationPresenter;

    SimpleContainer sc;BorderLayoutContainer blc;

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        editorMVP = buildEditorMVP();
        editorMVP.init();
        validationMVP = buildValidationMVP();
        validationMVP.init();
        displayer.display(getContainer(), panel, eventBus);
    }

    private Widget getContainer() {
        sc= new SimpleContainer();
        blc= new BorderLayoutContainer();

        ContentPanel validationView = new ContentPanel();
        validationView.setHeadingText("Validation");

        ContentPanel center = new ContentPanel();
        center.setHeaderVisible(false);
        center.add(editorMVP.getDisplay());

        blc.setCenterWidget(center);

        BorderLayoutData southData = new BorderLayoutData(250);
        southData.setCollapsible(true);
        southData.setSplit(false);
        southData.setCollapsed(true);

        blc.setSouthWidget(validationView, southData);

        blc.collapse(LayoutRegion.SOUTH);

        sc.add(blc);

        return sc.asWidget();
    }

    public GenericMVP<DocumentModel, DocumentModelEditorView, DocumentModelEditorPresenter> buildEditorMVP() {
        return new GenericMVP<DocumentModel, DocumentModelEditorView, DocumentModelEditorPresenter>(editorView, editorPresenter);
    }

    public GenericMVP<DocumentModel, ValidationView, ValidationPresenter> buildValidationMVP() {
        return new GenericMVP<DocumentModel, ValidationView, ValidationPresenter>(validationView, validationPresenter);
    }
}
