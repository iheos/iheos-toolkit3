package gov.nist.toolkit.xdstools3.client.tabs;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.TabSet;

public class GenericTabSet extends TabSet {
	
	public GenericTabSet(){
	    this.setTabBarPosition(Side.TOP);  
	    this.setTabBarAlign(Side.LEFT); 
	    this.setWidth100();
	    this.setHeight100();
        setStyleName("tabset");
	}
	
	
}
