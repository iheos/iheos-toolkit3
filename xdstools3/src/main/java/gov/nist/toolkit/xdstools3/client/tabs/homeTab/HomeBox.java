package gov.nist.toolkit.xdstools3.client.tabs.homeTab;

import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.customWidgets.design.Formatter;
import gov.nist.toolkit.xdstools3.client.customWidgets.design.IconLabel;

/**
 * Formatted box that displays a title with optional icon, and a list of links as buttons.
 */
public class HomeBox extends VStack {
    IconLabel title;

    public HomeBox(String _title){
        super();
        title = Formatter.createHomeSubtitle(_title);
        addMember(title);
    }

    /**
     * Sets an icon before a label title. This function encapsulates an existing Smartgwt function adding an icon to a Label.
     * @param path The path of the icon. Must be relative to webapp/images.
     */
    public void setIcon(String path){
        title.setIcon(path);
    }

    /**
     * Creates a new HomeLinkButton item and adds it to the container.
     * @param item_title The title of the button.
     * @see HomeLinkButton
     */
    public void addItem(String item_title){
        HomeLinkButton button = new HomeLinkButton(item_title);
        addMember(button);
    }


}
