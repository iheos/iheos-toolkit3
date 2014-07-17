package edu.tn.xds.metadata.editor.client;

/* Imports */

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.tn.xds.metadata.editor.client.event.MetadataEditorEventBus;
import edu.tn.xds.metadata.editor.client.home.WelcomePlace;
import edu.tn.xds.metadata.editor.client.root.MetadataEditorAppView;

import java.util.logging.Logger;

/**
 * This is the XDS Metadata Editor Application EntryPoint. That's the first
 * class loaded, which instantiate the different object global to the
 * application.
 */
public class MetadataEditorApp implements EntryPoint {

    private final static MetadataEditorGinInjector injector = MetadataEditorGinInjector.instance;
    protected static Logger logger = Logger.getLogger(MetadataEditorApp.class.getName());
    private final MetadataEditorEventBus eventBus = injector.getEventBus();
    private final SimplePanel activityPanel = new SimplePanel();
    private MetadataEditorAppView appView;

    @SuppressWarnings("deprecation")
    @Override
    public void onModuleLoad() {
        appView = injector.getMetadataEditorAppView();

        PlaceController placeController = injector.getPlaceController();

        MetadataEditorActivityMapper activityMapper = new MetadataEditorActivityMapper();
        ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
        activityManager.setDisplay(activityPanel);

        MetadataEditorAppPlaceHistoryMapper historyMapper = GWT.create(MetadataEditorAppPlaceHistoryMapper.class);
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);

        historyHandler.register(placeController, eventBus, new WelcomePlace("Welcome"));

        activityPanel.add(appView.asWidget());

        RootPanel.get("editorAppContainer").add(activityPanel);

        logger.info("Application Started!");

        historyHandler.handleCurrentHistory();

//        appView.addResizeHandler(new ResizeHandler() {
//            @Override
//            public void onResize(ResizeEvent event) {
//                appView.setWidth(event.getWidth()-25);
//                appView.setHeight(event.getHeight());
//            }
//        });
    }


}
