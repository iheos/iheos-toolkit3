package gov.nist.hit.ds.xdstools3.client.customWidgets.buttons;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import gov.nist.hit.ds.xdstools3.client.customWidgets.dialogs.HelpWindow;

/**
 * Creates a help button to be displayed on each tab. When clicked, the button displays a help window.
 * Usage: create the help button where needed. Set the desired contents of the Help Window. The rest is automatic.
 * Created by dazais on 12/20/2014.
 */
public class HelpButton extends IButton {
    private HelpWindow helpWindow;
    private String helpWindowContents;

    /**
     * Creates a Help button with a question mark icon.
     * @param helpContainer
     * @param helpContents
     */
    public HelpButton(final Canvas helpContainer, String helpContents) {
        final Canvas helpWindowContainer = helpContainer;
        helpWindowContents = helpContents;
        setTitle("Help");
        setIcon("icons/glyphicons/16px-blue/glyphicons-help-16px.png");

        // When clicked, display a new Help Window. Contents of the Help Window must be set in the
        // tab from which the call is made.
        addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                helpWindow = new HelpWindow(helpWindowContents);
                helpWindow.setLayoutAlign(Alignment.RIGHT);
                helpWindowContainer.addChild(helpWindow);
            }
        });
    }

    public HelpWindow getHelpWindow() {
        return helpWindow;
    }

    /**
     * Registers the Help Window contents locally. This content will be set if / when the HelpWindow
     * is created.
     * @param contents
     */
    public void setHelpWindowContents(String contents){
        helpWindowContents = contents;
    }
}
