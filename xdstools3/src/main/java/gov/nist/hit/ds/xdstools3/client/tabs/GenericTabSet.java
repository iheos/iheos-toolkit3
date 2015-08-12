package gov.nist.hit.ds.xdstools3.client.tabs;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * Builds the main set of tabs for the application
 */
public class GenericTabSet extends TabSet {

//    private Logger logger= Logger.getLogger(GenericTabSet.class.getName());

	public GenericTabSet(){
	    this.setTabBarPosition(Side.TOP);
	    this.setTabBarAlign(Side.LEFT);
	}

    public Tab findTab(Tab tab){
        try {
        for (Tab t:getTabs()) {

//            Window.alert("******** findTab(): is " + t.getTitle() + " == " + tab.getTitle() + " : " + t.getTitle().equals(tab.getTitle()) );

            if (t.getTitle().equals(tab.getTitle())) {
                return t;
            }
        }
        } catch (Throwable t) {
            Window.alert("findTab error!");
            Window.alert("findTab "+  tab.getTitle() +"," + getTabs().length + " error:" + t.toString() );
        }
        return null;
    }


}
