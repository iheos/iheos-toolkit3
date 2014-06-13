package gov.nist.toolkit.xdstools3.client.customWidgets.tabs;

import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Defines the profile for creating a tab.
 * @author dazais
 * @see GenericCloseableTab
 *
 */
public interface TabInterface {
	
	public VLayout getPanel();
	public void setHeader(String s);
	public void setSubtitle(String s);
	
}
