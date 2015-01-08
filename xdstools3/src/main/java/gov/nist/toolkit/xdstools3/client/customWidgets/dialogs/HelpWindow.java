package gov.nist.toolkit.xdstools3.client.customWidgets.dialogs;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;

/**
 * Creates a "Help" window, displayed at the top right of each tab.
 * Uses the SmartGWT Window component. All parameters, such as height, width, canDragResize, can be changed
 * after the object has been created.
 */
public class HelpWindow extends Window {

    public HelpWindow (String contents){
        setTitle("Help");
        setWidth(500);
        setHeight100();
        setCanDragScroll(true);
        setContents(contents);
    }

    /**
     * Sets the contents of the help window.
     * @param contents
     */
    public void setContents(String contents){
        Label l = new Label(contents);
        l.setWidth100();
        l.setHeight100();
        l.setPadding(10);
        l.setValign(VerticalAlignment.TOP);
        addItem(l);
    }
}
