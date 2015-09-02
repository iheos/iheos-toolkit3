package gov.nist.hit.ds.xdstools3.client.tabs.homeTab;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.hit.ds.xdstools3.client.customWidgets.design.IconLabel;
import gov.nist.hit.ds.xdstools3.client.customWidgets.design.Formatter;

/**
 * Formatted box that displays a title with optional icon, and a list of links as buttons.
 */
public class HomeBox extends VStack {
    IconLabel title;

    public HomeBox(String _title){
        super();
        this.setStyleName("home-box");
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
     * Creates a new HomeLinkButton item and adds it to the container. If the tab is labeled as being under construction,
     * the item added will include an "Under construction" label, displayed inline, positioned to the right of the item.
     * @param item_title The title of the button.
     * @param isUnderConstruction Boolean indicating whether the tab being linked to is under construction.
     * @see HomeLinkButton
     */
    public void addItem(String item_title, int version, boolean isUnderConstruction){
        HomeLinkButton button = new HomeLinkButton(item_title);
        button.setBaseStyle("home-button");
        button.setWidth(250);

        // Horizontal stack to group all display elements for one link to a tool on the Home Page
        HStack stack = new HStack();
        stack.setAutoHeight();

        stack.addMember(button);

        switch(version){
            case 2:  // do nothing when it is the old version number
                break;
            case 3:
                // when the new version number is used, display a label
                Label versionLabel = new Label("NEW");
                versionLabel.setWidth(40);
                versionLabel.setLayoutAlign(VerticalAlignment.CENTER);
                versionLabel.setStyleName("version-homebox-label");

                stack.addMember(versionLabel);
                break;
            default:
                // do nothing when the version number was not filled out or is a random number
                break;
        }
        if (isUnderConstruction) {
            Label underConstructionLabel = new Label("Under construction");
            underConstructionLabel.setWidth(110);
            underConstructionLabel.setLayoutAlign(VerticalAlignment.CENTER);
            underConstructionLabel.setStyleName("under-construction-homebox-label");

            stack.addMember(underConstructionLabel);
        }

        // Add link button to the HomeBox
        addMember(stack);
    }

}
