package gov.nist.toolkit.xdstools3.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.toolkit.xdstools3.client.util.Util;

import java.util.logging.Logger;

/**
 * Created by onh2 on 9/22/2014.
 */
public class Xdstools3EP implements EntryPoint {
    private static final Xdstools3GinInjector injector=Xdstools3GinInjector.injector;

    private static final EventBus eventBus= injector.getEventBus();
    private static final ActivityMapper activityMapper=injector.getActivityMapper();
//    private SimplePanel simplePanel=new SimplePanel();
    private Xdstools3ActivityView xdstools3ActivityView=injector.getXdstools3();
//    VLayout layout = new VLayout();
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void onModuleLoad() {
//        xdstools3ActivityView =injector.getXdstools3();
        xdstools3ActivityView.run();
//        layout.addMember(xdstools3ActivityView.getDisplay());
//        layout.draw();

        PlaceController placeController = injector.getPlaceController();
        ActivityMapper actMapper = new Xdstools3ActivityMapper();
        ActivityManager activityManager = new ActivityManager(actMapper, eventBus);
        activityManager.setDisplay(xdstools3ActivityView);

        Xdstools3PlaceHistoryMapper historyMapper = GWT.create(Xdstools3PlaceHistoryMapper.class);
        final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);

        historyHandler.register(placeController, eventBus, new TabPlace("HOME"));

//        simplePanel.setWidget(layout);

        // Goes to place represented on URL or default place
        historyHandler.handleCurrentHistory();

//        xdstools3ActivityView.getDisplay().redraw();
//        RootLayoutPanel.get().add(simplePanel);
//        RootLayoutPanel.get().clear();
//        RootPanel.get().clear();

    }
}
