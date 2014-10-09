package gov.nist.toolkit.xdstools3.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import gov.nist.toolkit.xdstools3.client.util.Util;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Created by onh2 on 9/22/2014.
 */
public class Xdstools3ActivityMapper implements ActivityMapper {

    @Inject
    public Xdstools3ActivityMapper() {
        super();
    }

    @Override
    public Activity getActivity(Place place) {
        Xdstools3ActivityView xdstools3ActivityView =Xdstools3GinInjector.injector.getXdstools3();
        //TODO check if place name exists otherwise redirect to home
        xdstools3ActivityView.setTabId(((TabPlace) place).getTabId());
        return xdstools3ActivityView;
    }
}
