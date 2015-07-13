package gov.nist.hit.ds.xdstools3.client.activitiesAndPlaces;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import gov.nist.hit.ds.xdstools3.client.Xdstools3ActivityView;
import gov.nist.hit.ds.xdstools3.client.util.injection.Xdstools3GinInjector;

import javax.inject.Inject;

/**
 * Finds the activity to run for a given Place, used to configure an ActivityManager.
 * It binds the Places with the right Activities.
 *
 * @see TabPlace
 * @see com.google.gwt.activity.shared.ActivityManager
 *
 * Created by onh2 on 9/22/2014.
 */
public class Xdstools3ActivityMapper implements ActivityMapper {

    @Inject
    public Xdstools3ActivityMapper() {
        super();
    }

    /**
     * This method is supposed to return the right Activity for a given Place to load.
     * It sets the id of the tab to load in Xdstools3ActivityView and return this Activity.
     * The Activity will use this id to know which tab to open when it starts.
     *
     * @see gov.nist.hit.ds.xdstools3.client.Xdstools3ActivityView#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.gwt.event.shared.EventBus)
     * @param place Place to load
     * @return the right Activity for a given place to load.
     */
    @Override
    public Activity getActivity(Place place) {
        Xdstools3ActivityView xdstools3ActivityView = Xdstools3GinInjector.injector.getXdstools3();
        //TODO check if place name exists otherwise redirect to home
        xdstools3ActivityView.setTabId(((TabPlace) place).getTabId());
        return xdstools3ActivityView;
    }
}
