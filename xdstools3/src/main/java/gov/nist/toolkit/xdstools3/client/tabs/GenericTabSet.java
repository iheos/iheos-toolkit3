package gov.nist.toolkit.xdstools3.client.tabs;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class GenericTabSet extends TabSet {

	public GenericTabSet(){
	    this.setTabBarPosition(Side.TOP);
	    this.setTabBarAlign(Side.LEFT);
//        setStyleName("tabset");
	}

    public Tab findTab(Tab tab){
        for (Tab t:getTabs())
            if (t.getTitle().equals(tab.getTitle())) {
                return t;
            }
        return null;
    }


}
