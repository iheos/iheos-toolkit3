package gov.nist.toolkit.xdstools3.client.tabs;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.TabSet;

public class TabSetWidget extends TabSet {
	
	public TabSetWidget(){
	    this.setTabBarPosition(Side.TOP);  
	    this.setTabBarAlign(Side.LEFT); 
	    this.setWidth100();
	    this.setHeight100();
	}
	
	
}
