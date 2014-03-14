package gov.nist.toolkit.xdstools3.client.customWidgets.tabs;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.TabSet;

public class GenericTabSetWidget extends TabSet {
	
	public GenericTabSetWidget(){
	    this.setTabBarPosition(Side.TOP);  
	    this.setTabBarAlign(Side.LEFT); 
	    this.setWidth100();
	    this.setHeight100();
	}
	
	
}
