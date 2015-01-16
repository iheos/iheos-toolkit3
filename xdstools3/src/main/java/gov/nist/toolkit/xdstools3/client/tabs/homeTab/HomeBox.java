package gov.nist.toolkit.xdstools3.client.tabs.homeTab;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HStack;
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
    public void addItem(String item_title, boolean isUnderConstruction){
        HomeLinkButton button = new HomeLinkButton(item_title);
        button.setBaseStyle("home-button");

        if (isUnderConstruction){
            Label underConstructionLabel = new Label("Under construction");
            underConstructionLabel.setWidth(150);
            underConstructionLabel.setLayoutAlign(VerticalAlignment.CENTER);
            underConstructionLabel.setStyleName("under-construction-label");
            button.setWidth(280);
            HStack stack = new HStack();
            stack.setAutoHeight();
            stack.addMembers(button, underConstructionLabel);
            addMember(stack);
        } else {
            addMember(button);
            button.setWidth(420);
        }
    }

}
