package gov.nist.hit.ds.docentryeditor.client.editor.submissionSetEditor;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import gov.nist.hit.ds.docentryeditor.client.generics.ActivityDisplayer;
import gov.nist.hit.ds.docentryeditor.client.generics.GenericMVP;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsSubmissionSet;

import javax.inject.Inject;

/**
 * Place for the Submission Set editor.
 * This enables the navigation within the application thanks to
 * the Activity-place design.
 */
public class SubmissionSetEditorActivity extends AbstractActivity{
    @Inject
    ActivityDisplayer displayer;

    GenericMVP<XdsSubmissionSet,SubmissionSetEditorView,SubmissionSetEditorPresenter> editorMVP;

    @Inject
    SubmissionSetEditorView editorView;

    @Inject
    SubmissionSetEditorPresenter editorPresenter;

    SimpleContainer sc;
    BorderLayoutContainer blc;

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        editorMVP=buildEditorMVP();
        editorMVP.init();
        displayer.display(getContainer(), acceptsOneWidget, eventBus);
    }

    private Widget getContainer() {
        sc = new SimpleContainer();
        blc = new BorderLayoutContainer();

        ContentPanel validationView = new ContentPanel();
        validationView.setHeadingText("Validation");

        ContentPanel center = new ContentPanel();
        center.setHeaderVisible(false);
        center.add(editorMVP.getDisplay());

        blc.setCenterWidget(center);

        BorderLayoutContainer.BorderLayoutData southData = new BorderLayoutContainer.BorderLayoutData(250);
        southData.setCollapsible(true);
        southData.setSplit(true);
        southData.setCollapsed(true);

        blc.setSouthWidget(validationView, southData);

        blc.collapse(Style.LayoutRegion.SOUTH);

        sc.add(blc);

        return sc.asWidget();
    }

    private GenericMVP<XdsSubmissionSet, SubmissionSetEditorView, SubmissionSetEditorPresenter> buildEditorMVP() {
        return new GenericMVP<XdsSubmissionSet,SubmissionSetEditorView,SubmissionSetEditorPresenter>(editorView,editorPresenter);
    }
}
