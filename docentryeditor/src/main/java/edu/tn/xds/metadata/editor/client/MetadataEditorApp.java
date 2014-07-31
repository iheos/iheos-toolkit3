package edu.tn.xds.metadata.editor.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import edu.tn.xds.metadata.editor.client.event.MetadataEditorEventBus;
import edu.tn.xds.metadata.editor.client.home.WelcomePlace;
import edu.tn.xds.metadata.editor.client.root.MetadataEditorAppView;

import java.util.logging.Logger;

/**
 * This is the class to use
 */
public class MetadataEditorApp implements IsWidget {
    private final static MetadataEditorGinInjector injector = MetadataEditorGinInjector.instance;
    protected static Logger logger = Logger.getLogger(MetadataEditorApp.class.getName());
    private final MetadataEditorEventBus eventBus = injector.getEventBus();
    private final SimplePanel activityPanel = new SimplePanel();
    private MetadataEditorAppView appView;

    public MetadataEditorApp(){
        appView = injector.getMetadataEditorAppView();

        PlaceController placeController = injector.getPlaceController();

        MetadataEditorActivityMapper activityMapper = new MetadataEditorActivityMapper();
        ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
        activityManager.setDisplay(activityPanel);

        MetadataEditorAppPlaceHistoryMapper historyMapper = GWT.create(MetadataEditorAppPlaceHistoryMapper.class);
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);

        historyHandler.register(placeController, eventBus, new WelcomePlace("Welcome"));

        activityPanel.add(appView.asWidget());

        historyHandler.handleCurrentHistory();
//        appView.addResizeHandler(new ResizeHandler() {
//            @Override
//            public void onResize(ResizeEvent event) {
//                appView.forceLayout();
//                appView.setResize(true);
//            }
//        });
    }

    @Override
    public Widget asWidget() {
        return activityPanel;
    }

    public static MetadataEditorGinInjector getInjector() {
        return injector;
    }

    public MetadataEditorEventBus getEventBus() {
        return eventBus;
    }
}
