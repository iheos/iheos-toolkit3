package gov.nist.toolkit.xdstools3.client.xdstools2Widgets.adapters;

import com.google.gwt.user.client.ui.VerticalPanel;

import gov.nist.toolkit.xdstools2.client.TabbedWindow;
import gov.nist.toolkit.xdstools2.client.tabs.actorConfigTab.ActorConfigTab;
import gov.nist.toolkit.xdstools2.client.tabs.genericQueryTab.GenericQueryTab;
import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.CloseableTabWidget;

public class AdapterV2V3OrNot {

	/**
	 * Takes tab contents from xdstools2 and puts it into a tab object usable in xdstools3 and compatible with SmartGWT.
	 * 
	 * @param v2Tab Comes from the xdstools2 package (v2 GUI).
	 * @param title The title of the tab that will be created.
	 * @return An object compatible with xdstools3 (v3 GUI). 
	 */
	public CloseableTabWidget getV2Tab(GenericQueryTab v2Tab, String title){
		//CloseableTabWidget tab = new CloseableTabWidget(title);
		VerticalPanel panel = new VerticalPanel();
		//panel.add((TabbedWindow) v2Tab); // which function returns the actual contents of the tab?
	//	new ActorConfigTab().onAbstractTabLoad(container, true, null);
		return null;
		
	}
}
