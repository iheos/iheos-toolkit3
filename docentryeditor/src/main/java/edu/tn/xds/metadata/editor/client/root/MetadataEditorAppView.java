package edu.tn.xds.metadata.editor.client.root;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.Viewport;
import edu.tn.xds.metadata.editor.client.MetadataEditorGinInjector;
import edu.tn.xds.metadata.editor.client.generics.GenericMVP;
import edu.tn.xds.metadata.editor.client.root.submission.SubmissionMenuData;
import edu.tn.xds.metadata.editor.client.root.submission.SubmissionPanelPresenter;
import edu.tn.xds.metadata.editor.client.root.submission.SubmissionPanelView;

/**
 * This class contains layout objects to handle the global interface layout,
 * which is divided into 3 parts.
 *
 * @author OLIVIER
 */
public class MetadataEditorAppView extends Viewport {

    NorthPanel north; // interface for file loading and saving
    CenterPanel center; // main edtior fields
    GenericMVP<SubmissionMenuData, SubmissionPanelView, SubmissionPanelPresenter> submissionMVP;

    SubmissionPanelView submissionPanelView;
    SubmissionPanelPresenter submissionPanelPresenter;

    public MetadataEditorAppView() {
        super();

        final MetadataEditorGinInjector injector = MetadataEditorGinInjector.instance;

        submissionPanelPresenter = injector.getSubmissionPanelPresenter();
        submissionPanelView = injector.getSubmissionPanelView();

        BorderLayoutContainer con = new BorderLayoutContainer();
        con.setBorders(true);

        // NORTH
//        north = injector.getNorthPanel();
//        BorderLayoutData northData = new BorderLayoutData(35);
//        northData.setMargins(new Margins(5, 5, 5, 5));
//        con.setNorthWidget(north, northData);

        // CENTER
        center = new CenterPanel();
        MarginData centerData = new MarginData(0, 5, 5, 5);
        BorderLayoutContainer c = new BorderLayoutContainer();
        c.setCenterWidget(center, centerData);

        con.setCenterWidget(c);

        // WEST
        submissionMVP = buildSubmissionMVP();
        submissionMVP.init();
        BorderLayoutData westData = new BorderLayoutData(200);
        westData.setMargins(new Margins(0, 5, 5, 5));
        westData.setCollapsible(true);
        westData.setSplit(false);

        con.setWestWidget(submissionMVP.getDisplay(), westData);


        SimpleContainer simple = new SimpleContainer();
        simple.add(con, new MarginData(0, 0, /*10*/0, 0));
        add(con);

//        north.start();
//        submissionMVP.start();
    }

    public void setCenterDisplay(Widget display) {
        center.clear();
        center.add(display);
        center.forceLayout();
    }

    public GenericMVP<SubmissionMenuData, SubmissionPanelView, SubmissionPanelPresenter> buildSubmissionMVP() {
        return new GenericMVP<SubmissionMenuData, SubmissionPanelView, SubmissionPanelPresenter>(submissionPanelView, submissionPanelPresenter);
    }

}
