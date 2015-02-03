package gov.nist.hit.ds.docentryeditor.client.widgets;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import gov.nist.hit.ds.docentryeditor.client.MetadataEditorGinInjector;
import gov.nist.hit.ds.docentryeditor.client.event.MetadataEditorEventBus;
import gov.nist.hit.ds.docentryeditor.client.resources.AppImages;

/**
 * Created by onh2 on 1/20/2015.
 */
public class HomeButton extends TextButton {

    private MetadataEditorEventBus eventBus;

    public HomeButton() {
        eventBus= MetadataEditorGinInjector.instance.getEventBus();
        setText("Back to home page");
        setIcon(AppImages.INSTANCE.home());
        bindUI();
    }

    private void bindUI() {
        addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent selectEvent) {
                eventBus.fireBackToHomePageEvent();
//                placeController.goTo(new WelcomePlace("Welcome"));
            }
        });
    }
}
